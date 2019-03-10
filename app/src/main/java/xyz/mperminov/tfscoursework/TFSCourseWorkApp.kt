package xyz.mperminov.tfscoursework

import android.app.Application
import android.content.Context
import xyz.mperminov.tfscoursework.repositories.contacts.ContactsHolder
import xyz.mperminov.tfscoursework.repositories.contacts.ContactsHolderImpl
import xyz.mperminov.tfscoursework.repositories.user.SharedPrefUserRepository
import xyz.mperminov.tfscoursework.repositories.user.UserRepository

class TFSCourseWorkApp: Application() {

    override fun onCreate() {
        super.onCreate()
        initUserRepositoryInstance(applicationContext)
        initContactRepositoryInstance()
    }

    companion object {
        lateinit var repository : UserRepository
        lateinit var contactsHolder: ContactsHolder

        private fun initUserRepositoryInstance(context: Context) {
                repository = SharedPrefUserRepository(context)

        }
        private fun initContactRepositoryInstance() {
                contactsHolder = ContactsHolderImpl()
        }
    }

}