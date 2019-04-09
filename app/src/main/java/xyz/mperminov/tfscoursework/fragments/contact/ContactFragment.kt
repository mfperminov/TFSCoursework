package xyz.mperminov.tfscoursework.fragments.contact

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_contact_list.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.models.Contact
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.network.RestClient
import xyz.mperminov.tfscoursework.repositories.students.db.StudentMapper
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository
import xyz.mperminov.tfscoursework.utils.toast

class ContactFragment : Fragment(), UserNetworkRepository.TokenProvider {
    private var contacts = mutableListOf<Contact>()
    private val firstNames: Array<String> = arrayOf("Alexander", "Mikhail", "Ivan", "Tikhon")
    private val lastNames: Array<String> = arrayOf("Ivanov", "Petrov", "Sidorov", "Martynov")
    private lateinit var currentLayoutManagerType: LayoutManagerType
    private var listener: OnUpSelectedHandler? = null

    enum class LayoutManagerType { GRID_LAYOUT_MANAGER, LINEAR_LAYOUT_MANAGER }

    private val api = RestClient.api
    private var studentSchemaDisposable: Disposable? = null
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var layoutAdapter: RecyclerView.Adapter<ContactAdapter.ViewHolder>
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnUpSelectedHandler) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnUpSelectedHandler")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contacts = it.getParcelableArrayList<Contact>(ARG_CONTACTS).orEmpty().toMutableList()
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER

        if (savedInstanceState != null) {
            currentLayoutManagerType = savedInstanceState
                .getSerializable(KEY_LAYOUT_MANAGER) as LayoutManagerType
            contacts = savedInstanceState.getParcelableArrayList<Contact>(ARG_CONTACTS) as MutableList<Contact>
        }
        layoutAdapter = ContactAdapter(contacts)
        rv.adapter = layoutAdapter
        val dividerItemDecoration = ContactItemDecoration(context!!)
        rv.addItemDecoration(dividerItemDecoration)
        rv.itemAnimator = ContactItemAnimator(context!!)
        setRecyclerViewLayoutManager(currentLayoutManagerType)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        val token = getToken()
        if (token != null)
            studentSchemaDisposable =
                api.getStudents(token).take(1).map { list -> StudentMapper().mapToDbModel(list) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { studentSchema ->
                            Log.d("student schema", studentSchema.joinToString()); Log.d("students initials",
                            studentSchema.joinToString { it.getInitials() })
                        },
                        { e -> Log.e("error", e.localizedMessage) })
        else
            showError(getString(R.string.error_no_user_auth))
        super.onStart()
    }

    override fun onStop() {
        studentSchemaDisposable?.dispose()
        super.onStop()
    }

    private fun showError(message: String) {
        context?.toast(message)
    }

    override fun getToken(): String? {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(AuthHolder.AUTH_TOKEN_ARG, null)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, currentLayoutManagerType)
        savedInstanceState.putParcelableArrayList(ARG_CONTACTS, contacts as ArrayList<Contact>)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.contact_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
            R.id.change_layout -> {
                if (currentLayoutManagerType == LayoutManagerType.LINEAR_LAYOUT_MANAGER)
                    setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER)
                else setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER)
            }
            R.id.add_contact -> addContact()
            R.id.delete_contact -> deleteContact()
            R.id.mix_contacts -> mixContacts()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addContact() {
        contacts.add(Contact(getRandomFirstName(), getRandomLastName()))
        layoutAdapter.notifyItemInserted(contacts.size - 1)
    }

    private fun getRandomFirstName(): String = firstNames.random()
    private fun getRandomLastName(): String = lastNames.random()
    private fun deleteContact() {
        if (contacts.size == 0) {
            context?.toast(getString(R.string.nothing_delete))
            return
        }
        val positionToDelete = (0 until contacts.size).random()
        contacts.removeAt(positionToDelete)
        layoutAdapter.notifyItemRemoved(positionToDelete)
    }

    private fun mixContacts() {
        val oldContacts = mutableListOf<Contact>()
        oldContacts.addAll(contacts)
        contacts.shuffle()
        val diffResult = DiffUtil.calculateDiff(ContactsDiffUtil(oldContacts, contacts))
        diffResult.dispatchUpdatesTo(layoutAdapter)
    }

    private fun setRecyclerViewLayoutManager(layoutManagerType: LayoutManagerType) {
        var scrollPosition = 0

        if (rv.layoutManager != null) {
            scrollPosition = (rv.layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition()
        }

        when (layoutManagerType) {
            ContactFragment.LayoutManagerType.GRID_LAYOUT_MANAGER -> {
                layoutManager = GridLayoutManager(activity, SPAN_COUNT)
                currentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER
            }
            ContactFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER -> {
                layoutManager = LinearLayoutManager(activity)
                currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER
            }
        }

        if (rv.adapter != null) {
            (rv.adapter as ContactAdapter).changeLayout(
                if (currentLayoutManagerType == LayoutManagerType.LINEAR_LAYOUT_MANAGER)
                    ContactAdapter.LIST_ITEM
                else
                    ContactAdapter.GRID_ITEM
            )
        }
        with(rv) {
            layoutManager = this@ContactFragment.layoutManager
            scrollToPosition(scrollPosition)
        }
    }

    interface OnUpSelectedHandler {
        fun onUpSelected()
    }

    companion object {
        private const val KEY_LAYOUT_MANAGER = "layoutManager"
        private const val SPAN_COUNT = 2
        const val ARG_CONTACTS = "contacts"
        @JvmStatic
        fun newInstance(contacts: List<Contact>) = ContactFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(ARG_CONTACTS, ArrayList(contacts))
            }
        }
    }
}
