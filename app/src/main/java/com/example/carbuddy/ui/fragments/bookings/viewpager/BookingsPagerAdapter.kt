package com.example.carbuddy.ui.fragments.bookings.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.carbuddy.ui.fragments.bookings.history.HistoryFragment
import com.example.carbuddy.ui.fragments.bookings.ongoing.OngoingFragment
import com.example.carbuddy.ui.fragments.bookings.pending.PendingFragment

class BookingsPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PendingFragment()
            1 -> OngoingFragment()
            2 -> HistoryFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }
}
