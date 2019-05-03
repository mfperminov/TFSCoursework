package xyz.mperminov.tfscoursework.fragments.activitites.archive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.past_activity_viewholder_small.view.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.repositories.activities.Archive
import java.util.*

class RecyclerViewArchiveAdapter : RecyclerView.Adapter<RecyclerViewArchiveAdapter.ArchiveViewModel>() {
    private var data: List<Archive> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArchiveViewModel {
        return ArchiveViewModel(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.past_activity_viewholder_small, parent, false)
        )
    }

    override fun getItemCount() = data.size
    override fun onBindViewHolder(holder: ArchiveViewModel, position: Int) = holder.bind(data[position])
    fun swapData(data: List<Archive>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ArchiveViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Archive) {
            itemView.activity_title.text = item.title
            itemView.activity_type_subtitle.text = itemView.context.getString(item.eventType.stringResid)
            itemView.activity_sign.setImageResource(item.eventType.imgResId)
        }
    }
}