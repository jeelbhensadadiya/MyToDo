package com.jeelpatel.mytodo.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.ActivityMainBinding
import com.jeelpatel.mytodo.model.TaskRepository
import com.jeelpatel.mytodo.model.local.database.DatabaseBuilder
import com.jeelpatel.mytodo.view.authentication.LoginActivity
import com.jeelpatel.mytodo.view.authentication.SignUpActivity
import com.jeelpatel.mytodo.viewModel.TaskViewModel
import com.jeelpatel.mytodo.viewModel.TaskViewModelFactory

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var taskViewModel: TaskViewModel
    lateinit var sharedPref: SharedPreferences
    var currentUserID: Int = 0

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
        sharedPref = getSharedPreferences("currentUserId", MODE_PRIVATE)
        currentUserID = sharedPref.getInt("uId", 0)


        // Setup Room + MVVM
        val db = DatabaseBuilder.getInstance(this)
        val taskDao = db.taskDao()
        val taskRepository = TaskRepository(taskDao)
        val taskFactory = TaskViewModelFactory(taskRepository)
        taskViewModel = ViewModelProvider(this, taskFactory)[TaskViewModel::class.java]

        uiSetup()
        observeData()

        taskViewModel.getAllTask(currentUserID)

    }

    override fun onResume() {
        super.onResume()
        taskViewModel.getAllTask(currentUserID)
    }

    private fun observeData() {
        taskViewModel.task.observe(this) { task ->
            binding.taskTv.text = task.joinToString {
                """
                    ${it.taskId}
                    ${it.title}
                    ${it.description}
                    ${it.isCompleted}
                    ${it.priority}
                    ${it.dueDate}
                    ${it.createdAt}
                    ${it.userOwnerId}
                    
                    
                """.trimIndent()
            }
        }

        taskViewModel.message.observe(this) { msg ->
            binding.taskTv.text = msg
        }
    }

    private fun uiSetup() {

        binding.createNewTaskBtn.setOnClickListener {
            startActivity(Intent(this, AddNewTaskActivity::class.java))
        }

        binding.materialToolBar.setOnMenuItemClickListener() { menuItem ->
            if (menuItem.itemId == R.id.logOutBtn) {
                sharedPref.edit().clear().apply()
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
                true
            } else {
                false
            }

        }
    }

    override fun onStart() {
        super.onStart()
        if (!sharedPref.getBoolean("isLoggedIn", false)) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}