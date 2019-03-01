package xyz.mperminov.tfscoursework.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_profile.view.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.fragments.base.ChildFragmentsAdder
import xyz.mperminov.tfscoursework.fragments.base.ToolbarTitleSetter
import xyz.mperminov.tfscoursework.models.User

class ProfileFragment: Fragment() {
    private lateinit var user: User

    companion object {
        private const val ARG_USER = "user"
        fun newInstance(user: User): ProfileFragment {
            val args= Bundle()
            args.putParcelable(ARG_USER, user)
            val fragment = ProfileFragment()
            fragment.arguments = args
            return fragment
        }

        const val TAG = "PROFILE_FRAGMENT"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as ToolbarTitleSetter).setTitle(getString(R.string.profile))
        if (arguments?.getParcelable<User>(EditProfileFragment.ARG_USER) != null) {
            user = arguments?.getParcelable(EditProfileFragment.ARG_USER) as User
        }
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.user_info.text = user.toString()
        view.btn_edit.setOnClickListener {
            val editProfileFragment = EditProfileFragment.newInstance(user)
            (activity as ChildFragmentsAdder).addChildOnTop(editProfileFragment)
        }
        super.onViewCreated(view, savedInstanceState)
    }
}