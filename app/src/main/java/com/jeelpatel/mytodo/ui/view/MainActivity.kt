package com.jeelpatel.mytodo.ui.view

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.ActivityMainBinding
import com.jeelpatel.mytodo.utils.SessionManager
import com.jeelpatel.mytodo.ui.adapter.TaskAdapter
import com.jeelpatel.mytodo.ui.view.authentication.LoginActivity
import com.jeelpatel.mytodo.ui.view.authentication.SignUpActivity
import com.jeelpatel.mytodo.ui.viewModel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val taskViewModel: TaskViewModel by viewModels()
    lateinit var sessionManager: SessionManager
    lateinit var taskAdapter: TaskAdapter
    var currentUserID = 0

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
        currentUserID = sessionManager.getUserId()


        // Adapter
        taskAdapter = TaskAdapter(this) { taskId, isCompleted ->
            taskViewModel.updateTaskStatus(taskId, isCompleted)
        }
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.taskRecyclerView.adapter = taskAdapter

        binding.createNewTaskBtn.setOnClickListener {
            startActivity(Intent(this, AddNewTaskActivity::class.java))
        }

        binding.onlineTodosBtn.setOnClickListener {
            startActivity(Intent(this, TodoActivity::class.java))
        }

        binding.materialToolBar.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.logOutBtn) {
                sessionManager.clearSession()
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
                true
            } else {
                false
            }
        }

        observeData()

        taskViewModel.getAllTask(currentUserID)

    }


    private fun observeData() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    taskViewModel.task.collect { task ->
                        taskAdapter.submitList(task)
                    }
                }

                launch {
                    taskViewModel.message.collect { msg ->
                        Toast.makeText(this@MainActivity, msg, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        taskViewModel.getAllTask(currentUserID)
    }

    override fun onStart() {
        super.onStart()
        if (!sessionManager.isLoggedIn()) {
            if (currentUserID == 0) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
    }
}