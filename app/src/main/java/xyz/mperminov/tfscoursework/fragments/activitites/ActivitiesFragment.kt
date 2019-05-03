package xyz.mperminov.tfscoursework.fragments.activitites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.base.ToolbarTitleSetter
import xyz.mperminov.tfscoursework.repositories.activities.ActivitiesNetworkRepository
import javax.inject.Inject

class ActivitiesFragment : Fragment() {
    companion object {
        fun newInstance(): ActivitiesFragment {
            return ActivitiesFragment()
        }

        const val TAG: String = "ACTIVITIES_FRAGMENT"
    }

    @Inject
    lateinit var rep: ActivitiesNetworkRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        TFSCourseWorkApp.userComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_activities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as ToolbarTitleSetter).setTitle(getString(R.string.activities))
        super.onViewCreated(view, savedInstanceState)
    }
}