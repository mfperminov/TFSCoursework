package xyz.mperminov.tfscoursework.fragments.courses

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_progress.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.activities.ContactActivity
import xyz.mperminov.tfscoursework.fragments.base.ChildFragmentsAdder
import xyz.mperminov.tfscoursework.fragments.students.StudentsFragment
import xyz.mperminov.tfscoursework.utils.views.ProfileView
import java.lang.ref.WeakReference

class ProgressFragment : Fragment(), BadgeUpdateCallback {
    private var childFragmentsAdder: ChildFragmentsAdder? = null
    override fun onAttach(context: Context) {
        if (context is ChildFragmentsAdder) childFragmentsAdder = context
        super.onAttach(context)
    }

    override fun onDetach() {
        childFragmentsAdder = null
        super.onDetach()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progress_header_layout.setOnClickListener {
            childFragmentsAdder?.addChildOnTop(StudentsFragment.newInstance())
        }
    }

    private fun startContactActivity() {
        val intent = Intent(context, ContactActivity::class.java)
        startActivity(intent)
    }

    fun updateBadges() {
        BadgeUpdateThread(this, profiles_container.childCount).start()
    }

    override fun setBadge(profileId: Int, badgeValue: Int) {
        (profiles_container[profileId] as ProfileView).setBadge(badgeValue)
    }
}

class BadgeUpdateThread(badgeUpdateCallback: BadgeUpdateCallback, private val profilesCount: Int) : Thread() {

    private val reference: WeakReference<BadgeUpdateCallback> = WeakReference(badgeUpdateCallback)

    private val SEND_RANDOM = 10

    private val handler = Handler(
        Looper.getMainLooper(),
        Handler.Callback { msg ->
            if (msg.what == SEND_RANDOM)
                if (reference.get() != null) (reference.get() as BadgeUpdateCallback).setBadge(
                    msg.arg1,
                    msg.arg2
                ); true
        })

    override fun run() {
        for (i in 0 until profilesCount) {
            val rand = (0..10).random()
            val message = handler.obtainMessage(SEND_RANDOM, i, rand)
            handler.sendMessage(message)
        }
    }
}

interface BadgeUpdateCallback {
    fun setBadge(profileId: Int, badgeValue: Int)
}

