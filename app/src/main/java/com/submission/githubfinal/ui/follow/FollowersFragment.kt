package com.submission.githubfinal.ui.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.submission.githubfinal.databinding.FollowersFragmentBinding
import com.submission.githubfinal.ui.detail.UserDetailActivity

class FollowersFragment : Fragment() {

    companion object {
        const val TAB = "tab"
        const val FOLLOWER = "Follower"
        const val FOLLOWING = "Following"
    }

    private lateinit var binding: FollowersFragmentBinding
    private lateinit var viewModel: FollowersViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        binding = FollowersFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()) [FollowersViewModel::class.java]

        val followers  = requireActivity().intent.getStringExtra(UserDetailActivity.EXTRA_FAVORITE)


        binding.rvFollow.layoutManager = LinearLayoutManager(activity)
        val userTab = arguments?.getString(TAB)


        if (userTab == FOLLOWER) {
            followers?.let { viewModel.getUserFollower(it) }
        } else if (userTab == FOLLOWING) {
            followers?.let { viewModel.getUserFollowing(it) }
        }


        viewModel.loading.observe(viewLifecycleOwner){
            showLoading(it)
        }


        viewModel.userFollow.observe(viewLifecycleOwner){
            val adapter = FollowAdapter(it)
            binding.apply {
                rvFollow.adapter = adapter
            }
        }

        viewModel.error.observe(viewLifecycleOwner){
            Toast.makeText(context, "Data Not Found", Toast.LENGTH_SHORT).show()
            viewModel.doneToastError()
        }
        return binding.root
    }

    private fun showLoading(isLoading : Boolean) {
        if (isLoading) {
            binding.loadingFollow.visibility = View.VISIBLE
        } else {
            binding.loadingFollow.visibility = View.GONE
        }
    }
}