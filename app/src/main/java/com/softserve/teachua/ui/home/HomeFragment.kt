package com.softserve.teachua.ui.home

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.softserve.teachua.MainActivity
import com.softserve.teachua.adapters.CategoriesAdapter
import com.softserve.teachua.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.app_bar_main.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CategoriesAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var bans: ImageSlider
    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        createHomeViewModel()

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val toolbar = binding.root.tool.toolbar


        bans = binding.imageSlider


        initCategories()
        updateViewModel()
        updateToolbar()
        return root
    }

    private fun createHomeViewModel() {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.loadBanners()
        homeViewModel.loadCategories()
    }

    private fun updateViewModel() {

        homeViewModel.viewModelScope.launch {

            homeViewModel.banners.collect { initBanners() }
        }



        homeViewModel.viewModelScope.launch {

            Glide.with(requireContext())
                .load(homeViewModel.staticMainImg.value)
                .into(binding.challengeImg)


            homeViewModel.categories.collect { categories ->
                adapter.submitList(categories)
                if (homeViewModel.categories.value.isNotEmpty())
                    dismissProgressDialog()
            }


        }
//
    }

    private fun initCategories() {

        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.categoriesList.layoutManager = layoutManager
        adapter = CategoriesAdapter(requireContext())
        binding.categoriesList.adapter = adapter
        if (homeViewModel.categories.value.isEmpty()) {

            showLoadingListDialog()
        }
    }

    private fun showLoadingListDialog() {
        progressDialog = ProgressDialog(requireContext())

        progressDialog.setTitle("Loading List Of Clubs")
        progressDialog.setMessage("List of clubs is loading, please wait")
        progressDialog.show()


    }

    private fun dismissProgressDialog() {

        progressDialog.dismiss()
    }


    private fun initBanners() {


        val bansList = ArrayList<SlideModel>()

        for (ban in homeViewModel.banners.value)
            bansList.add(SlideModel(homeViewModel.baseUrl.value + ban.bannerPicture,
                ban.bannerTitle + "\n\n" + ban.bannerSubtitle))
        bans.setImageList(bansList, ScaleTypes.CENTER_CROP)
        bans.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                println("pos " + position)
            }

        })
    }

    private fun updateToolbar() {
        lifecycleScope.launch {
            if ((requireActivity() as MainActivity).hasWindowFocus()) {

                if ((requireActivity() as MainActivity).toolbar.visibility == View.GONE)
                    (requireActivity() as MainActivity).toolbar.visibility = View.VISIBLE
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModelStore.clear()
    }
}