package xyz.mperminov.tfscoursework.fragments.courses

import android.content.Intent
import android.os.Bundle
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
        for (i in 0 until profiles_container.childCount) {
            val rand = (0..10).random()
            (profiles_container[i] as ProfileView).setBadge(rand)
        }
    }

}