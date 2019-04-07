package xyz.mperminov.tfscoursework.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_login.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.utils.toast

class LoginActivity : Activity(), AuthHolder.PrefsProvider {

    private lateinit var authHolder: AuthHolder
    private var authDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        authHolder = AuthHolder(this)
        if (authHolder.getToken() != null) proceedToMainActivity() //Todo check if token valid (expires?)
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin(email.text.toString(), password.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
        sign_in_button.setOnClickListener { attemptLogin(email.text.toString(), password.text.toString()) }
    }

    private fun attemptLogin(email: String, password: String) {
        showProgress()
        authDisposable = authHolder.updateToken(email, password)
            .subscribe({ onAuthSuccess() }, { t -> onAuthFailure(t.localizedMessage) })
    }

    override fun onStop() {
        super.onStop()
        authDisposable?.dispose()
    }

    private fun onAuthSuccess() {
        hideProgress()
        this.toast(getString(R.string.auth_success))
        proceedToMainActivity()
    }

    private fun proceedToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun onAuthFailure(msg: String) {
        hideProgress()
        this.toast(msg)
    }

    override fun getPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(this)
    }

    private fun showProgress() {
        login_progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        login_progress.visibility = View.GONE
    }
}
