package com.randomuser.essencerandom.createrandomuser.helper

import android.content.Context
import android.content.Intent
import android.net.Uri

object IntentHelper {
    /**
     * Open and display the dialer with the given phone number.
     */
    fun launchCall(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phoneNumber")
        intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    /**
     * Open the following URI in the google map application if installed, otherwise will open it
     * in the browser.
     */
    fun launchMaps(context: Context, uri: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(uri)
        )
        intent.flags += Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}