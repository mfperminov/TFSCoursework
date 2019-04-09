package xyz.mperminov.tfscoursework.fragments.contact

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_contact_list.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.fragments.base.BaseChildFragment
import xyz.mperminov.tfscoursework.fragments.base.ChildFragmentsAdder
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.students.StudentsRepository
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository
import xyz.mperminov.tfscoursework.utils.toast
import java.util.concurrent.TimeUnit

class StudentsFragment : BaseChildFragment(), UserNetworkRepository.TokenProvider, StudentsRepository.UpdateTimeSaver {
    private var childFragmentsAdder: ChildFragmentsAdder? = null

    private lateinit var currentLayoutManagerType: LayoutManagerType
    private var listener: OnUpSelectedHandler? = null

    enum class LayoutManagerType { GRID_LAYOUT_MANAGER, LINEAR_LAYOUT_MANAGER }

    private val studentsRepository = StudentsRepository(this, this)
    private var studentSchemaDisposable: Disposable? = null
    private val ARG_TIME_UPDATE = "last_time_updated"
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var layoutAdapter: RecyclerView.Adapter<StudentsAdapter.ViewHolder>
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnUpSelectedHandler && context is ChildFragmentsAdder) {
            listener = context
            childFragmentsAdder = context
        } else {
            throw RuntimeException("$context must implement OnUpSelectedHandler")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER

        if (savedInstanceState != null) {
            currentLayoutManagerType = savedInstanceState
                .getSerializable(KEY_LAYOUT_MANAGER) as LayoutManagerType
        }
        layoutAdapter = StudentsAdapter()
        rv.adapter = layoutAdapter
        val dividerItemDecoration = ContactItemDecoration(context!!)
        rv.addItemDecoration(dividerItemDecoration)
        rv.itemAnimator = ContactItemAnimator(context!!)
        setRecyclerViewLayoutManager(currentLayoutManagerType)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        studentSchemaDisposable =
            studentsRepository.getStudents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { students ->
                        (rv.adapter as StudentsAdapter).students = students
                    },
                    { e -> Log.e("error", e.localizedMessage) })

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
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        childFragmentsAdder = null
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
    }

    private fun deleteContact() {
    }

    private fun mixContacts() {
    }

    private fun setRecyclerViewLayoutManager(layoutManagerType: LayoutManagerType) {
        var scrollPosition = 0

        if (rv.layoutManager != null) {
            scrollPosition = (rv.layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition()
        }

        when (layoutManagerType) {
            StudentsFragment.LayoutManagerType.GRID_LAYOUT_MANAGER -> {
                layoutManager = GridLayoutManager(activity, SPAN_COUNT)
                currentLayoutManagerType = LayoutManagerType.GRID_LAYOUT_MANAGER
            }
            StudentsFragment.LayoutManagerType.LINEAR_LAYOUT_MANAGER -> {
                layoutManager = LinearLayoutManager(activity)
                currentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER
            }
        }

        if (rv.adapter != null) {
            (rv.adapter as StudentsAdapter).changeLayout(
                if (currentLayoutManagerType == LayoutManagerType.LINEAR_LAYOUT_MANAGER)
                    StudentsAdapter.LIST_ITEM
                else
                    StudentsAdapter.GRID_ITEM
            )
        }
        with(rv) {
            layoutManager = this@StudentsFragment.layoutManager
            scrollToPosition(scrollPosition)
        }
    }

    interface OnUpSelectedHandler {
        fun onUpSelected()
    }

    override fun saveUpdateTime(timestamp: Long) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(ARG_TIME_UPDATE, timestamp).apply()
    }

    override fun getTimeDiffInSeconds(currentTime: Long): Long {
        val lastTimeUpdate = PreferenceManager.getDefaultSharedPreferences(context).getLong(ARG_TIME_UPDATE, 0)
        return TimeUnit.MILLISECONDS.toSeconds(currentTime - lastTimeUpdate)
    }

    override fun handleBackPress() {
        childFragmentsAdder?.onBackPressHandled()
    }

    companion object {
        private const val KEY_LAYOUT_MANAGER = "layoutManager"
        private const val SPAN_COUNT = 2
        @JvmStatic
        fun newInstance() = StudentsFragment()
    }
}
