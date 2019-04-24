package xyz.mperminov.tfscoursework.fragments.students

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_contact_list.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.base.BaseChildFragment
import xyz.mperminov.tfscoursework.fragments.base.ChildFragmentsAdder
import xyz.mperminov.tfscoursework.utils.toast

class StudentsFragment : BaseChildFragment() {
    private lateinit var viewModel: StudentsViewModel
    private var childFragmentsAdder: ChildFragmentsAdder? = null
    private lateinit var currentLayoutManagerType: LayoutManagerType
    private var listener: OnUpSelectedHandler? = null

    enum class LayoutManagerType { GRID_LAYOUT_MANAGER, LINEAR_LAYOUT_MANAGER }

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var layoutAdapter: RecyclerView.Adapter<StudentsAdapter.ViewHolder>
    private lateinit var searchView: SearchView
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
        (activity?.application as TFSCourseWorkApp).initStudentComponent()
        viewModel = ViewModelProviders.of(this).get(StudentsViewModel::class.java)
        if (savedInstanceState == null) viewModel.getStudents()
        viewModel.result.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> showSuccess()
                is Result.Error -> showError(result.error!!.localizedMessage)
                is Result.Loading -> showProgress()
                is Result.Empty -> showEmptyState()
            }
        })
        setHasOptionsMenu(true)
    }

    private fun showEmptyState() {
        context?.toast(getString(R.string.no_students_message))
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
        viewModel.studentsLiveData.observe(
            this,
            Observer { students -> (rv.adapter as StudentsAdapter).students = students })
        val dividerItemDecoration = StudentItemDecoration(context!!)
        rv.addItemDecoration(dividerItemDecoration)
        rv.itemAnimator = StudentItemAnimator(context!!)
        setRecyclerViewLayoutManager(currentLayoutManagerType)
        swipe_refresh.setOnRefreshListener { viewModel.getStudents() }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun showSuccess() {
        hideProgress()
    }

    private fun showError(message: String) {
        hideProgress()
        context?.toast(message)
    }

    private fun showProgress() {
        swipe_refresh.isRefreshing = true
    }

    private fun hideProgress() {
        if (swipe_refresh.isRefreshing) swipe_refresh.isRefreshing = false
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
        setSearchView(menu)
    }

    private fun setSearchView(menu: Menu) {
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        if (!viewModel.searchQuery.value.isNullOrEmpty()) {
            searchView.setQuery(viewModel.searchQuery.value, false)
            searchView.isIconified = false
            searchView.clearFocus()
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.filter.filter(query)
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.filter.filter(query)
                return false
            }
        })
        searchView.setOnCloseListener { viewModel.resetFilter();searchView.onActionViewCollapsed(); true }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
            R.id.change_layout -> {
                if (currentLayoutManagerType == LayoutManagerType.LINEAR_LAYOUT_MANAGER)
                    setRecyclerViewLayoutManager(LayoutManagerType.GRID_LAYOUT_MANAGER)
                else setRecyclerViewLayoutManager(LayoutManagerType.LINEAR_LAYOUT_MANAGER)
            }
            R.id.sort_alpha -> viewModel.sortStudentsAlphabetically()
            R.id.sort_marks -> viewModel.sortStudentsByMarks()
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

