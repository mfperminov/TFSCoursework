package xyz.mperminov.tfscoursework.fragments.courses

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_rating.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.base.ChildFragmentsAdder
import xyz.mperminov.tfscoursework.fragments.courses.lectures.LecturesFragment

class RatingFragment : Fragment() {
    private lateinit var viewModel: RatingViewModel
    private lateinit var progressViewModel: ProgressViewModel

    private var childFragmentsAdder: ChildFragmentsAdder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.application as TFSCourseWorkApp).initLecturesComponent()
        viewModel = ViewModelProviders.of(this).get(RatingViewModel::class.java)
        progressViewModel = ViewModelProviders.of(this).get(ProgressViewModel::class.java)
        if (savedInstanceState == null) {
            progressViewModel.getUserProgress()
            viewModel.getTestsRating()
            viewModel.getHomeworkRating()
        }
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        if (context is ChildFragmentsAdder) childFragmentsAdder = context else
            throw IllegalStateException("$context must implement ChildFragmentsAdder interface")
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rating, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViewModels()
        header_layout.setOnClickListener { childFragmentsAdder?.addChildOnTop(LecturesFragment.newInstance()) }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupViewModels() {
        viewModel.testLiveData.observe(this, Observer { testRating -> updateTestInfo(testRating) })
        viewModel.homeworkLiveData.observe(this, Observer { homeworkRating -> updateHomeworkInfo(homeworkRating) })
        viewModel.lecturesCount.observe(this, Observer { count -> updateLecturesCount(count) })
        progressViewModel.userRating.observe(this, Observer { userRating -> updateUserRating(userRating) })
        progressViewModel.userMark.observe(this, Observer { mark -> updateUserMark(mark) })
    }

    private fun updateUserMark(mark: Int?) {
        if (mark != null)
            points.text = mark.toString()
    }

    private fun updateUserRating(userRating: ratingOverall?) {
        if (userRating != null) {
            common_rating.text = "${userRating.first}/${userRating.second}"
        }
    }

    private fun updateLecturesCount(count: Int?) {
        if (count != null)
            total_classes.text = getString(R.string.classes_count, count)
    }

    private fun updateHomeworkInfo(homeworkRating: ratingOverall?) {
        if (homeworkRating != null)
            homework_rating.text = "${homeworkRating.first}/${homeworkRating.second}"
    }

    private fun updateTestInfo(testRating: ratingOverall?) {
        if (testRating != null)
            tests_rating.text = "${testRating.first}/${testRating.second}"
    }

    override fun onDetach() {
        childFragmentsAdder = null
        super.onDetach()
    }
}
