package xyz.mperminov.tfscoursework.fragments.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_rating.*
import xyz.mperminov.tfscoursework.R


class RatingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setProgress(10, 20)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setProgress(passedClasses: Int, totalClasses: Int) {
        class_progressbar.progress = ((passedClasses.toFloat() / totalClasses.toFloat()) * 100).toInt()
        total_classes.text = getString(R.string.classes_count, totalClasses)
        classes_past.text = getString(R.string.classes_count, passedClasses)
        classes_left.text = getString(R.string.classes_count, totalClasses - passedClasses)
    }
}
