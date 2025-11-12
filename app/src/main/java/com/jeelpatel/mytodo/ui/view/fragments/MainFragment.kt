package com.jeelpatel.mytodo.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.jeelpatel.mytodo.R
import com.jeelpatel.mytodo.databinding.FragmentMainBinding
import com.jeelpatel.mytodo.ui.adapter.TaskFilterTabsAdapter


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val taskFilterTabsAdapter = TaskFilterTabsAdapter(this)
        binding.taskViewPager.adapter = taskFilterTabsAdapter


        TabLayoutMediator(binding.tabLayout, binding.taskViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.all)
                1 -> getString(R.string.over_due)
                2 -> getString(R.string.completed)
                3 -> getString(R.string.pending)
                else -> null
            }
        }.attach()


        binding.onlineTodosBtn.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToRemoteTodoFragment())
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}