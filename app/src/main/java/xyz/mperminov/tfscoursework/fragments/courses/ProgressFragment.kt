package xyz.mperminov.tfscoursework.fragments.courses

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_progress.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.activitites.archive.Result
import xyz.mperminov.tfscoursework.fragments.base.ChildFragmentsAdder
import xyz.mperminov.tfscoursework.fragments.students.StudentsFragment
import xyz.mperminov.tfscoursework.repositories.students.db.Student
import xyz.mperminov.tfscoursework.utils.toast
import xyz.mperminov.tfscoursework.utils.views.ProfileView

class ProgressFragment : Fragment(), BadgeUpdateCallback {
    private lateinit var viewModel: ProgressViewModel
    private val topSize: Long = 10
    private var childFragmentsAdder: ChildFragmentsAdder? = null
    override fun onAttach(context: Context) {
        if (context is ChildFragmentsAdder) childFragmentsAdder = context
        super.onAttach(context)
    }

    override fun onDetach() {
        childFragmentsAdder = null
        super.onDetach()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as TFSCourseWorkApp).initStudentComponent()
        viewModel = ViewModelProviders.of(this).get(ProgressViewModel::class.java)
        if (savedInstanceState == null)
            viewModel.getTopStudents(topSize)
        (activity?.application as TFSCourseWorkApp).initStudentComponent()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progress_header_layout.setOnClickListener {
            childFragmentsAdder?.addChildOnTop(StudentsFragment.newInstance())
        }
        viewModel.topStudents.observe(this, Observer { result ->
            when (result) {
                is Result.Success<Student> -> updateBadges(result.data)
                is Result.Error<Student> -> showError(result.e!!)
                is Result.Empty<Student> -> showEmptyState()
                is Result.Loading<Student> -> showProgress()
            }
        })
    }

    private fun showProgress() {
        top_students_progress.visibility = View.VISIBLE
    }

    private fun showEmptyState() {
        top_students_progress.visibility = View.GONE
        context?.toast(getString(R.string.rating_no_students))
    }

    private fun showError(e: Throwable) {
        top_students_progress.visibility = View.GONE
        context?.toast(e.localizedMessage)
    }

    private fun updateBadges(students: List<Student>) {
        top_students_progress.visibility = View.GONE
        for (i in 0 until students.size) {
            val profileView = ProfileView(context!!)
            profileView.setName(students[i].getFirstName())
            profileView.setBadge(students[i].mark.toInt())
            if (students[i].userProfile) profileView.enableBorder() else profileView.disableBorder()
            profiles_container.addView(profileView)
        }
    }

    fun updateBadges() {
//        BadgeUpdateThread(this, profiles_container.childCount).start()
    }

    override fun setBadge(profileId: Int, badgeValue: Int) {
        (profiles_container[profileId] as ProfileView).setBadge(badgeValue)
    }
}

interface BadgeUpdateCallback {
    fun setBadge(profileId: Int, badgeValue: Int)
}

