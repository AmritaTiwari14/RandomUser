package com.randomuser.essencerandom.createrandomuser.screens.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.randomuser.essencerandom.createrandomuser.R
import com.randomuser.essencerandom.createrandomuser.databinding.ActivityHomeBinding
import com.randomuser.essencerandom.createrandomuser.helper.viewmodel.DataStatus
import com.randomuser.essencerandom.createrandomuser.helper.network.ConnectionLiveData
import com.randomuser.essencerandom.createrandomuser.helper.network.NetworkHelper
import com.randomuser.essencerandom.createrandomuser.screens.detail.DetailActivity


class HomeActivity : AppCompatActivity() {
    companion object {
        const val KEY_USER_EMAIL = "home_activity_user_email"
    }
    private var hasLostInternetOnce: Boolean = false
    private val userAdapter = HomeUserAdapter(ArrayList())
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var mainBinding: ActivityHomeBinding
    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        val recyclerViewAlbums: RecyclerView = mainBinding.recyclerViewUsers
        recyclerViewAlbums.adapter = userAdapter
        recyclerViewAlbums.layoutManager = LinearLayoutManager(applicationContext)
        userAdapter.onItemClick = { clickedUser ->
            val intent = Intent(applicationContext, DetailActivity::class.java)
            intent.putExtra(KEY_USER_EMAIL, clickedUser.email)
            startActivity(intent)
        }
        userAdapter.onBottomReached = {
            homeViewModel.loadUsers()
        }

        connectionLiveData = ConnectionLiveData(applicationContext)
        updateNetworkStatus()

        homeViewModel.loadUsers()
        updateUsersList()
    }

    private fun updateNetworkStatus() {
        homeViewModel.isNetworkAvailable.value = NetworkHelper.isOnline(applicationContext)
        connectionLiveData.observe(this) { hasNetwork ->
            homeViewModel.isNetworkAvailable.value = hasNetwork

            if (hasNetwork) {
                if (hasLostInternetOnce) {
                    displayNetworkStatus(true)
                    hasLostInternetOnce = false
                }
            } else {
                displayNetworkStatus(false)
                hasLostInternetOnce = true
            }
        }
    }

    private fun displayNetworkStatus(hasNetwork: Boolean) {
        val text: String = if (hasNetwork) {
            getString(R.string.home_activity_user_has_internet_connection)
        } else {
            getString(R.string.home_activity_user_has_no_internet_connection)
        }
        Snackbar.make(mainBinding.root, text, Snackbar.LENGTH_SHORT).show()
    }

    private fun updateUsersList() {
        homeViewModel.stateDataUsers.observe(this) { stateData ->
            when (stateData.status) {
                DataStatus.SUCCESS -> {
                    userAdapter.addUsers(stateData.data!!)
                }
                DataStatus.ERROR -> {
                    displayError(stateData.errorMessage!!)
                }
            }
        }
    }

    private fun displayError(errorMessage: String) {
        val snackBar: Snackbar = Snackbar.make(mainBinding.root, errorMessage, Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction(getString(R.string.action_retry)) {
            homeViewModel.isNetworkAvailable.value = NetworkHelper.isOnline(applicationContext)
            homeViewModel.loadUsers()
            snackBar.dismiss()
        }
        val snackBarView: View = snackBar.view
        val snackBarTextView: TextView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text)
        snackBarTextView.maxLines = 3
        snackBar.show()
    }
}