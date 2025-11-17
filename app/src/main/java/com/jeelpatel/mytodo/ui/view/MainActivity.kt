package com.jeelpatel.mytodo.ui.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }


        // check user login status
        userViewModel.checkLoginStatus()


        // setup nav host
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController


        // setup back button in app toolbar
        binding.materialToolBar.setNavigationOnClickListener {
            findNavController(R.id.nav_host_fragment).navigateUp()
        }


        // setup tool bar action according to active fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.signUpFragment -> {
                    binding.appBar.visibility = View.GONE
                    binding.bottomNavigation.visibility = View.GONE
                }

                R.id.mainFragment -> {
                    binding.appBar.visibility = View.VISIBLE
                    binding.bottomNavigation.visibility = View.VISIBLE

                    binding.materialToolBar.navigationIcon =
                        AppCompatResources.getDrawable(this, R.drawable.to_do_24)
                    binding.materialToolBar.title = getString(R.string.app_name)
                }

                R.id.recycleBinFragment -> {
                    showToolbarWithBackIcon("Recycle Bin")
                }

                R.id.cameraFragment -> {
                    showToolbarWithBackIcon("Take Photo")
                    binding.bottomNavigation.visibility = View.GONE
                }

                R.id.mediaListFragment -> {
                    showToolbarWithBackIcon("Media List")
                }

                R.id.videoPlayerFragment -> {
                    showToolbarWithBackIcon("Media Player")
                }

                R.id.createTaskFragment -> {
                    showToolbarWithBackIcon("Create Task")
                }

                R.id.taskViewFragment -> {
                    showToolbarWithBackIcon("")
                    binding.bottomNavigation.visibility = View.GONE
                }

                R.id.remoteTodoFragment -> {
                    showToolbarWithBackIcon("Remote Todo")
                }

                R.id.profileFragment -> {
                    showToolbarWithBackIcon("My Profile")
                }

                else -> {
                    showToolbarWithBackIcon("My Todo")
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


    private fun showToolbarWithBackIcon(title: String) {
        binding.appBar.visibility = View.VISIBLE
        binding.bottomNavigation.visibility = View.VISIBLE
        binding.materialToolBar.navigationIcon =
            AppCompatResources.getDrawable(this, R.drawable.arrow_small_left_24)
        binding.materialToolBar.title = title
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


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}