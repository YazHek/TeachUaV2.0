package com.softserve.teachua.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.softserve.teachua.R
import com.softserve.teachua.app.tools.Resource
import com.softserve.teachua.data.dto.UserLoginDto
import com.softserve.teachua.databinding.FragmentLoginBinding
import com.softserve.teachua.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()
    private var userLoginDto = UserLoginDto("", "")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view: View = binding.root

        binding.loginButton.setOnClickListener(this)

        updateView()

        return view
    }

    private fun loadData(userLoginDto: UserLoginDto) {

        loginViewModel.viewModelScope.launch {
            loginViewModel.load(userLoginDto)
        }
    }

    private fun loadUser(token: String, id: Int) {

        loginViewModel.viewModelScope.launch {
            loginViewModel.loadUser(token, id)
        }
    }

    private fun updateView() {
        loginViewModel.viewModelScope.launch {
            loginViewModel.loggedDto.collectLatest { loggedUser ->
                when (loggedUser.status) {

                    Resource.Status.SUCCESS -> {
                        loadUser("Bearer " + loggedUser.data?.accessToken!!, loggedUser.data.id)
                        Toast.makeText(requireContext(),
                            "Successfully loged user with id " + loggedUser.data.id,
                            Toast.LENGTH_SHORT).show()

                        loginViewModel.user.collectLatest { user ->

                            when (user.status) {

                                Resource.Status.SUCCESS -> {
                                    println("userr " + user)
                                    (activity as MainActivity).changeLoginNavSection()
                                    view?.let {
                                        Navigation.findNavController(view = it).navigateUp()
                                    }
                                }
                            }

                        }

                    }
                    else -> {}
                }

            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

            R.id.login_button -> {
                userLoginDto.email = binding.enterEmail.text?.trim().toString()
                userLoginDto.password = binding.enterPassword.text?.trim().toString()
                loadData(userLoginDto)

            }
        }
    }

}