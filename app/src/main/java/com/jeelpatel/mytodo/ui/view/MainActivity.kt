package com.jeelpatel.mytodo.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.ActivityMainBinding
import com.jeelpatel.mytodo.ui.adapter.TaskAdapter
import com.jeelpatel.mytodo.ui.view.authentication.LoginActivity
import com.jeelpatel.mytodo.ui.view.authentication.SignUpActivity
import com.jeelpatel.mytodo.ui.viewModel.TaskViewModel
import com.jeelpatel.mytodo.utils.SessionManager
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



        taskAdapter = TaskAdapter(
            this,
            onStatusChange = { taskID, isCompleted ->
                taskViewModel.updateTaskStatus(taskID, isCompleted)
            },
            onDeleted = { taskId ->
                taskViewModel.deleteTask(taskId)
            }
        )
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.taskRecyclerView.adapter = taskAdapter

        // default Get all task
        taskViewModel.getAllTask(currentUserID)

        binding.createNewTaskBtn.setOnClickListener {
            startActivity(Intent(this, AddNewTaskActivity::class.java))
        }

        binding.onlineTodosBtn.setOnClickListener {
            startActivity(Intent(this, TodoActivity::class.java))
        }

        binding.recyclerBinBtn.setOnClickListener {
            startActivity(Intent(this, RecyclerBinActivity::class.java))
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

        binding.buttonGroup.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener

            binding.buttonGroup.children.forEach { view ->
                view.isSelected = view.id == checkedId
            }

            when (checkedId) {
                R.id.allTaskFilterBtn -> {
                    taskViewModel.getAllTask(currentUserID)
                }

                R.id.overDueTaskFilterBtn -> {
                    taskViewModel.overDueTask(currentUserID)
                }

                R.id.completedTaskFilterBtn -> {
                    taskViewModel.completedTask(currentUserID)
                }

                R.id.pendingTaskFilterBtn -> {
                    taskViewModel.pendingTask(currentUserID)
                }
            }
        }

        observeData()

    }


    private fun observeData() {

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    taskViewModel.task.collect { task ->
                        taskAdapter.submitList(task)
                        if (task.isEmpty()) {
                            binding.emptyTextView.text = "No tasks"
                            binding.emptyTextView.visibility = View.VISIBLE
                        } else {
                            binding.emptyTextView.visibility = View.GONE
                        }
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