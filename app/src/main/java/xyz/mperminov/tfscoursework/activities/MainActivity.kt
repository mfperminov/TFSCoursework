package xyz.mperminov.tfscoursework.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import kotlinx.android.synthetic.main.activity_main.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.activitites.ActivitiesFragment
import xyz.mperminov.tfscoursework.fragments.base.BaseChildFragment
import xyz.mperminov.tfscoursework.fragments.base.ChildFragmentsAdder
import xyz.mperminov.tfscoursework.fragments.base.ToolbarTitleSetter
import xyz.mperminov.tfscoursework.fragments.courses.CoursesFragment
import xyz.mperminov.tfscoursework.fragments.profile.ProfileFragment
import xyz.mperminov.tfscoursework.repositories.UserRepository

class MainActivity : AppCompatActivity(), AHBottomNavigation.OnTabSelectedListener, ChildFragmentsAdder,
    ToolbarTitleSetter {

    private val repository: UserRepository = TFSCourseWorkApp.repository!!

    //region Lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addItemsToBottomNav()
        nav.setOnTabSelectedListener(this)
        if (savedInstanceState == null) {
            onTabSelected(0, false)
        }
    }
    //endregion

    private fun addItemsToBottomNav() {
        with(nav) {
            addItem(
                AHBottomNavigationItem(
                    R.string.activities,
                    R.drawable.ic_local_activity,
                    R.color.color_tab_activity
                )
            )
            addItem(
                AHBottomNavigationItem(
                    R.string.courses,
                    R.drawable.ic_list,
                    R.color.color_tab_courses
                )
            )
            addItem(
                AHBottomNavigationItem(
                    R.string.profile,
                    R.drawable.ic_person,
                    R.color.color_tab_profile
                )
            )
        }
    }

    override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
        val tag = getTagOnPosition(position)
        val fragment =
            getFragmentOnPosition(position)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, tag)
            .commit()
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            val childFragment = supportFragmentManager.findFragmentByTag(getTagOnPosition(nav.currentItem))
            if (childFragment != null && childFragment.isVisible && childFragment is BaseChildFragment) {
                childFragment.handleBackPress()
                return
            } else
                supportFinishAfterTransition()
        } else
            super.onBackPressed()
    }

    //region ChildFragmentsAdder implementation
    override fun onBackPressHandled() {
        supportFragmentManager.popBackStack()
    }

    override fun addChildOnTop(childFragment: BaseChildFragment) {
        // здесь я добавляю чайлдовый фрагмент под тегом родителя, чтобы корректно обработать
        // onBackPressed()  - найти фрагмент в текущей открытой вкладке
        val tagCurrentFragment = getTagOnPosition(nav.currentItem)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, childFragment, tagCurrentFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun recreateParentFragment() {
        onTabSelected(nav.currentItem, false)
    }
    //endregion


    private fun getFragmentOnPosition(position: Int): Fragment {
        return when (position) {

            0 -> ActivitiesFragment.newInstance()

            1 -> CoursesFragment.newInstance()

            2 -> if (repository.getUser() == null)
                ProfileFragment.newInstance(
                    null,
                    getString(R.string.string_no_user)
                ) else ProfileFragment.newInstance(repository.getUser(), null)

            else -> {
                throw IllegalArgumentException("No fragment for position $position")
            }
        }

    }

    private fun getTagOnPosition(position: Int): String {
        return when (position) {
            0 -> ActivitiesFragment.TAG
            1 -> CoursesFragment.TAG
            2 -> ProfileFragment.TAG
            else -> {
                throw IllegalArgumentException("No fragment for position $position")
            }
        }

    }

    override fun setTitle(title: String) {
        supportActionBar?.title = title
    }

}

