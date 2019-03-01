package xyz.mperminov.tfscoursework.fragments.base

import androidx.fragment.app.Fragment

abstract class BaseChildFragment: Fragment() {

    abstract fun handleBackPress()
}