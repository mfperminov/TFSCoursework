package xyz.mperminov.tfscoursework.fragments.contact


import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_contact_list.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.models.Contact
import androidx.recyclerview.widget.DividerItemDecoration




class ContactFragment : Fragment() {

    private var contacts = mutableListOf<Contact>()
    private lateinit var currentLayoutManagerType: LayoutManagerType

    enum class LayoutManagerType { GRID_LAYOUT_MANAGER, LINEAR_LAYOUT_MANAGER }

    private lateinit var layoutManager: RecyclerView.LayoutManager

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
        }
        rv.adapter = ContactAdapter(contacts)
        val dividerItemDecoration = ContactItemDecoration(context!!)
        rv.addItemDecoration(dividerItemDecoration)
        setRecyclerViewLayoutManager(currentLayoutManagerType)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {

        // Save currently selected layout manager.
        savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, currentLayoutManagerType)
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
        }
        return super.onOptionsItemSelected(item)
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

    interface ListCallbacks {

        fun onItemAdded()

        fun onItemRemoved()

        fun onItemsMixed()
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
