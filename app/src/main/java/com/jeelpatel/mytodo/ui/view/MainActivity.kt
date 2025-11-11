package com.jeelpatel.mytodo.ui.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.ActivityMainBinding
import com.jeelpatel.mytodo.ui.viewModel.userViewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var navController: NavController
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        userViewModel.checkLoginStatus()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.signUpFragment -> {
                    binding.appBar.visibility = View.GONE
                    binding.bottomNavigation.visibility = View.GONE
                }

                R.id.recycleBinFragment -> binding.materialToolBar.title = "Recycle Bin"
                R.id.createTaskFragment -> binding.materialToolBar.title = "Create Task"
                R.id.taskViewFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                    binding.materialToolBar.title = ""
                }

                R.id.remoteTodoFragment -> binding.materialToolBar.title = "Remote Todo"
                R.id.profileFragment -> binding.materialToolBar.title = "My Profile"

                else -> {
                    binding.appBar.visibility = View.VISIBLE
                    binding.bottomNavigation.visibility = View.VISIBLE
                    binding.materialToolBar.title = "My Todo"
                }
            }
        }

        binding.bottomNavigation.setupWithNavController(navController)

        binding.materialToolBar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.logOutBtn) {
                userViewModel.logoutUser()
                userViewModel.checkLoginStatus()
                true
            } else {
                false
            }
        }

        dataCollector()
    }

    private fun dataCollector() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    userViewModel.isUserLoggedIn.collect { loggedIn ->

                        Log.d("AUTH_STATE", "loggedIn = $loggedIn")

                        if (!loggedIn) {
                            val navController = findNavController(R.id.nav_host_fragment)
                            navController.navigate(
                                R.id.signUpFragment,
                                null,
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
}