package com.jeelpatel.mytodo.ui.view.authentication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.ActivityLoginBinding
import com.jeelpatel.mytodo.utils.SessionManager
import com.jeelpatel.mytodo.ui.view.MainActivity
import com.jeelpatel.mytodo.ui.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity() : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val userViewModel: UserViewModel by viewModels()

    lateinit var sessionManager: SessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sessionManager = SessionManager(this)

        observeData()

        binding.loginBtn.setOnClickListener {
            val userEmail = binding.userEmailEt.text.toString()
            val userPassword = binding.userPasswordEt.text.toString()

            if (userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(this, "Enter email or Password", Toast.LENGTH_LONG).show()
            } else {
                userViewModel.loginUser(userEmail.lowercase().trim(), userPassword)
            }
        }

        binding.notUserBtn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
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
                    userViewModel.user.collect { user ->
                        user?.let {
                            sessionManager.saveUserSession(it.uId)
                        }
                    }
                }

                launch {
                    userViewModel.isRegistered.collect { registered ->
                        if (registered) {
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                }

                launch {
                    userViewModel.message.collect { msg ->
                        Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}