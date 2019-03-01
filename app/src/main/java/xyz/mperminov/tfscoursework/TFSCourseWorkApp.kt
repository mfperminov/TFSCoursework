package xyz.mperminov.tfscoursework

import android.app.Application
import android.content.Context
import xyz.mperminov.tfscoursework.repositories.SharedPrefUserRepository
import xyz.mperminov.tfscoursework.repositories.UserRepository

class TFSCourseWorkApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initUserRepositoryInstance(applicationContext)
    }

    companion object {
        var repository : UserRepository? = null

        private fun initUserRepositoryInstance(context: Context) {
            if (repository == null)
                repository = SharedPrefUserRepository(context)

        }
    }

}