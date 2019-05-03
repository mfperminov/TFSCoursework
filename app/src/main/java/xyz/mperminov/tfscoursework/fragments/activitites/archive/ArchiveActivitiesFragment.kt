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
            when (result) {
                is Result.Success<Archive> -> {
                    (rv_past_activities.adapter as RecyclerViewArchiveAdapter).swapData(result.data)
                    updateHeader(result.data.size)
                }
                is Result.Error<Archive> -> context?.toast(result.e!!.localizedMessage)
                is Result.Empty<Archive> -> context?.toast("Empty")
                is Result.Loading<Archive> -> context?.toast("Loading")
            }
        })
    }

    private fun updateHeader(size: Int) {
        caption_details.text = getString(R.string.activities_count, size)
    }

    private fun initRecyclerView() {
        rv_past_activities.layoutManager = LinearLayoutManager(context)
        val adapter = RecyclerViewArchiveAdapter()
        rv_past_activities.adapter = adapter
    }
}