package xyz.mperminov.tfscoursework.fragments.profile

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_profile.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.base.ChildFragmentsAdder
import xyz.mperminov.tfscoursework.fragments.base.ToolbarTitleSetter
import xyz.mperminov.tfscoursework.models.User
import xyz.mperminov.tfscoursework.utils.toast

class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileViewModel

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }

        const val TAG = "PROFILE_FRAGMENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as TFSCourseWorkApp).initProfileComponent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as ToolbarTitleSetter).setTitle(getString(R.string.profile))
        btn_edit.setOnClickListener {
            val editProfileFragment = EditProfileFragment.newInstance(viewModel.user.value ?: User.NOBODY)
            (activity as ChildFragmentsAdder).addChildOnTop(editProfileFragment)
        }
        setupViewModel()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getUser()
        viewModel.user.observe(this@ProfileFragment, Observer { user -> updateUi(user) })
        viewModel.avatar.observe(
            this@ProfileFragment,
            Observer { avatarImage ->
                avatarImage.bitmap?.let { setAvatarImage(it) }
                avatarImage.e?.let {
                    showError(it.localizedMessage)
                }
            })
    }

    private fun showError(message: String) {
        context?.toast(message)
    }

    private fun updateUi(user: User) {
        if (user != User.NOBODY) {
            user_info.text = user.toString()
        } else {
            showError(getString(R.string.error_no_info))
        }
    }

    private fun setAvatarImage(avatarImage: Bitmap) {
        avatar.setImageBitmap(avatarImage)
    }
}