package xyz.mperminov.tfscoursework.fragments.base

interface ChildFragmentsAdder {
    fun addChildOnTop(childFragment: BaseChildFragment)

    fun onBackPressHandled()

    fun recreateParentFragment()
}