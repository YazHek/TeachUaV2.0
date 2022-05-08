package com.softserve.teachua.ui.club

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.softserve.teachua.MainActivity
import com.softserve.teachua.databinding.FragmentClubBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ClubFragment : Fragment() {

    private var _binding: FragmentClubBinding? = null

    private val binding get() = _binding!!

    private val clubViewModel: ClubViewModel by viewModels()

    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        _binding = FragmentClubBinding.inflate(inflater, container, false)
        val root: View = binding.root

        toolbar = binding.tb.toolbar
        getCurrentClubInfo()



        return root

    }

    private fun getCurrentClubInfo() {


        binding.clubNameText.text = arguments?.getString("clubName")
        binding.clubDescriptionText.text = arguments?.getString("clubDescription")


    }


    private fun updateToolbar() {
        lifecycleScope.launch {
            if ((requireActivity() as MainActivity).hasWindowFocus()) {
                if ((requireActivity() as MainActivity).toolbar.visibility == View.GONE) {
                    toolbar.visibility = View.VISIBLE
                    (requireActivity() as MainActivity).setToobar(toolbar)
                }

            }

        }
    }

    override fun onResume() {
        super.onResume()
        updateToolbar()
    }

    override fun onStop() {
        super.onStop()
        binding.clubNameText.visibility = View.GONE
        binding.clubDescriptionText.visibility = View.GONE
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        viewModelStore.clear()
    }


}