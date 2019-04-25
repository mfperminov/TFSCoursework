package xyz.mperminov.tfscoursework.fragments.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.base.BaseChildFragment
import xyz.mperminov.tfscoursework.fragments.base.ChildFragmentsAdder
import xyz.mperminov.tfscoursework.fragments.base.ToolbarTitleSetter
import xyz.mperminov.tfscoursework.models.User
import xyz.mperminov.tfscoursework.repositories.user.UserRepository
import xyz.mperminov.tfscoursework.utils.afterTextChanged
import xyz.mperminov.tfscoursework.utils.validate
import javax.inject.Inject

class EditProfileFragment : BaseChildFragment() {
    @Inject
    lateinit var repository: UserRepository
    private var hasChanges = false

    private lateinit var user: User

    companion object {
        const val ARG_USER = "user"
        fun newInstance(user: User): EditProfileFragment {
            val args = Bundle()
            args.putParcelable(ARG_USER, user)
            val fragment = EditProfileFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TFSCourseWorkApp.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        user = arguments?.getParcelable(ARG_USER) as User
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as ToolbarTitleSetter).setTitle(getString(R.string.edit_profile))
        initForms()
        btnSave.setOnClickListener {
            onSaveClicked()
        }
        btnCancel.setOnClickListener {
            handleBackPress()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun onSaveClicked() {
        if (lastNameLayout.error.isNullOrEmpty()
            && firstNameLayout.error.isNullOrEmpty()
            && patronymicLayout.error.isNullOrEmpty()
        ) {

            repository.saveUser(
                User(
                    lastName.text.toString().capitalize(),
                    firstName.text.toString().capitalize(),
                    patronymic.text.toString().capitalize(),
                    null
                )
            )
            (activity as ChildFragmentsAdder).recreateParentFragment()

        }
    }

    private fun initForms() {
        lastName.setText(user.lastName)
        lastName.validate({ s -> !s.isEmpty() }, lastNameLayout, getString(R.string.last_name_error))
        lastName.afterTextChanged { hasChanges = true }
        firstName.setText(user.firstName)
        firstName.validate({ s -> !s.isEmpty() }, firstNameLayout, getString(R.string.first_name_error))
        firstName.afterTextChanged { hasChanges = true }
        patronymic.setText(user.patronymic)
        patronymic.validate({ s -> !s.isEmpty() }, patronymicLayout, getString(R.string.patronymic_error))
        patronymic.afterTextChanged { hasChanges = true }
    }

    override fun handleBackPress() {
        if (!hasChanges) {
            (activity as ChildFragmentsAdder).onBackPressHandled()
            return
        }
        val dialog = DialogConfirmLeavingEdit()
        dialog.show(childFragmentManager, null)
    }
}

class DialogConfirmLeavingEdit : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage(getString(R.string.message_dialog_leave_edit_profile))
        builder.setPositiveButton(R.string.leave) { _, _ ->
            (activity as ChildFragmentsAdder).onBackPressHandled()
        }
        builder.setNegativeButton(R.string.stay) { dialog, _ ->
            dialog.dismiss()
        }
        return builder.create()

    }
}