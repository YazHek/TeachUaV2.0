package com.softserve.teachua.ui.challenge

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.softserve.teachua.R

class ChallengeFragment : Fragment() {

    companion object {
        fun newInstance() = ChallengeFragment()
    }

    private val viewModel : ChallengeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.challenge_fragment, container, false)
    }



}