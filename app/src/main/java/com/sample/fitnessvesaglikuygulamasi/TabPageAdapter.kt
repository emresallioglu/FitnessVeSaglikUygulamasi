package com.sample.fitnessvesaglikuygulamasi

import ActivityFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class TabPageAdapter(activity: FragmentActivity, private val tabCount: Int): FragmentStateAdapter(activity)
{
    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {
        return when (position)
        {
            0 -> HomeFragment()
            1 -> ActivityFragment()
            2 -> GoalsFragment()
            3 -> NutritionFragment()
            4 -> SleepFragment()
            5 -> ProfileFragment()
            else -> HomeFragment()

        }
    }

}