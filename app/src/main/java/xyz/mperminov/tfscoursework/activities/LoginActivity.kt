package xyz.mperminov.tfscoursework.activities

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.network.AuthHolder

class LoginActivity : Activity(), AuthHolder.Callback, AuthHolder.PrefsProvider {

    private lateinit var authHolder: AuthHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin(email.text.toString(), password.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
        authHolder = AuthHolder(this, this)
        sign_in_button.setOnClickListener { attemptLogin(email.text.toString(), password.text.toString()) }
    }

    private fun attemptLogin(email: String, password: String) {
        authHolder.updateToken(email, password).subscribeOn(Schedulers.io()).subscribe()
    }

    override fun onAuthSuccess() {
        Toast.makeText(this, "Auth success", Toast.LENGTH_SHORT).show()
        proceedToMainActivity()
    }

    private fun proceedToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onAuthFailure() {
        Toast.makeText(this, "Auth success", Toast.LENGTH_SHORT).show()
    }

    override fun getPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(this)
    }
}
