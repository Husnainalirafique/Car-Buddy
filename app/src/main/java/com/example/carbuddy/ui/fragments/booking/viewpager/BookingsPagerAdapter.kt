package com.example.carbuddy.ui.fragments.booking.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.carbuddy.ui.fragments.booking.history.HistoryFragment
import com.example.carbuddy.ui.fragments.booking.pending.PendingFragment

class BookingsPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PendingFragment()
            1 -> HistoryFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }
}
