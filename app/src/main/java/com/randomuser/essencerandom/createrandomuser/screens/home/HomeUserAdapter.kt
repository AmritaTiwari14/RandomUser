package com.randomuser.essencerandom.createrandomuser.screens.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.randomuser.essencerandom.createrandomuser.R
import com.randomuser.essencerandom.createrandomuser.model.User
import java.util.*
import kotlin.collections.ArrayList

class HomeUserAdapter(private val users: ArrayList<User>): RecyclerView.Adapter<HomeUserAdapter.UserViewHolder>() {
    companion object {
        const val FADE_IN_DURATION = 300
    }
    var onItemClick: ((User) -> Unit)? = null
    var onBottomReached: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.item_user, parent, false
        )
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val user: User = users[position]

        val factory: DrawableCrossFadeFactory = DrawableCrossFadeFactory.Builder(FADE_IN_DURATION)
            .setCrossFadeEnabled(true)
            .build()

        Glide.with(context)
            .load(user.picture.medium)
            .placeholder(R.drawable.user_default_picture)
            .transition(DrawableTransitionOptions.withCrossFade(factory))
            .circleCrop()
            .into(holder.imageViewUserPicture)

        val username: String = context.getString(
            R.string.user_adapter_user_name,
            user.name.first.capitalize(Locale.getDefault()),
            user.name.last.capitalize(Locale.getDefault())
        )
        holder.textViewUsername.text = username
        holder.textViewUserEmail.text = user.email

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(user)
        }
        if (position == users.size - 2) {
            onBottomReached?.invoke()
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun addUsers(items: List<User>) {
        val lastAlbumsSize: Int = users.size
        users.addAll(items)
        val newAlbumsSize: Int = users.size
        if (newAlbumsSize > lastAlbumsSize) {
            notifyItemRangeChanged(lastAlbumsSize, newAlbumsSize)
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewUserPicture: ImageView = itemView.findViewById(R.id.image_view_user_picture)
        val textViewUsername: TextView = itemView.findViewById(R.id.text_view_user_name)
        val textViewUserEmail: TextView = itemView.findViewById(R.id.text_view_user_email)
    }
}