package com.submission.githubfinal.ui.favorite

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.submission.githubfinal.R
import com.submission.githubfinal.core.db.User
import com.submission.githubfinal.core.modelresponse.DetailUserResponse
import com.submission.githubfinal.databinding.ActivityUserDetailBinding
import com.submission.githubfinal.ui.detail.DetailViewModel
import com.submission.githubfinal.ui.detail.PagerAdapter
import com.submission.githubfinal.ui.detail.UserDetailActivity
import com.submission.githubfinal.ui.detail.DetailViewModelFactory

class DetailFavorite : AppCompatActivity() {

    private var _binding: ActivityUserDetailBinding? = null
    private val binding get() = _binding

    private var detailUser = DetailUserResponse()
    private var ivFavorite: Boolean = false
    private var favUser: User? = null

    private lateinit var viewModel: DetailViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = obtainViewModel(this@DetailFavorite)
        val user = intent.getStringExtra(UserDetailActivity.EXTRA_FAVORITE)

        if (user != null) {
            viewModel.getDetailUser(user)
        }


        viewModel.listUser.observe(this) { detailList ->
            detailUser = detailList

            favUser = User(detailUser.id, detailUser.login, detailUser.avatarUrl)
            binding?.let {
                Glide.with(this)
                    .load(detailUser.avatarUrl)
                    .circleCrop()
                    .into(it.profileImage)
            }
            binding?.apply {
                profileName.text = detailUser.name
                profileUsername.text = detailUser.login
                totalFollowers.text = detailUser.followers.toString()
                totalFollowing.text = detailUser.following.toString()
            }

            viewModel.getFavorite().observe(this) { userFavorite ->
                if (userFavorite != null) {
                    for (data in userFavorite) {
                        if (detailUser.id == data.id) {
                            ivFavorite = true
                            binding?.IvFavorite?.setImageResource(R.drawable.favorited)
                        }
                    }
                }
            }

            binding?.IvFavorite?.setOnClickListener {
                if (!ivFavorite) {
                    ivFavorite = true
                    binding!!.IvFavorite.setImageResource(R.drawable.favorited)
                    insertToDatabase(detailUser)
                } else {
                    ivFavorite = false
                    binding!!.IvFavorite.setImageResource(R.drawable.ic_favorite)
                    viewModel.delete(detailUser.id)
                    Toast.makeText(this, "User Dihapus dari Favorite", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val pagerAdapter = PagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = pagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabLayout)
        TabLayoutMediator(tabs, viewPager) { detailTabs, position ->
            detailTabs.text = resources.getString(UserDetailActivity.GIT_TABS[position])
        }.attach()

        viewModel.loading.observe(this){
            showLoading(it)
        }

        viewModel.error.observe(this){
            Toast.makeText(this, "Data tidak ada", Toast.LENGTH_SHORT).show()
            viewModel.doneToastError()
        }
    }

    private fun insertToDatabase(gitDetailList: DetailUserResponse) {
        favUser.let { favoriteUser ->
            favoriteUser?.id = gitDetailList.id
            favoriteUser?.login = gitDetailList.login
            favoriteUser?.imageUrl = gitDetailList.avatarUrl
            viewModel.insert(favoriteUser as User)
            Toast.makeText(this, "Ditambahkan ke favorite", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = DetailViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DetailViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBar2?.visibility = View.VISIBLE
        } else {
            binding?.progressBar2?.visibility = View.GONE
        }
    }
}