package xyz.mperminov.tfscoursework.fragments.courses.tasks


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_viewholder.view.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.repositories.models.Task
import java.util.*

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    private var data: List<Task> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.task_viewholder, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) = holder.bind(data[position])

    fun swapData(data: List<Task>) {
        this.data = data
        notifyDataSetChanged()
    }

    class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Task) = with(itemView) {
            task_title.text = item.task.title
            marks.text = String.format(MARKS_TEMPLATE, item.mark, item.task.max_score)
            if (item.task.deadline_date != null)
            //TODO время
                deadline.text = String.format(
                    DEADLINE_TEMPLATE,
                    itemView.context.getString(R.string.send_before),
                    item.task.deadline_date!!.take(CHARS_FOR_DATE)
                )
            if (item.status == "accepted") {
                task_state.text = itemView.context.getString(R.string.passed)
            } else {
                task_state.setColor(R.color.colorPrimary)
                task_state.text = itemView.context.getString(R.string.not_passed)
            }
        }

        companion object {
            const val MARKS_TEMPLATE = "%s/%s"
            const val DEADLINE_TEMPLATE = "%s: %s"
            const val CHARS_FOR_DATE = 10
        }

    }

}