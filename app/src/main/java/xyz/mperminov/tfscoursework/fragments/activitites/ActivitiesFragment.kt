package xyz.mperminov.tfscoursework.fragments.activitites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.fragments.base.ToolbarTitleSetter


class ActivitiesFragment : Fragment() {
    companion object {
        fun newInstance(): ActivitiesFragment {
            return ActivitiesFragment()
        }

        const val TAG: String = "ACTIVITIES_FRAGMENT"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as ToolbarTitleSetter).setTitle(getString(R.string.activities))
        return inflater.inflate(R.layout.fragment_activities,container,false)
    }
}