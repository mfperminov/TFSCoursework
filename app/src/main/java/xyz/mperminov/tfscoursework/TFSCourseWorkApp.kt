package xyz.mperminov.tfscoursework

import android.app.Application
import android.content.Context
import xyz.mperminov.tfscoursework.repositories.user.UserRepository
import xyz.mperminov.tfscoursework.repositories.user.prefs.SharedPrefUserRepository

class TFSCourseWorkApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initUserRepositoryInstance(applicationContext)
    }

    companion object {
        lateinit var repository : UserRepository

        private fun initUserRepositoryInstance(context: Context) {
                repository = SharedPrefUserRepository(context)

        }
    }

}