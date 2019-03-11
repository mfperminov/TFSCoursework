package xyz.mperminov.tfscoursework.activities

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.contact.ContactFragment
import xyz.mperminov.tfscoursework.models.Contact
import xyz.mperminov.tfscoursework.services.BROADCAST_ACTION
import xyz.mperminov.tfscoursework.services.ContactService
import xyz.mperminov.tfscoursework.services.EXTRA_CONTACTS


class ContactActivity : AppCompatActivity(), ContactFragment.ListCallbacks {

    private val PERMISSIONS_REQUEST_READ_CONTACTS = 4353
    private val contactRepository = TFSCourseWorkApp.contactsHolder
    lateinit var localBroadcastManager: LocalBroadcastManager

    private var reciever: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if (intent.hasExtra(EXTRA_CONTACTS)) {
                val contacts = intent.getParcelableArrayListExtra<Contact>(EXTRA_CONTACTS)
                contactRepository.saveContacts(contacts)
                supportFragmentManager.beginTransaction()
                    .add(
                        R.id.container,
                        ContactFragment.newInstance(contacts)
                    ).commit()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        supportActionBar?.title = getString(R.string.progress)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        localBroadcastManager = LocalBroadcastManager.getInstance(this)
        if (savedInstanceState == null)
            checkPermissions()

    }

    private fun checkPermissions() {
        if (hasNoPermissionsToReadContacts())
            requestPermissionsToReadContacts()
        else
            ContactService.startActionFetchContacts(this)
    }

    override fun onResume() {
        val intentFilter = IntentFilter(BROADCAST_ACTION)
        localBroadcastManager.registerReceiver(reciever, intentFilter)
        super.onResume()
    }

    override fun onPause() {
        localBroadcastManager.unregisterReceiver(reciever)
        super.onPause()
    }

    private fun hasNoPermissionsToReadContacts(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        )
                != PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermissionsToReadContacts() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_CONTACTS),
            PERMISSIONS_REQUEST_READ_CONTACTS
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_CONTACTS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    ContactService.startActionFetchContacts(this)
                } else {
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    //region ContactFragment.ListCallbacks implementation
    override fun onItemAdded() {
    }

    override fun onItemRemoved() {
    }

    override fun onItemsMixed() {
    }
    //endregion
}
