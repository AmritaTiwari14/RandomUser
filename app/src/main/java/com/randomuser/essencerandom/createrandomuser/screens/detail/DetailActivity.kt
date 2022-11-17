package com.randomuser.essencerandom.createrandomuser.screens.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.randomuser.essencerandom.createrandomuser.R
import com.randomuser.essencerandom.createrandomuser.databinding.ActivityDetailBinding
import com.randomuser.essencerandom.createrandomuser.helper.IntentHelper
import com.randomuser.essencerandom.createrandomuser.model.User
import com.randomuser.essencerandom.createrandomuser.screens.home.HomeActivity

/**
 * Display all the available information that we know about an user.
 * We mainly use DataBinding to display the information about the selected user.
 */
class DetailActivity : AppCompatActivity() {
    companion object {
        const val FADE_IN_DURATION = 300
    }
    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var detailBinding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        detailBinding.viewModel = detailViewModel
        detailBinding.lifecycleOwner = this
        setContentView(detailBinding.root)

        detailViewModel.user.observe(this) { user ->
            updateUser(user)
            bindClick(user)
        }

        intent.extras?.let { bundle ->
            if (bundle.containsKey(HomeActivity.KEY_USER_EMAIL)) {
                bundle.getString(HomeActivity.KEY_USER_EMAIL)?.apply {
                    detailViewModel.loadUser(this)
                }
            }
        }
    }

    private fun updateUser(user: User) {
        Glide.with(applicationContext)
            .load(user.picture.large)
            .placeholder(R.drawable.user_default_picture)
            .transition(DrawableTransitionOptions.withCrossFade(FADE_IN_DURATION))
            .into(detailBinding.imageViewUserPicture)

        val genderId: Int = if (user.gender == "male") {
            R.drawable.ic_male
        } else {
            R.drawable.ic_female
        }
        detailBinding.textViewUserName.setCompoundDrawablesWithIntrinsicBounds(0, 0, genderId, 0)

        if (user.id != null && user.id.name.isNotEmpty() && user.id.value.isNotEmpty()) {
            detailBinding.textViewUserId.text = getString(R.string.detail_activity_user_id, user.id.name, user.id.value)
        } else {
            detailBinding.textViewUserId.visibility = View.GONE
        }
    }

    private fun bindClick(user: User) {
        detailBinding.textViewUserCell.setOnClickListener {
            IntentHelper.launchCall(applicationContext, user.cell)
        }
        detailBinding.textViewUserPhone.setOnClickListener {
            IntentHelper.launchCall(applicationContext, user.phone)
        }
        detailBinding.textViewUserLocation.setOnClickListener {
            IntentHelper.launchMaps(applicationContext, user.getGoogleMapsURL())
        }
    }
}