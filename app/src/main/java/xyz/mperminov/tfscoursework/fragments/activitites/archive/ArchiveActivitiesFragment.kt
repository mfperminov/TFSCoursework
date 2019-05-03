package xyz.mperminov.tfscoursework.fragments.activitites.archive

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.past_activitites.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.repositories.activities.Archive
import xyz.mperminov.tfscoursework.utils.toast

class ArchiveActivitiesFragment : Fragment() {
    private lateinit var viewModel: ArchiveActivitiesViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.past_activitites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ArchiveActivitiesViewModel::class.java)
        initRecyclerView()
        if (savedInstanceState == null) {
            viewModel.getActivities()
        }
        viewModel.activitiesLiveData.observe(this, Observer { result ->
            invalidateState()
            when (result) {
                is Result.Success<Archive> -> {
                    (rv_archive_activities.adapter as RecyclerViewArchiveAdapter).swapData(result.data)
                    updateHeader(result.data.size)
                }
                is Result.Error<Archive> -> showError(result.e!!)
                is Result.Empty<Archive> -> showEmptyState()
                is Result.Loading<Archive> -> showProgress()
            }
        })
        swipe_refresh_archive.setOnRefreshListener { viewModel.getActivities() }
    }

    private fun showEmptyState() {
        context?.toast(getString(R.string.no_past_events))
        rv_archive_activities.visibility = View.GONE
        empty_state_archive.visibility = View.VISIBLE
    }

    private fun invalidateState() {
        swipe_refresh_archive.isRefreshing = false
        caption_details.text = ""
        rv_archive_activities.visibility = View.VISIBLE
        empty_state_archive.visibility = View.GONE
    }

    private fun showError(e: Throwable) {
        context?.toast(e.localizedMessage)
    }

    private fun showProgress() {
        swipe_refresh_archive.isRefreshing = true
    }

    private fun updateHeader(size: Int) {
        caption_details.text = getString(R.string.activities_count, size)
    }

    private fun initRecyclerView() {
        rv_archive_activities.layoutManager = LinearLayoutManager(context)
        val adapter = RecyclerViewArchiveAdapter()
        rv_archive_activities.adapter = adapter
    }
}