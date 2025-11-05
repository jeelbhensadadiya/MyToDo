package com.jeelpatel.mytodo.ui.view

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
import com.jeelpatel.mytodo.databinding.ActivityRecyclerBinBinding
import com.jeelpatel.mytodo.ui.adapter.RecycleBinTaskAdapter
import com.jeelpatel.mytodo.ui.viewModel.TaskViewModel
import com.jeelpatel.mytodo.utils.SessionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecyclerBinActivity : AppCompatActivity() {

    private val taskViewModel: TaskViewModel by viewModels()
    private val binding: ActivityRecyclerBinBinding by lazy {
        ActivityRecyclerBinBinding.inflate(layoutInflater)
    }

    lateinit var recyclerBinAdapter: RecycleBinTaskAdapter

    lateinit var sessionManager: SessionManager
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

        binding.materialToolBar.setNavigationOnClickListener {
            finish()
        }

        taskViewModel.getAllDeletedTask(currentUserID)

        recyclerBinAdapter = RecycleBinTaskAdapter(this, onRestore = { taskId ->
            taskViewModel.restoreTask(taskId)
        })
        binding.taskRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.taskRecyclerView.adapter = recyclerBinAdapter

        collector()
    }

    private fun collector() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    taskViewModel.task.collect { taskList ->
                        recyclerBinAdapter.submitList(taskList)
                    }
                }

                launch {
                    taskViewModel.message.collect { msg ->
                        Toast.makeText(this@RecyclerBinActivity, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}