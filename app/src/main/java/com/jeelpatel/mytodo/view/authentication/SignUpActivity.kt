package com.jeelpatel.mytodo.view.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jeelpatel.mytodo.databinding.ActivitySignUpBinding
import com.jeelpatel.mytodo.model.local.entity.UserEntity
import com.jeelpatel.mytodo.utils.SessionManager
import com.jeelpatel.mytodo.view.MainActivity
import com.jeelpatel.mytodo.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private val userViewModel: UserViewModel by viewModels()

    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        observeData()

        binding.signUpBtn.setOnClickListener {
            val userName = binding.userNameEt.text.toString()
            val userEmail = binding.userEmailEt.text.toString()
            val userPassword = binding.userPasswordEt.text.toString()
            val userRePassword = binding.userRePasswordEt.text.toString()

            if (userName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || userRePassword.isEmpty()) {
                Toast.makeText(this, "Fill all Fields !!!", Toast.LENGTH_LONG).show()
            } else {
                if (userPassword != userRePassword) {
                    Toast.makeText(this, "Passwords mismatch !!!", Toast.LENGTH_LONG).show()
                } else {
                    val user = UserEntity(
                        uName = userName,
                        uEmail = userEmail.lowercase().trim(),
                        uPassword = userPassword
                    )
                    userViewModel.registerUser(user)
                }
            }
        }

        binding.alreadyUserBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if (sessionManager.isLoggedIn()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun observeData() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    userViewModel.message.collect { msg ->
                        Toast.makeText(this@SignUpActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }

                launch {

                    userViewModel.isRegistered.collect { registered ->
                        if (registered) {
                            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }


    }
}