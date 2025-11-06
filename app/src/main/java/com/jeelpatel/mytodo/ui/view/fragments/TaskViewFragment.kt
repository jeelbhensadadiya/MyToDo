package com.jeelpatel.mytodo.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.jeelpatel.mytodo.databinding.FragmentTaskViewBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskViewFragment : Fragment() {

    private var _binding: FragmentTaskViewBinding? = null
    private val binding get() = _binding!!
    private val args: TaskViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val formattedDate = SimpleDateFormat("dd-MMM-yyyy, hh:mm a", Locale.getDefault())
            .format(Date(args.dueDate))

        with(binding) {
            taskTitleTv.text = args.title
            taskDescriptionTv.text = args.desc
            taskStatusChip.text = if (args.isCompleted) "Completed" else "In Progress"
            taskPriorityChip.text = "Priority: ${args.priority}"
            taskDueDateTv.text = "Due : $formattedDate"
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}