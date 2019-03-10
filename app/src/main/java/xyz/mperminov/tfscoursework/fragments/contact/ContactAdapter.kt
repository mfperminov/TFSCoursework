package xyz.mperminov.tfscoursework.fragments.contact


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_contact.view.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.models.Contact

class ContactAdapter(
    private val contacts: List<Contact>
    ) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_contact, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(contacts[position])

    override fun getItemCount(): Int = contacts.size

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        private val mIdView: TextView = mView.name
        private val mContentView: TextView = mView.points
        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }

        fun bind(contact: Contact) {
            mIdView.text = "${contact.firstName} ${contact.lastName}"
            mContentView.text = contact.points.toString()
        }
    }
}
