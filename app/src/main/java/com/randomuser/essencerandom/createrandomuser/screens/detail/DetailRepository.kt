package com.randomuser.essencerandom.createrandomuser.screens.detail

import com.randomuser.essencerandom.createrandomuser.database.UserDao
import com.randomuser.essencerandom.createrandomuser.model.User
import org.koin.core.KoinComponent
import org.koin.core.inject

class DetailRepository: KoinComponent {
    private val userDao: UserDao by inject()

    suspend fun retrieveUser(userEmail: String): User {
        return userDao.get(userEmail)
    }
}