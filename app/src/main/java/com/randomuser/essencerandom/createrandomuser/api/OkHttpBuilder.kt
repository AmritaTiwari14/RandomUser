package com.randomuser.essencerandom.createrandomuser.api

import android.os.Build
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.util.*
import javax.net.ssl.*
import kotlin.collections.ArrayList

object OkHttpBuilder {
    fun getClient(): OkHttpClient {
        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            getKitKatOkHttpClient()
        } else {
            OkHttpClient.Builder().build()
        }
    }


    private fun getKitKatOkHttpClient(): OkHttpClient {
        val trustAllCerts: Array<TrustManager> = arrayOf(TrustAllCertificateManager())

        // Install the all-trusting trust manager
        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
        val builder = OkHttpClient.Builder()
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)

        builder.connectionSpecs(Collections.singletonList(buildConnectionSpec()))

        builder.hostnameVerifier { hostname, sslSession ->
            isHostnameAuthorized(hostname, sslSession)
        }

        return builder.build()
    }

    private fun buildConnectionSpec(): ConnectionSpec {
        var cipherSuites: MutableList<CipherSuite?>? = ConnectionSpec.MODERN_TLS.cipherSuites()
        if (!cipherSuites!!.contains(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)) {
            cipherSuites = ArrayList(cipherSuites)
            cipherSuites.add(CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)
        }
        return ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .cipherSuites(*cipherSuites.toTypedArray())
            .build()
    }

    private fun isHostnameAuthorized(hostname: String?, sslSession: SSLSession?): Boolean {
        val hostnameVerifier: HostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier()
        val apiHostname: String = RandomUserAPI.HOSTNAME
        return if (hostname == apiHostname) {
            true
        } else {
            hostnameVerifier.verify(hostname, sslSession)
        }
    }
}