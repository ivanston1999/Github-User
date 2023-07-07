package com.submission.githubfinal.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.submission.githubfinal.R
import com.submission.githubfinal.core.modelresponse.DetailUserResponse
import com.submission.githubfinal.databinding.ActivityMainBinding
import com.submission.githubfinal.ui.darklight.*
import com.submission.githubfinal.ui.favorite.DetailFavorite
import com.submission.githubfinal.ui.favorite.FavoriteActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "setting")

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvDetail: RecyclerView

    companion object {
        const val DATA = "data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        viewModel.listUser.observe(this){gitUserList ->
            showRecyclerList(gitUserList)
        }

        viewModel.loading.observe(this) {
            showLoading(it)
        }
        viewModel.error.observe(this) {
            Toast.makeText(this, "Data Not Found", Toast.LENGTH_SHORT).show()
            viewModel.doneToastError()
        }
        rvDetail = binding.rvUser
        rvDetail.setHasFixedSize(true)

        val mode = PreferencesMode.getInstance(dataStore)

        val themeSettingView =
            ViewModelProvider(this, ThemeViewModelFactory(mode)).get(ThemeViewModel::class.java)

        themeSettingView.getThemeSettings().observe(this) {isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun showRecyclerList(gitUserList: List<DetailUserResponse>) {
        rvDetail.layoutManager = LinearLayoutManager(this)
        val searchUserAdapter = MainAdapter(gitUserList)
        rvDetail.adapter = searchUserAdapter

        searchUserAdapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback{
            override fun onItemClicked(data: DetailUserResponse) {
                val intentDetail = Intent(this@MainActivity, DetailFavorite::class.java)
                intentDetail.putExtra("extra_favorite", data.login)
                startActivity(intentDetail)
            }
        })
    }





    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_bar, menu)

        val sManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val sItem: MenuItem? = menu.findItem(R.id.action_search)
        val sView = sItem?.actionView as SearchView

        sView.setSearchableInfo(sManager.getSearchableInfo(componentName))
        sView.queryHint = resources.getString(R.string.hint)


        sItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                viewModel.detailUser()
                return true
            }
        })


        sView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchUser(query.toString())
                sView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.theme -> {
                Intent(this@MainActivity, ThemeActivity::class.java).apply {
                    startActivity(this)
                }
            }
            R.id.list_favorited -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }



}