package xyz.mperminov.tfscoursework.fragments.courses.homeworks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.lecture_viewholder.view.*
import xyz.mperminov.tfscoursework.R
import java.util.*

class LectureAdapter(private val clickListener: (Int) -> Unit) :
    RecyclerView.Adapter<LectureAdapter.LectureViewHolder>() {
    private var data: List<LectureModelView> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        return LectureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.lecture_viewholder, parent, false), clickListener
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        holder.bind(data[position])
    }

    fun swapData(data: List<LectureModelView>) {
        this.data = data
        notifyDataSetChanged()
    }

    class LectureViewHolder(itemView: View, val clickListener: (Int) -> Unit) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: LectureModelView) {
            itemView.lecture_title.text = item.title
            itemView.setOnClickListener { clickListener(item.id) }
        }
    }
}