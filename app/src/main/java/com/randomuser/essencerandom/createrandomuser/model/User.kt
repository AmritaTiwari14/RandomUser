package com.randomuser.essencerandom.createrandomuser.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class User(val nat: String,
                val gender: String,
                val phone: String,
                val dob: Long,
                val name: Name,
                val registered: Long,
                val location: Location,
                val login: Login,
                val cell: String,
                @PrimaryKey
                val email: String,
                val id: Id?,
                val picture: Picture) {
    companion object {
        const val DATE_FORMAT = "dd MMM YYYY"
    }

    @Entity
    data class Name(
        val title: String,
        val first: String,
        val last: String
    )

    @Entity
    data class Location(
        val city: String,
        val street: String,
        val postcode: String,
        val state: String) {
        override fun toString(): String {
            val locale: Locale = Locale.getDefault()
            return "${street.capitalize(locale)}\n" +
                    "$postcode ${city.toUpperCase(locale)}\n" +
                    state.capitalize(locale)
        }
    }

    @Entity
    data class Login(
        val sha1: String,
        val username: String,
        val password: String,
        val salt: String,
        val sha256: String,
        val md5: String
    )

    @Entity
    data class Id(
        val name: String,
        val value: String
    )

    @Entity
    data class Picture(
        val thumbnail: String,
        val medium: String,
        val large: String
    )

    fun getUserCompleteName(): String {
        val defaultLocale: Locale = Locale.getDefault()
        return "${name.title.capitalize(defaultLocale)} " +
                "${name.first.capitalize(defaultLocale)} " +
                name.last.capitalize(defaultLocale)
    }

    fun getCountryName(): String {
        val loc = Locale("", nat)
        return loc.displayCountry
    }

    fun getDateOfBirth(): String {
        return getDateTime(dob) ?: ""
    }

    fun getRegistrationDate(): String {
        return getDateTime(registered) ?: ""
    }

    private fun getDateTime(timestamp: Long): String? {
        return try {
            val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            val date = Date(timestamp * 1_000)
            dateFormat.format(date)
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun getGoogleMapsURL(): String {
        val googleURL = "https://www.google.com/maps/search/?api=1&query="
        return "$googleURL${location.street}+${location.postcode}+${location.city}+${getCountryName()}"
    }
}