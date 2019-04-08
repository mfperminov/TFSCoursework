package xyz.mperminov.tfscoursework.fragments.courses

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_rating.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.fragments.base.ChildFragmentsAdder
import xyz.mperminov.tfscoursework.fragments.courses.lectures.LecturesFragment

class RatingFragment : Fragment() {

    private var childFragmentsAdder: ChildFragmentsAdder? = null

    override fun onAttach(context: Context) {
        if (context is ChildFragmentsAdder) childFragmentsAdder = context else
            throw IllegalStateException("$context must implement ChildFragmentsAdder interface")
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setProgress(10, 20)
        header_layout.setOnClickListener { childFragmentsAdder?.addChildOnTop(LecturesFragment.newInstance()) }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDetach() {
        childFragmentsAdder = null
        super.onDetach()
    }

    private fun setProgress(passedClasses: Int, totalClasses: Int) {
        class_progressbar.progress = ((passedClasses.toFloat() / totalClasses.toFloat()) * 100).toInt()
        total_classes.text = getString(R.string.classes_count, totalClasses)
        classes_past.text = getString(R.string.classes_count, passedClasses)
        classes_left.text = getString(R.string.classes_count, totalClasses - passedClasses)
    }
}
