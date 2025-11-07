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
import com.jeelpatel.mytodo.databinding.FragmentLoginBinding
import com.jeelpatel.mytodo.ui.viewModel.userViewModel.UserViewModel
import com.jeelpatel.mytodo.utils.SessionManager
import com.jeelpatel.mytodo.utils.UiHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var sessionManager: SessionManager
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // login user
        binding.loginBtn.setOnClickListener {
            userViewModel.loginUser(
                userEmail = binding.userEmailEt.text.toString(),
                userPassword = binding.userPasswordEt.text.toString()
            )
        }


        // navigate to signup if not registered
        binding.notUserBtn.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
        }


        // collects flow data
        dataCollectors()
    }

    private fun dataCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    userViewModel.message.collect {
                        // show errors
                        UiHelper.showToast(requireContext(), it)
                    }
                }

                launch {
                    userViewModel.isUserLoggedIn.collect { loggedIn ->

                        // navigate to main fragment if login success
                        if (loggedIn) {
                            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
                        }
                    }
                }

                launch {
                    userViewModel.user.collect { user ->
                        // store logged in user id to session manager
                        sessionManager.saveUserSession(user?.uId ?: 0)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // clean binding
    }
}