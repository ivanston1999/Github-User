package com.submission.githubfinal.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.submission.githubfinal.ui.follow.FollowersFragment

class PagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowersFragment()
        val tab: String = when (position) {
            0 -> FollowersFragment.FOLLOWER
            1 -> FollowersFragment.FOLLOWING
            else -> ""
        }
        fragment.arguments = Bundle().apply {
            putString(FollowersFragment.TAB, tab)
        }
        return fragment
    }
}