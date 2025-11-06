package com.jeelpatel.mytodo.ui.view.fragments.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.FragmentSignUpBinding
import com.jeelpatel.mytodo.domain.model.UserModel
import com.jeelpatel.mytodo.ui.viewModel.UserViewModel
import com.jeelpatel.mytodo.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var sessionManager: SessionManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        sessionManager = SessionManager(requireContext())

        // Check User Login Status
        if (sessionManager.isLoggedIn()) {
            findNavController().navigate(R.id.action_signUpFragment_to_mainFragment)
            return
        }

        dataCollectors()

        binding.signUpBtn.setOnClickListener {
            val userName = binding.userNameEt.text.toString()
            val userEmail = binding.userEmailEt.text.toString()
            val userPassword = binding.userPasswordEt.text.toString()
            val userRePassword = binding.userRePasswordEt.text.toString()

            if (userName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || userRePassword.isEmpty()) {
                toast("Fill all Fields !!!")
            } else if (userPassword != userRePassword) {
                toast("Passwords mismatch !!!")
            } else {
                val user = UserModel(
                    uName = userName,
                    uEmail = userEmail.lowercase().trim(),
                    uPassword = userPassword,
                )
                userViewModel.registerUser(user)
            }
        }


        binding.alreadyUserBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
    }

    private fun dataCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    userViewModel.message.collect {
                        toast(it)
                    }
                }

                launch {
                    userViewModel.isRegistered.collect { registered ->
                        if (registered) {
                            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                        }
                    }
                }
            }
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}