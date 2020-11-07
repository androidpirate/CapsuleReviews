package com.github.androidpirate.capsulereviews.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.github.androidpirate.capsulereviews.util.internal.ConnectivityType
import com.github.androidpirate.capsulereviews.util.internal.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptorImpl(context: Context) : ConnectivityInterceptor {

    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!isOnline())
            throw NoConnectivityException()
        return chain.proceed(chain.request())
    }

    private fun isOnline(): Boolean {
        val result = getConnectionType(appContext)
        return result != ConnectivityType.NO_CONNECTIVITY
    }

    private fun getConnectionType(context: Context): ConnectivityType {
        var result = ConnectivityType.NO_CONNECTIVITY // Returns connection type. 0: none; 1: mobile data; 2: wifi
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            result = ConnectivityType.WIFI
                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            result = ConnectivityType.CELLULAR
                        }
                        hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                            result = ConnectivityType.VPN
                        }
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    when (type) {
                        ConnectivityManager.TYPE_WIFI -> {
                            result = ConnectivityType.WIFI
                        }
                        ConnectivityManager.TYPE_MOBILE -> {
                            result = ConnectivityType.CELLULAR
                        }
                        ConnectivityManager.TYPE_VPN -> {
                            result = ConnectivityType.VPN
                        }
                    }
                }
            }
        }
        return result
    }
}