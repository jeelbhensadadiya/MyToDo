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
        setContentView(binding.root)


        // check user login status
        userViewModel.checkLoginStatus()


        // setup nav host
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController


        // setup tool bar action according to active fragment
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


        // setup bottom app bar with nav controller
        binding.bottomNavigation.setupWithNavController(navController)


        // tool bar logout button
        binding.materialToolBar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.logOutBtn) {
                userViewModel.logoutUser()
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

                        Log.d("AUTH", "isUserLoggedIn = $loggedIn")

                        val currentGraphId = navController.graph.id
                        val targetGraphId =
                            if (loggedIn) R.navigation.main_graph else R.navigation.auth_graph

                        if (currentGraphId != targetGraphId) {
                            navController.setGraph(targetGraphId)
                        }
                    }
                }
            }
        }
    }
}