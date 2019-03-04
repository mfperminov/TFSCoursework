package xyz.mperminov.tfscoursework.fragments.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_courses.*
import kotlinx.android.synthetic.main.fragment_progress.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.fragments.base.ToolbarTitleSetter
import xyz.mperminov.tfscoursework.utils.views.BadgeView


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
            for (i in 0 until profiles_container.childCount)
                for (j in 0 until (profiles_container[i] as ViewGroup).childCount)
                    if ((profiles_container[i] as ViewGroup)[j] is BadgeView) {
                        val currentBadge = (profiles_container[i] as ViewGroup)[j] as BadgeView
                        val rand = (0..10).random()
                        currentBadge.text = rand.toString()
                        if (rand == 0) currentBadge.visibility = View.INVISIBLE else currentBadge.visibility = View.VISIBLE
                    }
            swipe_layout.isRefreshing = false

        }

    }
}