package com.jeelpatel.mytodo.ui.view.fragments.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.FragmentSignUpBinding
import com.jeelpatel.mytodo.ui.viewModel.userViewModel.UserViewModel
import com.jeelpatel.mytodo.utils.UiHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Check User Login Status
        userViewModel.checkLoginStatus()


        // create user
        binding.signUpBtn.setOnClickListener {
            userViewModel.registerUser(
                userName = binding.userNameEt.text.toString(),
                userEmail = binding.userEmailEt.text.toString(),
                userPassword = binding.userPasswordEt.text.toString(),
                userRePassword = binding.userRePasswordEt.text.toString()
            )
        }


        // navigate to login fragment if user already have account
        binding.alreadyUserBtn.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
        }


        // collects flow data
        dataCollectors()
    }

    private fun dataCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    userViewModel.message.collectLatest {
                        // show errors
                        UiHelper.showToast(requireContext(), it)
                    }
                }


                launch {
                    userViewModel.isUserCreated.collectLatest { created ->
                        // navigate to login after successful signup
                        if (created) {
                            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment())
                        }
                    }
                }


                launch {
                    userViewModel.isUserLoggedIn.collectLatest { loggedIn ->
                        // navigate to main if already loggedIn
                        if (loggedIn) {
                            findNavController().navigate(
                                SignUpFragmentDirections.actionSignUpFragmentToMainFragment(),
                                androidx.navigation.NavOptions.Builder()
                                    .setPopUpTo(R.id.signUpFragment, true)
                                    .build()
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // clear binding
    }
}