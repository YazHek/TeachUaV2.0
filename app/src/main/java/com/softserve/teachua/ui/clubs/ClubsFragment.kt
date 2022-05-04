package com.softserve.teachua.ui.clubs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softserve.teachua.MainActivity
import com.softserve.teachua.adapters.ClubsAdapter
import com.softserve.teachua.adapters.ClubsLoadStateAdapter
import com.softserve.teachua.databinding.FragmentClubsBinding
import com.softserve.teachua.dto.AdvancedSearchClubDto
import com.softserve.teachua.dto.SearchClubDto
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ClubsFragment : Fragment() {

    private var _binding: FragmentClubsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var clubsViewModel: ClubsViewModel
    private lateinit var clubsAdapter: ClubsAdapter
    private var layoutManager = LinearLayoutManager(activity?.baseContext, RecyclerView.VERTICAL, false)
    private var searchClubsDto = SearchClubDto("", "", false, "", 0)
    private var advancedSearchClubDto = AdvancedSearchClubDto("",
        null,
        "",
        "",
        "",
        "name,asc",
        0,
        emptyList(),
        isOnline = false,
        isCenter = false,
        isAdvanced = false)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        createViewModel()

        _binding = FragmentClubsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initClubs()
        updateViewModel()
        updateToolbar()
        return root
    }

    private fun createViewModel() {
        val factory = ClubsViewModelFactory(searchClubsDto, advancedSearchClubDto)

        clubsViewModel =
            ViewModelProvider(this, factory)[ClubsViewModel::class.java]
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

    private fun initClubs() {

        binding.rcv.layoutManager = layoutManager

        clubsAdapter = ClubsAdapter(requireContext())

        binding.rcv.adapter = clubsAdapter.withLoadStateHeaderAndFooter(
            header = ClubsLoadStateAdapter { clubsAdapter.retry() },
            footer = ClubsLoadStateAdapter { clubsAdapter.retry() })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModelStore.clear()
    }
}