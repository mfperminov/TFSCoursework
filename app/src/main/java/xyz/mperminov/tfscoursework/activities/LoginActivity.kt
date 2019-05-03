package xyz.mperminov.tfscoursework.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_login.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.utils.toast
import xyz.mperminov.tfscoursework.utils.validate

class LoginActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        email.validate(
            { s: String -> !s.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(s).matches() },
            email,
            getString(R.string.please_correct_email)
        )
        password.validate({ s -> !s.isNullOrEmpty() }, password, getString(R.string.please_correct_password))
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.tokenStatus.observe(this, Observer { isTokenValid -> if (isTokenValid) proceedToMainActivity() })
        viewModel.response.observe(this, Observer { response -> processResponse(response) })
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if ((id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) && validateFileds()) {
                viewModel.attemptToLogin(email.text.toString(), password.text.toString())
                return@OnEditorActionListener true
            }
            false
        })
        sign_in_button.setOnClickListener {
            if (validateFileds())
                viewModel.attemptToLogin(
                    email.text.toString(),
                    password.text.toString()
                )
            else
                return@setOnClickListener
        }
    }

    private fun validateFileds() = email.error.isNullOrEmpty() && password.error.isNullOrEmpty()
    private fun processResponse(response: Response) {
        when (response) {
            is Response.Error -> showFailure(response.error?.localizedMessage)
            is Response.Loading -> showProgress()
            is Response.Success -> showSuccess()
        }
    }

    private fun proceedToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showSuccess() {
        hideProgress()
        this.toast(getString(R.string.auth_success))
        proceedToMainActivity()
    }

    private fun showFailure(msg: String?) {
        hideProgress()
        this.toast(msg ?: getString(R.string.unknown_error))
    }

    private fun showProgress() {
        login_progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        login_progress.visibility = View.GONE
    }
}
