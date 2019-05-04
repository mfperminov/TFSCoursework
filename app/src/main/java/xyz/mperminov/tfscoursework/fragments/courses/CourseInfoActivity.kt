package xyz.mperminov.tfscoursework.fragments.courses

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.course_info_activity.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.network.AuthHolder
import javax.inject.Inject

class CourseInfoActivity : AppCompatActivity() {
    @Inject
    lateinit var authHolder: AuthHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TFSCourseWorkApp.studentComponent.inject(this)
        setContentView(R.layout.course_info_activity)
        val title = intent?.extras?.getString("descr")
        supportActionBar?.title = title
        if (savedInstanceState == null) {
            val token = authHolder.getToken()
            val url = intent?.extras?.getString("url")
            if (url != null && token != null) {
                webView.loadUrl(url, mapOf(Pair("Cookie", token)))
            }
        }
    }
}