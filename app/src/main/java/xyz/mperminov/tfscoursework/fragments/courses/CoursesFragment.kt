package xyz.mperminov.tfscoursework.fragments.courses

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_courses.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.base.ToolbarTitleSetter
import xyz.mperminov.tfscoursework.repositories.students.CourseResponse

class CoursesFragment : Fragment() {
    private lateinit var viewModel: CoursesViewModel
    companion object {
        fun newInstance(): CoursesFragment {
            return CoursesFragment()
        }
        const val TAG = "COURSES_FRAGMENT"
        const val webViewUrl = "https://fintech.tinkoff.ru/api/course/%s/about"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as TFSCourseWorkApp).initStudentComponent()
        viewModel = ViewModelProviders.of(this).get(CoursesViewModel::class.java)
        if (savedInstanceState == null)
            viewModel.updateCourseInfo()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_courses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ToolbarTitleSetter).setTitle(getString(R.string.courses))
        viewModel.courseinfo.observe(this, Observer { it -> updateUi(it) })
        swipe_layout.setOnRefreshListener {
            val progressFragment = childFragmentManager.findFragmentById(R.id.progress_fragment) as? ProgressFragment
            progressFragment?.apply { updateBadges() }
            swipe_layout.isRefreshing = false
        }
    }

    private fun updateUi(response: CourseResponse?) {
        if (response == null)
            hideCourseViews()
        else {
            showCourseViews()
            course_header.text = response.title
            more_info_btn.setOnClickListener { openInfoAboutCourse(response.url) }
        }
    }

    private fun openInfoAboutCourse(url: String) {
        startActivity(Intent(context, CourseInfoActivity::class.java).apply {
            putExtra(
                "url",
                String.format(webViewUrl, url)
            )
        })
    }

    private fun showCourseViews() {
        course_header.visibility = View.VISIBLE
        more_info_btn.visibility = View.VISIBLE
    }

    private fun hideCourseViews() {
        course_header.visibility = View.GONE
        more_info_btn.visibility = View.GONE
    }

}