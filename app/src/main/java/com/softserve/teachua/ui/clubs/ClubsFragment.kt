package com.softserve.teachua.ui.clubs

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softserve.teachua.MainActivity
import com.softserve.teachua.R
import com.softserve.teachua.app.adapters.ClubsAdapter
import com.softserve.teachua.app.adapters.ClubsLoadStateAdapter
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.dto.AdvancedSearchClubDto
import com.softserve.teachua.data.dto.SearchClubDto
import com.softserve.teachua.databinding.FragmentClubsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ClubsFragment : Fragment() {

    private var _binding: FragmentClubsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val clubsViewModel: ClubsViewModel by viewModels()
    private lateinit var clubsAdapter: ClubsAdapter
    private lateinit var progressDialog: ProgressDialog

    var cities = arrayListOf<String>()
    var districts = arrayListOf<String>()
    var stations = arrayListOf<String>()

    private var districtByCity = ""

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

        initClubs()

        updateViewModel()
        updateToolbar()



        lifecycleScope.launch {
            //differentThread()
            getCt()



        }
        return root
    }

    private fun createViewModel() {

        clubsViewModel.loadCities()

    }

    private fun updateToolbar() {
        val toolbar = binding.toolbar
        (requireActivity() as MainActivity).toolbar.visibility = View.GONE
        (requireActivity() as MainActivity).setToobar(toolbar)
    }

    private fun updateViewModel() {

        clubsViewModel.viewModelScope.launch {

            clubsViewModel.clubs.collect { pagingData -> clubsAdapter.submitData(pagingData) }
        }
    }

    private fun updateDistricts() {

        lifecycleScope.launch {
            districts.clear()
            districts.add("Виберіть район")
            //getDistrictByCityName(districtByCity)
            clubsViewModel.loadStations(districtByCity)
        }
    }

    private fun updateStations() {

        lifecycleScope.launch {
            stations.clear()
            stations.add("Виберіть станцію")
            clubsViewModel.loadDistricts(districtByCity)
        }
    }


    private fun initClubs() {

        binding.rcv.layoutManager = layoutManager

        clubsAdapter = ClubsAdapter(requireContext())

        binding.rcv.adapter = clubsAdapter.withLoadStateHeaderAndFooter(
            header = ClubsLoadStateAdapter { clubsAdapter.retry() },
            footer = ClubsLoadStateAdapter { clubsAdapter.retry() })
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


    private fun citySpinnerPicker() {

        println("cities" + cities.toString())
        val citySpinnerAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(),
            R.layout.item_dropdown, cities)

        citySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCities.adapter = citySpinnerAdapter

        binding.spinnerCities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, id: Long) {
                println(parent!!.getItemAtPosition(pos))
                //iewModelStore.clear()

                clubsViewModel.searchClubDto.value?.cityName =
                    parent.getItemAtPosition(pos).toString()
                Toast.makeText(requireContext(),
                    parent.getItemAtPosition(pos).toString(),
                    Toast.LENGTH_SHORT).show()
                clubsViewModel.advancedSearchClubDto.value?.isAdvanced = false
                //createViewModel()
                //addDataToVM()
                clubsAdapter.refresh()


            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }

    }

    private suspend fun getCt() {

        println(clubsViewModel.cities.value.data)

        clubsViewModel.cities.collectLatest { data ->
            println(data.data.toString())
            when(data.status){

                Resource.Status.SUCCESS -> {
                    for (city in data.data!!) {
                        cities.add(city.cityName)
                    }
                    citySpinnerPicker()
                    dismissProgressDialog()
                }

                Resource.Status.LOADING -> showLoadingProgressDialog()

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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModelStore.clear()
    }
}