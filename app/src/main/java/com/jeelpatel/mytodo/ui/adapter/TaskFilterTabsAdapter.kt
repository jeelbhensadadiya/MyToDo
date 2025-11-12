package com.jeelpatel.mytodo.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jeelpatel.mytodo.ui.view.fragments.filters.AllTasksFragment
import com.jeelpatel.mytodo.ui.view.fragments.filters.CompletedTasksFragment
import com.jeelpatel.mytodo.ui.view.fragments.filters.OverdueTasksFragment
import com.jeelpatel.mytodo.ui.view.fragments.filters.PendingTasksFragment

class TaskFilterTabsAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllTasksFragment()
            1 -> OverdueTasksFragment()
            2 -> CompletedTasksFragment()
            3 -> PendingTasksFragment()
            else -> AllTasksFragment()
        }
    }

    override fun getItemCount(): Int {
        return 4
    }
}