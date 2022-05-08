package com.softserve.teachua.ui.clubs

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softserve.teachua.MainActivity
import com.softserve.teachua.R
import com.softserve.teachua.app.adapters.ClubsAdapter
import com.softserve.teachua.app.adapters.ClubsLoadStateAdapter
import com.softserve.teachua.app.tools.CategoryToUrlTransformer
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.databinding.FragmentClubsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.adv_search.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds


@AndroidEntryPoint
class ClubsFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentClubsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val clubsViewModel: ClubsViewModel by viewModels()
    private lateinit var clubsAdapter: ClubsAdapter
    private lateinit var progressDialog: ProgressDialog
    private lateinit var dialog: Dialog

    var cities = listOf<String>()
    var districts = mutableListOf<String>()
    var stations = mutableListOf<String>()

    var categories = listOf<String>()
    var listOfSearchedCategories = arrayListOf<String>()

    private var districtByCity = "Київ"
    private var checkboxCounter = 0
    lateinit var toolbar: Toolbar

    private var query = ""
    private var layoutManager =
        LinearLayoutManager(activity?.baseContext, RecyclerView.VERTICAL, false)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        createViewModel()

        _binding = FragmentClubsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.searchEdit.setupClearButtonWithAction()
        checkboxCounter = 0
        createAdvancedSearchDialog()
        setAllClickListenersAndDefaultValues()
        initClubs()
        initDataForAllAdvSpinners()
        updateViewModel()
        updateToolbar()

        println("search " + clubsViewModel.searchClubDto.value.toString())
        println("advancedSearch " + clubsViewModel.advancedSearchClubDto.value.toString())


        return root
    }

    private fun setAllClickListenersAndDefaultValues() {

        binding.searchAdvBtn.setOnClickListener(this)
        binding.searchBtn.setOnClickListener(this)
        binding.clearEdit.setOnClickListener(this)
        dialog.club_adv_search_radioBtn.isChecked = true
        dialog.center_adv_search_radioBtn.isChecked = false
        dialog.isOnline.isChecked = false
    }

    private fun whenLoadingClubs() {
        showLoadingProgressDialog()
        binding.errorText.isVisible = false

    }

    private fun whenErrorLoadingClubs() {

        dismissProgressDialog()
        binding.rcv.isInvisible = true
        binding.errorText.isVisible = true
    }

    private fun whenDataIsClear() {

        println("Data Is Clear")
        dismissProgressDialog()
        binding.rcv.isInvisible = true
        binding.errorText.text = "Cant load matching results for your search"
        binding.errorText.isVisible = true

    }


    private fun createViewModel() {


        clubsViewModel.loadCities()
        clubsViewModel.loadCategories()
        updateDistricts()


    }

    private fun updateToolbar() {
         toolbar = binding.toolbar
        (requireActivity() as MainActivity).toolbar.visibility = View.GONE
        (requireActivity() as MainActivity).setToobar(toolbar)
    }

    private fun updateViewModel() {

        clubsViewModel.viewModelScope.launch {

            clubsViewModel.clubs.collectLatest { pagingData -> clubsAdapter.submitData(pagingData) }


        }
    }

    private fun updateDistricts() {

        lifecycleScope.launch {
            clubsViewModel.loadDistricts(districtByCity)

        }


    }

    private fun updateStations() {

        lifecycleScope.launch {
            clubsViewModel.loadStations(districtByCity)

        }
    }


    private fun initClubs() {

        binding.rcv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        clubsAdapter = ClubsAdapter(requireContext())

        binding.rcv.adapter = clubsAdapter.withLoadStateHeaderAndFooter(
            header = ClubsLoadStateAdapter { clubsAdapter.retry() },
            footer = ClubsLoadStateAdapter { clubsAdapter.retry() })

    }

    private fun initDataForAllAdvSpinners() {
        lifecycleScope.launch {

            getCitiesForSpinner()
            getDistrictsForSpinner()
            getStationsForSpinner()
            getCategoriesForCheckBox()


        }
    }

    private fun initAdapterState() {
        lifecycleScope.launch {
            clubsAdapter.loadStateFlow.collectLatest { loadStates ->


                when (loadStates.refresh) {

                    is LoadState.Loading -> {

                        println("Loading Case")
                        whenLoadingClubs()
                    }

                    is LoadState.Error -> {
                        println("Error Case")
                        whenErrorLoadingClubs()
                    }


                    else -> {
                        println("Else Case")
                        dismissProgressDialog()

                        if (clubsAdapter.itemCount < 1) {
                            whenDataIsClear()
                            println("clubs ada" + clubsAdapter.itemCount)
                        }
                        println("NotLoading Case")
                        binding.rcv.isInvisible = false
                        //error_text.isVisible = false
                    }
                }
            }
        }


    }


    @SuppressLint("ClickableViewAccessibility")
    fun EditText.setupClearButtonWithAction() {

        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                if (editable?.isNotEmpty() == true) binding.clearEdit.visibility =
                    View.VISIBLE else binding.clearEdit.visibility = View.INVISIBLE
            }

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) =
                Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) =
                Unit
        })

    }


    private fun citySpinnerPicker(cities: ArrayList<String>) {

        val citySpinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
            R.layout.item_dropdown, cities)

        citySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCities.adapter = citySpinnerAdapter

        binding.spinnerCities.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    pos: Int,
                    id: Long,
                ) {
                    println(parent!!.getItemAtPosition(pos))
                    //iewModelStore.clear()

                    clubsViewModel.searchClubDto.value?.cityName =
                        parent.getItemAtPosition(pos).toString()
                    Toast.makeText(requireContext(),
                        parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_SHORT).show()
                    //clubsViewModel.advancedSearchClubDto.value?.isAdvanced = false
                    //createViewModel()
                    //addDataToVM()
                    clubsAdapter.refresh()


                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }


            }

    }

    private fun getCitiesForSpinner() {

        lifecycleScope.launch {
            clubsViewModel.cities.collect { data ->

                when (data.status) {

                    Resource.Status.SUCCESS -> {
                        cities = data.data?.map { it.cityName } as ArrayList
                        citySpinnerPicker(cities as ArrayList<String>)
                    }


                }
            }


        }

    }

    private fun getDistrictsForSpinner() {


        lifecycleScope.launch {

            clubsViewModel.districts.collectLatest { data ->
                when (data.status) {
                    Resource.Status.SUCCESS -> {
                        districts = data.data?.map { it.districtName } as ArrayList
                        districts.add(0, "Виберіть район")
                        setUpDefaultSpinner(dialog, districts, R.id.spinner_city_district)
                    }
                }
            }
        }
    }


    private suspend fun getStationsForSpinner() {

        lifecycleScope.launch {

            clubsViewModel.stations.collect { data ->
                when (data.status) {

                    Resource.Status.SUCCESS -> {
                        stations = data.data?.map { it.stationName } as ArrayList
                        stations.add(0, "Виберіть станцію")
                        setUpDefaultSpinner(dialog, stations, R.id.spinner_metro_station)
                    }

                }

            }

        }

    }

    private suspend fun getCategoriesForCheckBox() {

        lifecycleScope.launch {

            clubsViewModel.categories.collect { data ->
                when (data.status) {

                    Resource.Status.SUCCESS -> {
                        categories =
                            data.data?.reversed()?.map { it.categoryName } as ArrayList<String>
                    }

                }

            }

        }

    }

    private fun showLoadingProgressDialog() {
        progressDialog = ProgressDialog(requireContext())

        progressDialog.setTitle("Loading List Of Clubs")
        progressDialog.setMessage("List of clubs is loading, please wait")
        progressDialog.show()


    }

    private fun dismissProgressDialog() {

        progressDialog.dismiss()
    }

    private fun createAdvancedSearchDialog() {
        dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
        dialog.setContentView(R.layout.adv_search)
        dialog.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )

    }


    private fun setUpDefaultSpinner(
        dialog: Dialog,
        arrayRes: List<String>,
        @IdRes spinner: Int,
    ) {
        var sp: Spinner = dialog.findViewById(spinner)

        var citySearchSpinnerAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(),
                R.layout.item_dropdown, arrayRes)
        citySearchSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp.adapter = citySearchSpinnerAdapter


    }

    private fun searchAdvDialog() {

        println("size" + checkboxCounter)

        if (checkboxCounter != categories.size) {

            for (i in 0 until categories.size) {
                val checkBox =
                    LayoutInflater.from(dialog.context)
                        .inflate(R.layout.category_checkbox, rootAdvView, false) as CheckBox
                checkBox.text = categories[i]
                checkBox.setOnClickListener {
                    if (checkBox.isChecked) {
                        listOfSearchedCategories.add(CategoryToUrlTransformer().toUrlEncode(
                            checkBox.text.toString()))
                    } else
                        listOfSearchedCategories.remove(CategoryToUrlTransformer().toUrlEncode(
                            checkBox.text.toString()))
                }
                dialog.rootAdvView.addView(checkBox, 12)
                checkboxCounter++

            }

        }

        dialog.show()

        setUpDefaultSpinner(dialog, cities, R.id.spinner_search_city)
        dialog.spinner_search_city.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    pos: Int,
                    id: Long,
                ) {

                    println("pos" + parent?.getItemAtPosition(pos))
                    districtByCity = parent?.getItemAtPosition(pos).toString()
                    clubsViewModel.advancedSearchClubDto.value?.cityName = districtByCity
                    updateDistricts()
                    updateStations()

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

        dialog.spinner_city_district.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    pos: Int,
                    id: Long,
                ) {
//
                    println("pos" + parent?.getItemAtPosition(pos))
                    var district = parent?.getItemAtPosition(pos).toString()
                    if (pos > 0) {
                        clubsViewModel.advancedSearchClubDto.value?.districtName = district
                    } else {
                        clubsViewModel.advancedSearchClubDto.value?.districtName = null


                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }


        setUpDefaultSpinner(dialog, stations, R.id.spinner_metro_station)

        dialog.spinner_metro_station.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    pos: Int,
                    id: Long,
                ) {

                    println("pos" + parent?.getItemAtPosition(pos))
                    var station = parent?.getItemAtPosition(pos).toString()
                    if (pos > 0) {
                        clubsViewModel.advancedSearchClubDto.value?.stationName = station
                    } else
                        clubsViewModel.advancedSearchClubDto.value?.stationName = null

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

        dialog.apply_search.setOnClickListener {

            println("categories $listOfSearchedCategories")
            clubsViewModel.advancedSearchClubDto.value?.categoriesName =
                listOfSearchedCategories
            clubsViewModel.advancedSearchClubDto.value?.isAdvanced = true
            clubsViewModel.advancedSearchClubDto.value?.isCenter = false
            clubsViewModel.advancedSearchClubDto.value?.sort = "name,asc"
            dialog.hide()
            //viewModelStore.clear()
            // createViewModel()
            // addDataToVM()
            binding.rcv.smoothScrollToPosition(0)
            clubsAdapter.refresh()
            layoutManager.scrollToPositionWithOffset(0, 0)


        }

        dialog.clear_search.setOnClickListener {

            clubsViewModel.advancedSearchClubDto.value?.isAdvanced = false
            clubsViewModel.advancedSearchClubDto.value?.name = ""
            clubsViewModel.advancedSearchClubDto.value?.cityName =
                clubsViewModel.searchClubDto.value?.cityName.toString()
            clubsViewModel.advancedSearchClubDto.value?.districtName = null.toString()
            clubsViewModel.advancedSearchClubDto.value?.stationName = null.toString()
            dialog.club_adv_search_radioBtn.isChecked = true
            dialog.center_adv_search_radioBtn.isChecked = false
            dialog.isOnline.isChecked = false
//
            //searchEdit.text.clear()
            dialog.cancel()
            checkboxCounter = 0
            //viewModelStore.clear()
            //createViewModel()
            // addDataToVM()
            clubsAdapter.refresh()
            binding.rcv.smoothScrollToPosition(0)
        }

    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.searchAdvBtn -> {
                searchAdvDialog()
            }

            R.id.searchBtn -> {
                //viewModelStore.clear()
                clubsViewModel.advancedSearchClubDto.value?.isAdvanced = true
                clubsViewModel.advancedSearchClubDto.value?.name =
                    binding.searchEdit.text.toString()
                clubsViewModel.searchClubDto.value?.clubName
                clubsViewModel.advancedSearchClubDto.value?.cityName =
                    clubsViewModel.searchClubDto.value?.cityName!!
                clubsViewModel.advancedSearchClubDto.value?.districtName = null
                clubsViewModel.advancedSearchClubDto.value?.stationName = null
                //createViewModel()
                // addDataToVM()
                clubsAdapter.refresh()
                binding.searchEdit.isActivated = false
                layoutManager.scrollToPositionWithOffset(0, 0)
                requireContext().hideKeyboard(binding.searchEdit)
                binding.searchEdit.clearFocus()

            }

            R.id.clearEdit -> {
                when (binding.searchEdit.text?.isNotEmpty()) {

                    true -> {


                        binding.searchEdit.text.clear()
                        binding.searchEdit.isActivated = false
                        binding.searchEdit.clearAnimation()
                        clubsViewModel.advancedSearchClubDto.value?.name = ""
                        clubsViewModel.advancedSearchClubDto.value?.isAdvanced = false
                        clubsViewModel.advancedSearchClubDto.value?.sort = ""
                        clubsAdapter.refresh()
                        binding.searchEdit.clearFocus()


                    }

                    false -> {

                        binding.searchEdit.requestFocus()
                        binding.searchEdit.isActivated = true

                        //showKeyboard()
                    }
                }

            }


        }
    }

    override fun onResume() {
        super.onResume()
        initAdapterState()
    }

    override fun onDestroy() {
        super.onDestroy()
        toolbar.visibility = View.GONE
        binding.rcv.visibility = View.GONE
        checkboxCounter = 0
        dismissProgressDialog()
    }
}