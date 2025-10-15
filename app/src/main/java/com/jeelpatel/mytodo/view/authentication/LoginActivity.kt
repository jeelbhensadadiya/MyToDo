package com.jeelpatel.mytodo.view.authentication

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.ActivityLoginBinding
import com.jeelpatel.mytodo.model.UserRepository
import com.jeelpatel.mytodo.model.local.database.DatabaseBuilder
import com.jeelpatel.mytodo.utils.SessionManager
import com.jeelpatel.mytodo.view.MainActivity
import com.jeelpatel.mytodo.viewModel.UserViewModel
import com.jeelpatel.mytodo.viewModel.UserViewModelFactory

class LoginActivity() : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    lateinit var sessionManager: SessionManager

    lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Setup Room + MVVM
        val db = DatabaseBuilder.getInstance(this)
        val userDao = db.userDao()
        val userRepository = UserRepository(userDao)
        val userFactory = UserViewModelFactory(userRepository)
        userViewModel = ViewModelProvider(this, userFactory)[UserViewModel::class.java]

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
        userViewModel.user.observe(this) { user ->
            sessionManager.saveUserSession(user.uId)
        }

        userViewModel.isRegistered.observe(this) { registered ->
            if (registered == true) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        userViewModel.message.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }
}