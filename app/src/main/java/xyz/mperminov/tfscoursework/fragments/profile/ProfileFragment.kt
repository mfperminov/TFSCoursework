package xyz.mperminov.tfscoursework.fragments.profile

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.profile_header.view.*
import kotlinx.android.synthetic.main.profile_info.view.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.activities.LoginActivity
import xyz.mperminov.tfscoursework.fragments.base.ToolbarTitleSetter
import xyz.mperminov.tfscoursework.models.User
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.user.prefs.SharedPrefUserRepository
import xyz.mperminov.tfscoursework.utils.toast
import javax.inject.Inject

class ProfileFragment : Fragment() {
    @Inject
    lateinit var authHolder: AuthHolder
    private lateinit var viewModel: ProfileViewModel

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }

        const val TAG = "PROFILE_FRAGMENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TFSCourseWorkApp.userComponent.inject(this)
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
        btn_logout.setOnClickListener { returnToLogin() }
        setupViewModel()
        if (savedInstanceState == null)
            viewModel.getUser()
        swipe_refresh.setOnRefreshListener { invalidateView(); viewModel.getUser() }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun invalidateView() {
        info_container.removeAllViews()
    }

    private fun returnToLogin() {
        authHolder.removeToken()
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(SharedPrefUserRepository.ARG_USER, null)
            .apply()
        startActivity(Intent(context, LoginActivity::class.java))
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.user.observe(this@ProfileFragment, Observer { userResult ->
            when (userResult) {
                is UserResult.Success -> updateUi(userResult.user!!)
                is UserResult.Loading -> swipe_refresh.isRefreshing = true
            }
        })
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
            user_name.text = "${user.lastName} ${user.firstName}"
            user_email.text = user.email
            inflateInfo(user)
        } else {
            showError(getString(R.string.error_no_info))
        }
        swipe_refresh.isRefreshing = false
    }

    private fun inflateInfo(user: User) {
        inflateContactInformation(user)
        if (user.hasEducationInfo()) inflateEducationInfo(user)
        if (user.hasWorkInfo()) inflateWorkInfo(user)
    }

    private fun inflateContactInformation(user: User) {
        val header = View.inflate(context, R.layout.profile_header, null)
        header.info_section_text.text = getString(R.string.contact_info)
        header.info_section_sign.setImageResource(R.drawable.ic_contacts)
        val emailInfo = createInfoPiece(user.email, INFO_TYPE.EMAIL)
        header.section_info_container.addView(emailInfo)
        if (user.phone_mobile != null) {
            val phoneInfo = createInfoPiece(user.phone_mobile, INFO_TYPE.PHONE)
            header.section_info_container.addView(phoneInfo)
        }
        if (user.region != null) {
            val regionInfo = createInfoPiece(user.region, INFO_TYPE.REGION)
            header.section_info_container.addView(regionInfo)
        }
        info_container.addView(header)
    }

    private fun createInfoPiece(info: String, type: INFO_TYPE): View {
        val profileInfo = View.inflate(context, R.layout.profile_info, null)
        profileInfo.info_subtitle.text = when (type) {
            INFO_TYPE.EMAIL -> getString(R.string.email)
            INFO_TYPE.PHONE -> getString(R.string.phone)
            INFO_TYPE.REGION -> getString(R.string.region)
            INFO_TYPE.SCHOOL -> getString(R.string.school)
            INFO_TYPE.SCHOOL_END -> getString(R.string.school_graduation)
            INFO_TYPE.UNIVER -> getString(R.string.university)
            INFO_TYPE.UNIVER_END -> getString(R.string.university_graduation)
            INFO_TYPE.FACULTY -> getString(R.string.faculty)
            INFO_TYPE.DEPARTMENT -> getString(R.string.department)
            INFO_TYPE.WORK -> getString(R.string.work)
            INFO_TYPE.RESUME -> getString(R.string.resume)
        }
        profileInfo.info_title.text = info
        return profileInfo
    }

    private fun inflateEducationInfo(user: User) {
        val header = View.inflate(context, R.layout.profile_header, null)
        header.info_section_text.text = getString(R.string.education)
        header.info_section_sign.setImageResource(R.drawable.ic_education)
        if (user.school != null) {
            val schoolInfo = createInfoPiece(user.school, INFO_TYPE.SCHOOL)
            header.section_info_container.addView(schoolInfo)
        }
        if (user.school_graduation != null) {
            val schoolGradInfo = createInfoPiece(user.school_graduation.toString(), INFO_TYPE.SCHOOL_END)
            header.section_info_container.addView(schoolGradInfo)
        }
        if (user.university != null) {
            val universityInfo = createInfoPiece(user.university, INFO_TYPE.UNIVER)
            header.section_info_container.addView(universityInfo)
        }
        if (user.university_graduation != null) {
            val univerGradInfo = createInfoPiece(user.university_graduation.toString(), INFO_TYPE.UNIVER_END)
            header.section_info_container.addView(univerGradInfo)
        }
        if (user.faculty != null) {
            val facultyInfo = createInfoPiece(user.faculty, INFO_TYPE.FACULTY)
            header.section_info_container.addView(facultyInfo)
        }
        if (user.department != null) {
            val departmentInfo = createInfoPiece(user.department, INFO_TYPE.FACULTY)
            header.section_info_container.addView(departmentInfo)
        }
        info_container.addView(header)
    }

    private fun inflateWorkInfo(user: User) {
        val header = View.inflate(context, R.layout.profile_header, null)
        header.info_section_text.text = getString(R.string.work_header)
        header.info_section_sign.setImageResource(R.drawable.ic_work)
        if (user.current_work != null) {
            val workInfo = createInfoPiece(user.current_work, INFO_TYPE.WORK)
            header.section_info_container.addView(workInfo)
        }
        if (user.resume != null) {
            val resumeInfo = createInfoPiece(user.resume, INFO_TYPE.RESUME)
            header.section_info_container.addView(resumeInfo)
        }
        info_container.addView(header)
    }

    private fun setAvatarImage(avatarImage: Bitmap) {
        avatar.setImageBitmap(avatarImage)
    }
}

enum class INFO_TYPE { EMAIL, PHONE, REGION, SCHOOL, SCHOOL_END, UNIVER, UNIVER_END, FACULTY, DEPARTMENT, WORK, RESUME }