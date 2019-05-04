package xyz.mperminov.tfscoursework.fragments.courses

import android.app.Activity
import android.os.Bundle
import kotlinx.android.synthetic.main.course_info_activity.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.network.AuthHolder
import javax.inject.Inject

class CourseInfoActivity : Activity() {
    @Inject
    lateinit var authHolder: AuthHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TFSCourseWorkApp.studentComponent.inject(this)
        setContentView(R.layout.course_info_activity)
        if (savedInstanceState == null) {
            val token = authHolder.getToken()
            val url = intent?.extras?.getString("url")
            if (url != null && token != null) {
                webView.loadUrl(url, mapOf(Pair("Cookie", token)))
            }
        }
    }
}