package xyz.mperminov.tfscoursework.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
    })
}

fun EditText.validate(validator: (String) -> Boolean,textInputLayout: TextInputLayout, message: String) {
    this.afterTextChanged {
        textInputLayout.error = if (validator(it)) null else message
    }
    textInputLayout.error = if (validator(this.text.toString())) null else message
}

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()