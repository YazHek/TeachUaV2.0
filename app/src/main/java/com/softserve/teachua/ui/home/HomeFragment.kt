package com.softserve.teachua.ui.home

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import com.softserve.teachua.app.adapters.CategoriesAdapter
import com.softserve.teachua.app.baseImageUrl
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CategoriesAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var bans: ImageSlider
    private lateinit var progressDialog: ProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        progressDialog = ProgressDialog(requireContext())
        adapter = CategoriesAdapter(requireContext())
        bans = binding.imageSlider

        homeViewModel.loadData()

        Glide.with(requireContext())
            .load(homeViewModel.staticMainImg)
            .into(binding.challengeImg)


        initCategories()
        updateViewModel()
        updateToolbar()
        return root
    }


    private fun updateViewModel() {

        homeViewModel.viewModelScope.launch {
            homeViewModel.banners.collectLatest {
                if (homeViewModel.banners.value.status == Resource.Status.SUCCESS){
                    initBanners()
                }
            }
        }



        homeViewModel.viewModelScope.launch {
            homeViewModel.categories.collectLatest { categories ->
                if (homeViewModel.categories.value.status == Resource.Status.SUCCESS){
                    adapter.submitList(categories.data)
                    dismissProgressDialog()
                } else {
                    adapter.submitList(emptyList())
                }
            }
        }

    }

    private fun initCategories() {

        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        binding.categoriesList.layoutManager = layoutManager
        binding.categoriesList.adapter = adapter
        if (homeViewModel.categories.value.status == Resource.Status.LOADING)
            showLoadingListDialog()

    }

    private fun showLoadingListDialog() {

        progressDialog.setTitle("Loading List Of Clubs")
        progressDialog.setMessage("List of clubs is loading, please wait")
        progressDialog.show()


    }

    private fun dismissProgressDialog() {

        progressDialog.dismiss()
    }

    private fun initBanners() {


        val bansList = ArrayList<SlideModel>()


            for (ban in homeViewModel.banners.value.data!!)
                bansList.add(SlideModel(
                    baseImageUrl + ban.bannerPicture,
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


}