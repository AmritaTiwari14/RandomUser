package com.randomuser.essencerandom.createrandomuser.screens

import android.os.SystemClock
import com.randomuser.essencerandom.createrandomuser.model.User
import java.util.*

object MockModel {
    fun buildUser(firstName: String,
                  lastName: String,
                  email: String,
                  nationality: String = "FR"): User {
        val name = User.Name("Mr", firstName, lastName)
        val location = User.Location("Paris", "2 Rue du Bourg", "75010", "")
        val login = User.Login(getId(), getId(), getId(), getId(), getId(), getId())
        val id = User.Id(getId(), getId())
        val picture = User.Picture("", "", "")
        return User(nationality, "male", "06.45.85.25.45", SystemClock.currentThreadTimeMillis(), name,
            SystemClock.currentThreadTimeMillis() - 1000, location, login, "02.25.25.26.24",
            email, id, picture)
    }

    private fun getId(): String {
        return UUID.randomUUID().toString()
    }
}