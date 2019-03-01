package xyz.mperminov.tfscoursework.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.fragments.base.ChildFragmentsAdder
import xyz.mperminov.tfscoursework.fragments.base.ToolbarTitleSetter
import xyz.mperminov.tfscoursework.models.User

class ProfileFragment : Fragment() {
    private var user: User? = null

    companion object {
        private const val ARG_USER = "user"
        private const val ARG_STRING_NO_USER = "user"
        fun newInstance(user: User?, stringNoUser: String?): ProfileFragment {
            val args = Bundle()
            if (user != null)
                args.putParcelable(ARG_USER, user)
            else
                args.putString(ARG_STRING_NO_USER, stringNoUser)
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
        if (arguments?.getParcelable<User>(EditProfileFragment.ARG_USER) != null) {
            user = arguments?.getParcelable(EditProfileFragment.ARG_USER) as User
        }
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as ToolbarTitleSetter).setTitle(getString(R.string.profile))
        user_info.text = if (user!=null) user.toString() else arguments?.getString(ARG_STRING_NO_USER)
        btn_edit.setOnClickListener {
            val editProfileFragment = EditProfileFragment.newInstance(user ?: User("","",""))
            (activity as ChildFragmentsAdder).addChildOnTop(editProfileFragment)
        }
        super.onViewCreated(view, savedInstanceState)
    }
}