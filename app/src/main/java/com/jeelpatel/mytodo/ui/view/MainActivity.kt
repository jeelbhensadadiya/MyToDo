package com.jeelpatel.mytodo.ui.view

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.ActivityMainBinding
import com.jeelpatel.mytodo.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var navController: NavController
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // for get current User ID and Status
        sessionManager = SessionManager(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.signUpFragment -> binding.appBar.visibility = View.GONE
                R.id.recycleBinFragment -> binding.materialToolBar.title = "Recycle Bin"
                R.id.createTaskFragment -> binding.materialToolBar.title = "Create Task"
                R.id.taskViewFragment -> binding.materialToolBar.title = ""
                R.id.remoteTodoFragment -> binding.materialToolBar.title = "Remote Todo"
                R.id.profileFragment -> binding.materialToolBar.title = "My Profile"

                else -> {
                    binding.appBar.visibility = View.VISIBLE
                    binding.materialToolBar.title = "My Todo"
                }
            }
        }

        binding.materialToolBar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.logOutBtn) {
                sessionManager.clearSession()
                val navController = findNavController(R.id.nav_host_fragment)
                navController.navigate(R.id.signUpFragment)
                true
            } else {
                false
            }
        }
    }
}