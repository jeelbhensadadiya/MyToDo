package com.jeelpatel.mytodo.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeelpatel.mytodo.databinding.FragmentRemoteTodoBinding
import com.jeelpatel.mytodo.ui.adapter.TodoAdapter
import com.jeelpatel.mytodo.ui.viewModel.remoteViewModel.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RemoteTodoFragment : Fragment() {

    private var _binding: FragmentRemoteTodoBinding? = null
    private val binding get() = _binding!!
    private val todoViewModel: TodoViewModel by viewModels()
    lateinit var todoAdapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRemoteTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter
        todoAdapter = TodoAdapter(requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = todoAdapter

        // Default
        todoViewModel.getTodos()

        // collect flow data
        dataCollectors()
    }

    private fun dataCollectors() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    todoViewModel.todos.collectLatest {
                        todoAdapter.submitList(it)
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}