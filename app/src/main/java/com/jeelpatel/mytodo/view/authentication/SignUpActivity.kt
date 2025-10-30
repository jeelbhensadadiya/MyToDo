package com.jeelpatel.mytodo.view.authentication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.ActivitySignUpBinding
import com.jeelpatel.mytodo.model.UserRepository
import com.jeelpatel.mytodo.model.local.dao.UserDao
import com.jeelpatel.mytodo.model.local.database.DatabaseBuilder
import com.jeelpatel.mytodo.model.local.database.UserDatabase
import com.jeelpatel.mytodo.model.local.entity.UserEntity
import com.jeelpatel.mytodo.utils.SessionManager
import com.jeelpatel.mytodo.view.MainActivity
import com.jeelpatel.mytodo.viewModel.UserViewModel
import com.jeelpatel.mytodo.viewModel.UserViewModelFactory
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
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