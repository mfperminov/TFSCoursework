package xyz.mperminov.tfscoursework.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.base.BaseChildFragment
import xyz.mperminov.tfscoursework.fragments.base.ChildFragmentsAdder
import xyz.mperminov.tfscoursework.fragments.base.ToolbarTitleSetter
import xyz.mperminov.tfscoursework.models.User
import xyz.mperminov.tfscoursework.repositories.UserRepository
import xyz.mperminov.tfscoursework.utils.onChange
import xyz.mperminov.tfscoursework.utils.validate

class EditProfileFragment : BaseChildFragment() {
    private val repository: UserRepository = TFSCourseWorkApp.repository!!
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as ToolbarTitleSetter).setTitle(getString(R.string.edit_profile))
        user = arguments?.getParcelable(ARG_USER) as User
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        inflateForms(view)
        view.btnSave.setOnClickListener {
            onSaveClicked(view)
        }
        view.btnCancel.setOnClickListener {
            handleBackPress()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun onSaveClicked(view: View) {
        if (view.lastName.error.isNullOrEmpty()
            && view.firstName.error.isNullOrEmpty()
            && view.patronymic.error.isNullOrEmpty()
        ) {

            repository.saveUser(
                User(
                    view.lastName.text.toString().capitalize(),
                    view.firstName.text.toString().capitalize(),
                    view.patronymic.text.toString().capitalize()
                )
            )
            (activity as ChildFragmentsAdder).recreateParentFragment()

        }
    }

    private fun inflateForms(view: View) {
        view.lastName.setText(user.lastName)
        view.lastName.validate({ s -> !s.isEmpty() }, view.lastNameLayout, getString(R.string.last_name_error))
        view.lastName.onChange { hasChanges = true }
        view.firstName.setText(user.firstName)
        view.firstName.validate({ s -> !s.isEmpty() }, view.firstNameLayout, getString(R.string.first_name_error))
        view.firstName.onChange { hasChanges = true }
        view.patronymic.setText(user.patronymic)
        view.patronymic.validate({ s -> !s.isEmpty() }, view.patronymicLayout, getString(R.string.patronymic_error))
        view.patronymic.onChange { hasChanges = true }
    }

    override fun handleBackPress() {
        if (!hasChanges) {
            (activity as ChildFragmentsAdder).onBackPressHandled()
            return
        }
        val builder = AlertDialog.Builder(context!!)
        builder.setMessage(getString(R.string.message_dialog_leave_edit_profile))
        builder.setPositiveButton(R.string.leave) { _, _ ->
            (activity as ChildFragmentsAdder).onBackPressHandled()
        }
        builder.setNegativeButton(R.string.stay) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}