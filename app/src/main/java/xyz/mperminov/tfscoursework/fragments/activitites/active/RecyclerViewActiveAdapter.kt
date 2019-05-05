package xyz.mperminov.tfscoursework.fragments.activitites.active

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.active_viewholder.view.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.repositories.activities.Active
import java.text.SimpleDateFormat
import java.util.*

class RecyclerViewActiveAdapter : RecyclerView.Adapter<RecyclerViewActiveAdapter.ActiveViewHolder>() {
    private var data: List<Active> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveViewHolder {
        return ActiveViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.active_viewholder, parent, false)
        )
    }

    override fun getItemCount() = data.size
    override fun onBindViewHolder(holder: ActiveViewHolder, position: Int) = holder.bind(data[position])
    fun swapData(data: List<Active>) {
        this.data = data
        notifyDataSetChanged()
    }

    class ActiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Active) {
            itemView.active_title.text = item.title
            itemView.active_date.text = SDF.format(item.dateStart)
            itemView.active_label.text = itemView.context.getString(item.eventType.stringResid)
            itemView.active_label.setBackgroundResource(item.eventType.colorResId)

        }
    }

    companion object {
        private val SDF = SimpleDateFormat("MMM yyyy")
    }
}