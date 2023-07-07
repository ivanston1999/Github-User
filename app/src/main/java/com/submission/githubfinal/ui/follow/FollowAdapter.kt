package com.submission.githubfinal.ui.follow

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.githubfinal.core.modelresponse.DetailUserResponse
import com.submission.githubfinal.databinding.ItemUserBinding


class FollowAdapter(private val UserList: List<DetailUserResponse>) : RecyclerView.Adapter<FollowAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun getItemCount(): Int = UserList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gitUser = UserList[position]
        Glide.with(holder.itemView.context)
            .load(gitUser.avatarUrl)
            .into(holder.binding.ivUser)
        holder.apply {
            binding.apply {
                tvUsername.text = gitUser.login
            }
        }
    }

    class ViewHolder(val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root)
}