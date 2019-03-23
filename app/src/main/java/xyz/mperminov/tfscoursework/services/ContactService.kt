package xyz.mperminov.tfscoursework.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import xyz.mperminov.tfscoursework.models.Contact

private const val ACTION_FETCH_CONTACTS = "xyz.mperminov.tfscoursework.services.FETCH_CONTACTS"
const val EXTRA_CONTACTS = "contacts"
const val BROADCAST_ACTION = "broadcast_action"

class ContactService : IntentService("Contacts service") {

    companion object {
        fun startActionFetchContacts(context: Context) {
            val intent = Intent(context, ContactService::class.java)
                .setAction(ACTION_FETCH_CONTACTS)
            context.startService(intent)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_FETCH_CONTACTS -> {
                val names = fetchContacts()
                sendLocalBroadcast(names)
            }
        }
    }

    private fun fetchContacts(): ArrayList<Contact> {
        val contacts = ArrayList<Contact>()
        val cursor = contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            ContactsContract.Data.MIMETYPE + " = ?",
            arrayOf(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE),
            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME
        )
        cursor?.use {
            while (it.moveToNext()) {
                val firstName = it.getString(
                    it.getColumnIndex(
                        ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME
                    )
                )
                val lastName = it.getString(
                    it.getColumnIndex(
                        ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME
                    )
                )
                contacts.add(Contact(firstName ?: "", lastName ?: ""))
            }
        }
        return contacts
    }

    private fun sendLocalBroadcast(contacts: ArrayList<Contact>) {
        val intent = Intent(BROADCAST_ACTION).putParcelableArrayListExtra(EXTRA_CONTACTS, contacts)
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.sendBroadcast(intent)

    }
}