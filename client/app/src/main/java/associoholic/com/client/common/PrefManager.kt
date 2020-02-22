package associoholic.com.client.common

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import associoholic.com.client.BuildConfig
import associoholic.com.client.util.SharedPreferenceLiveData
import associoholic.com.client.util.stringLiveData

class PrefManager(c: Context) {

    private val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(c.applicationContext)

    val liveDataToken: SharedPreferenceLiveData<String?> =
            sp.stringLiveData(PREF_TOKEN_ACCESS, null)

    val liveDataRecent: SharedPreferenceLiveData<String?> =
            sp.stringLiveData(PREF_RECENT_LOCATIONS, null)

    init {
        //upgrade sharedPrefs here if necessary
        sp.edit().putInt(PREF_VERSION_CODE, BuildConfig.VERSION_CODE).apply()
    }

    fun getVersionCode(): Int = sp.getInt(PREF_VERSION_CODE, 0)

    fun setUserToken(accessToken: String?, refreshToken: String?) = sp.edit()
            .putString(PREF_TOKEN_ACCESS, accessToken)
            .putString(PREF_TOKEN_REFRESH, refreshToken)
            .apply()

    fun getUserAccessToken() = sp.getString(PREF_TOKEN_ACCESS, null)

    fun getUserRefreshToken() = sp.getString(PREF_TOKEN_REFRESH, null)

    fun setRecentLocations(recentLocations: String) = sp.edit().putString(PREF_RECENT_LOCATIONS, recentLocations).apply()

    fun getRecentLocations() = sp.getString(PREF_RECENT_LOCATIONS, null)

    companion object {
        const val PREF_VERSION_CODE = "pref_version_code"
        const val PREF_TOKEN_ACCESS = "pref_token_access"
        const val PREF_RECENT_LOCATIONS = "pref_recent_locations"
        const val PREF_TOKEN_REFRESH = "pref_token_refresh"

        @Volatile
        private var INSTANCE: PrefManager? = null

        fun getInstance(context: Context): PrefManager =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: PrefManager(context).also { INSTANCE = it }
                }

        fun getInstance(): PrefManager? = INSTANCE

        fun tokenExists(): Boolean = getInstance()?.getUserAccessToken() != null
    }
}