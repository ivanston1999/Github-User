package com.submission.githubfinal.ui.detail


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.submission.githubfinal.R
import com.submission.githubfinal.core.db.User
import com.submission.githubfinal.databinding.ActivityUserDetailBinding
import com.submission.githubfinal.core.modelresponse.DetailUserResponse
import com.submission.githubfinal.ui.main.MainActivity


class UserDetailActivity : AppCompatActivity() {

    private var _binding: ActivityUserDetailBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: DetailViewModel

    private var ivFavorite: Boolean = false
    private var favoriteUser: User? = null
    private var detailUser = DetailUserResponse()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = obtainViewModel(this@UserDetailActivity)
        val user = intent.getParcelableExtra<DetailUserResponse>(MainActivity.DATA)
        println("Print (${MainActivity.DATA}")

        if (user != null) {
            user.login?.let { viewModel.getDetailUser(it) }
        }

        viewModel.listUser.observe(this) { detailList ->
            detailUser = detailList

            if (detailList != null) {
                binding?.let {
                    Glide.with(this)
                        .load(detailList.avatarUrl)
                        .circleCrop()
                        .into(it.profileImage)
                }
            }
            binding?.apply {
                profileName.text = detailList.name
                profileUsername.text = detailList.login
                totalFollowers.text = detailList.followers.toString()
                totalFollowing.text = detailList.following.toString()
            }

            favoriteUser = User(detailList.id, detailList.login, detailList.avatarUrl)
            viewModel.getFavorite().observe(this) { userFavorite ->
                if (userFavorite != null) {
                    for (data in userFavorite) {
                        if (detailList.id == data.id) {
                            ivFavorite = true
                            binding?.IvFavorite?.setImageResource(R.drawable.favorited)
                        }
                    }
                }
            }
            binding?.IvFavorite?.setOnClickListener{
                if(!ivFavorite){
                    ivFavorite = true
                    binding!!.IvFavorite.setImageResource(R.drawable.favorited)
                    insertToDatabase(detailUser)
                } else {
                    ivFavorite = false
                    binding!!.IvFavorite.setImageResource(R.drawable.ic_favorite)
                    viewModel.delete(detailUser.id)
                    Toast.makeText(this, "Dihapus dari Favorite", Toast.LENGTH_SHORT).show()
                }
            }

            val pagerAdapter = PagerAdapter(this)
            val viewPager: ViewPager2 = findViewById(R.id.viewPager)
            viewPager.adapter = pagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabLayout)
            TabLayoutMediator(tabs, viewPager) {detailTabs, position ->
                detailTabs.text = resources.getString(GIT_TABS[position])
            }.attach()


        }



        viewModel.error.observe(this) {
            Toast.makeText(this, "Data tidak ada", Toast.LENGTH_SHORT).show()
            viewModel.doneToastError()
        }
    }

    private fun insertToDatabase(detailList: DetailUserResponse) {
        favoriteUser.let { favoriteUser ->
            favoriteUser?.id = detailList.id
            favoriteUser?.login = detailList.login
            favoriteUser?.imageUrl = detailList.avatarUrl
            viewModel.insert(favoriteUser as User)
            Toast.makeText(this, "Ditambahkan ke favorite", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = DetailViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory) [DetailViewModel::class.java]
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_FAVORITE = "extra_favorite"
        @StringRes
        val GIT_TABS = intArrayOf(
            R.string.tab2,
            R.string.tab1
        )
    }

}