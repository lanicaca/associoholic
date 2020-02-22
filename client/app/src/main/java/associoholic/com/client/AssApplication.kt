package associoholic.com.client

import android.app.Application
import associoholic.com.client.common.PrefManager

class AssApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PrefManager.getInstance(this)
    }
}