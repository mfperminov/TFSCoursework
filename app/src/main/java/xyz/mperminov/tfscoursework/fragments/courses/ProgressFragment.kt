package xyz.mperminov.tfscoursework.fragments.courses

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
import xyz.mperminov.tfscoursework.utils.views.ProfileView

class ProgressFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        progress_header_layout.setOnClickListener {
            startContactActivity()
        }
    }

    private fun startContactActivity() {
        val intent = Intent(context, ContactActivity::class.java)
        startActivity(intent)
    }

    fun updateBadges() {
        BadgeUpdateThread(profiles_container.childCount).start()
    }

    fun setBadge(badgeId: Int, value: Int) {
        (profiles_container[badgeId] as ProfileView).setBadge(value)
    }

    inner class BadgeUpdateThread(private val profilesCount: Int) : Thread() {

        private val SEND_RANDOM = 10

        private val handler = Handler(
            Looper.getMainLooper(),
            Handler.Callback { msg ->
                if (msg.what == SEND_RANDOM) this@ProgressFragment.setBadge(
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
}

