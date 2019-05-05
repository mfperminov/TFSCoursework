package xyz.mperminov.tfscoursework.fragments.activitites.active

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.active_activities.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.fragments.activitites.archive.Result
import xyz.mperminov.tfscoursework.repositories.activities.Active
import xyz.mperminov.tfscoursework.utils.toast

class ActiveFragment : Fragment() {
    private lateinit var viewModel: ActiveViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.active_activities, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ActiveViewModel::class.java)
        initRecyclerView()
        if (savedInstanceState == null) {
            viewModel.getActivities()
        }
        viewModel.activitiesLiveData.observe(this, Observer { result ->
            invalidateState()
            when (result) {
                is Result.Success<Active> -> {
                    (rv_active_activities.adapter as RecyclerViewActiveAdapter).swapData(result.data)
                    updateHeader(result.data.size)
                }
                is Result.Error<Active> -> showError(result.e!!)
                is Result.Empty<Active> -> showEmptyState()
                is Result.Loading<Active> -> showProgress()
            }
        })
        swipe_refresh_active.setOnRefreshListener { viewModel.getActivities() }
    }

    private fun showEmptyState() {
        context?.toast(getString(R.string.no_past_events))
        rv_active_activities.visibility = View.GONE
        empty_state_active.visibility = View.VISIBLE
    }

    private fun invalidateState() {
        swipe_refresh_active.isRefreshing = false
        caption_details_active.text = ""
        rv_active_activities.visibility = View.VISIBLE
        empty_state_active.visibility = View.GONE
    }

    private fun showError(e: Throwable) {
        context?.toast(e.localizedMessage)
    }

    private fun showProgress() {
        swipe_refresh_active.isRefreshing = true
    }

    private fun updateHeader(size: Int) {
        caption_details_active.text = getString(R.string.activities_count, size)
    }

    private fun initRecyclerView() {
        rv_active_activities.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = RecyclerViewActiveAdapter()
        rv_active_activities.adapter = adapter
    }
}