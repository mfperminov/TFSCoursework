package xyz.mperminov.tfscoursework.fragments.contact


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.models.Contact

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ContactFragment.ListCallbacks] interface.
 */
class ContactFragment : Fragment() {

    private var contacts = mutableListOf<Contact>()

    private var listener: ListCallbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ListCallbacks) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement ListCallbacks")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contacts = (it.getParcelableArrayList<Contact>(ARG_CONTACTS) as List<Contact>).toMutableList()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = ContactAdapter(
                    contacts
                )
            }
        }
        return view
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface ListCallbacks {

        fun onItemAdded()

        fun onItemRemoved()

        fun onItemsMixed()
    }

    companion object {

        const val ARG_CONTACTS = "contacts"

        @JvmStatic
        fun newInstance(contacts: List<Contact>) = ContactFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(ARG_CONTACTS, ArrayList(contacts))
            }
        }

    }
}
