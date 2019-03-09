package xyz.mperminov.tfscoursework.fragments.courses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.course_info_viewholder.view.*
import kotlinx.android.synthetic.main.fragment_passed_courses.*
import xyz.mperminov.tfscoursework.R


class PassedCoursesFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_passed_courses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        rv.layoutManager = LinearLayoutManager(context)
        val adapter = PassedCoursesAdapter()
        rv.adapter = adapter
        adapter.data = mutableListOf(
            PassedCourse("ДЕК. 2018 Г", "Курс 1. iOs разработка", "315 баллов", false),
            PassedCourse("МАР. 2018 Г", "Курс 1. Android разработка", "267 баллов", true)
        )
        val dividerItemDecoration = DividerItemDecoration(
            rv.context,
            LinearLayoutManager.VERTICAL
        )
        rv.addItemDecoration(dividerItemDecoration)
    }
}


data class PassedCourse(val date: String, val name: String, val points: String, val done: Boolean)
class PassedCoursesAdapter : RecyclerView.Adapter<PassedCoursesAdapter.ViewHolder>() {

    var data: MutableList<PassedCourse> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.course_info_viewholder,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount() = data.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PassedCourse) = with(itemView) {
            course_date.text = item.date
            course_name.text = item.name
            course_points.text = item.points
            course_state.text =
                    if (item.done) context.getString(R.string.passed) else context.getString(R.string.not_passed)
            if (item.done) course_state.setColor(R.color.color_course_done)
            else course_state.setColor(R.color.colorPrimary)
        }
    }
}