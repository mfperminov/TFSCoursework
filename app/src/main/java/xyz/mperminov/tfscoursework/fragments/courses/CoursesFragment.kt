package xyz.mperminov.tfscoursework.fragments.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_courses.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.fragments.base.ToolbarTitleSetter


class CoursesFragment : Fragment() {
    companion object {
        fun newInstance(): CoursesFragment {
            return CoursesFragment()
        }
        const val TAG = "COURSES_FRAGMENT"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_courses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ToolbarTitleSetter).setTitle(getString(R.string.courses))
        swipe_layout.setOnRefreshListener {
            val progressFragment = childFragmentManager.findFragmentById(R.id.progress_fragment) as? ProgressFragment
            progressFragment?.apply { updateBadges() }
            swipe_layout.isRefreshing = false
        }
    }
}