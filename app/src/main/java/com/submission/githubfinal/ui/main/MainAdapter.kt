package com.submission.githubfinal.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.githubfinal.core.db.User
import com.submission.githubfinal.core.modelresponse.DetailUserResponse
import com.submission.githubfinal.databinding.ItemUserBinding

class MainAdapter(private val userList: List<DetailUserResponse>) : ListAdapter<User, MainAdapter.ViewHolder>(DIFF_CALLBACK
) {

    private lateinit var onItemClickCallBack: OnItemClickCallback
    class ViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gitUser = userList[position]

        Glide.with(holder.itemView.context)
            .load(gitUser.avatarUrl)
            .centerCrop()
            .into(holder.binding.ivUser)
        holder.apply {
            binding.apply {
                tvUsername.text = gitUser.login
                itemView.setOnClickListener{
                    onItemClickCallBack.onItemClicked(userList[holder.adapterPosition])
                }
            }
        }
    }

    override fun getItemCount(): Int = userList.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallBack = onItemClickCallback
    }
    interface OnItemClickCallback {
        fun onItemClicked(data: DetailUserResponse)
    }
    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<User> =
            object : DiffUtil.ItemCallback<User>() {
                override fun areItemsTheSame(oldUser: User, newUser: User): Boolean {
                    return oldUser.login == newUser.login
                }

                @SuppressLint("DiffUtilsEquals")
                override fun areContentsTheSame(oldUser: User, newUser: User): Boolean {
                    return oldUser == newUser
                }
            }
    }
}