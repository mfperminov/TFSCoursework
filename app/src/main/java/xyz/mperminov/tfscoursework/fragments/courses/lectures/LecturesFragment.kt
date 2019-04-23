package xyz.mperminov.tfscoursework.fragments.courses.lectures

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_lectures.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.base.BaseChildFragment
import xyz.mperminov.tfscoursework.fragments.base.ChildFragmentsAdder
import xyz.mperminov.tfscoursework.fragments.base.ToolbarTitleSetter
import xyz.mperminov.tfscoursework.fragments.courses.tasks.TasksFragment
import xyz.mperminov.tfscoursework.utils.toast

class LecturesFragment : BaseChildFragment() {

    companion object {
        fun newInstance(): LecturesFragment {
            return LecturesFragment()
        }
    }

    private lateinit var viewModel: LecturesViewModel
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var layoutAdapter: LectureAdapter
    private var childFragmentsAdder: ChildFragmentsAdder? = null
    override fun onAttach(context: Context) {
        if (context is ChildFragmentsAdder) childFragmentsAdder = context else
            throw IllegalStateException("$context must implement ChildFragmentsAdder interface")
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as TFSCourseWorkApp).initLecturesComponent()
        viewModel = ViewModelProviders.of(this).get(LecturesViewModel::class.java)
        if (savedInstanceState == null) viewModel.updateLectures()
        viewModel.result.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> showSuccess()
                is Result.Error -> showError(result.error!!.localizedMessage)
                is Result.Loading -> showProgress()
                is Result.Empty -> showEmptyState()
            }
        })
    }

    private fun showEmptyState() {
        stopRefresh()
        context?.toast(getString(R.string.empty_lectures_message))
    }

    private fun showProgress() {
        swipe_layout.isRefreshing = true
    }

    private fun showSuccess() {
        stopRefresh()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lectures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ToolbarTitleSetter).setTitle(getString(R.string.lectures))
        layoutManager = LinearLayoutManager(context)
        layoutAdapter = LectureAdapter { lectureId -> goToTasksFragment(lectureId) }
        rv_lectures.adapter = layoutAdapter
        rv_lectures.layoutManager = layoutManager
        swipe_layout.setOnRefreshListener { viewModel.updateLectures() }
        viewModel.lecturesLiveData.observe(
            this,
            Observer { newLecturesList -> layoutAdapter.swapData(newLecturesList) })
    }

    private fun goToTasksFragment(lectureId: Int) {
        childFragmentsAdder?.addChildOnTop(TasksFragment.newInstance(lectureId))
    }

    private fun stopRefresh() {
        if (swipe_layout.isRefreshing) swipe_layout.isRefreshing = false
    }

    private fun showError(message: String?) {
        if (message != null)
            context?.toast(message)
    }

    override fun onDetach() {
        childFragmentsAdder = null
        super.onDetach()
    }

    override fun handleBackPress() {
        childFragmentsAdder?.recreateParentFragment()
    }
}

data class LectureModelView(val id: Int, val title: String)
