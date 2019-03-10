package xyz.mperminov.tfscoursework.repositories.contacts

import xyz.mperminov.tfscoursework.models.Contact

interface ContactsHolder {
    fun saveContacts(contacts: List<Contact>)
    fun getContacts(): List<Contact>
    fun contactsNotEmpty(): Boolean
}

class ContactsHolderImpl: ContactsHolder {
    private var contacts: List<Contact>? = null
    override fun saveContacts(contacts: List<Contact>) {
        this.contacts = contacts
    }

    override fun getContacts(): List<Contact>  = contacts ?: listOf()

    override fun contactsNotEmpty(): Boolean = !contacts.isNullOrEmpty()
}