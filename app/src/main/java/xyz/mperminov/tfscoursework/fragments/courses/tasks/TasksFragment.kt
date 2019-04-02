package xyz.mperminov.tfscoursework.fragments.courses.tasks

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_tasks.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.fragments.base.BaseChildFragment
import xyz.mperminov.tfscoursework.fragments.base.ChildFragmentsAdder
import xyz.mperminov.tfscoursework.fragments.base.ToolbarTitleSetter
import xyz.mperminov.tfscoursework.repositories.lectures.db.HomeworkDatabase
import xyz.mperminov.tfscoursework.repositories.lectures.db.TasksDao

class TasksFragment : BaseChildFragment() {
    companion object {

        fun newInstance(lectureId: Int): TasksFragment {
            val args: Bundle = Bundle()
            args.putInt(ARG_LECTURE_ID, lectureId)
            val fragment = TasksFragment()
            fragment.arguments = args
            return fragment
        }

        const val ARG_LECTURE_ID = "lectureId"

    }

    private lateinit var database: HomeworkDatabase
    private lateinit var tasksDao: TasksDao
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var layoutAdapter: TasksAdapter
    private var getTasksDisposable: Disposable? = null
    private var childFragmentsAdder: ChildFragmentsAdder? = null

    override fun onAttach(context: Context) {
        if (context is ChildFragmentsAdder) childFragmentsAdder = context else
            throw IllegalStateException("$context must implement ChildFragmentsAdder interface")
        database = Room.databaseBuilder(
            context.applicationContext,
            HomeworkDatabase::class.java, "lectures.db"
        )
            .build()
        tasksDao = database.tasksDao()
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ToolbarTitleSetter).setTitle(getString(R.string.tasks))
        layoutManager = LinearLayoutManager(context)
        layoutAdapter = TasksAdapter()
        rv_tasks.adapter = layoutAdapter
        rv_tasks.layoutManager = layoutManager
        showTasksFromLecture(arguments?.getInt(ARG_LECTURE_ID) ?: 0)


    }

    private fun showTasksFromLecture(lectureId: Int) {
        getTasksDisposable = tasksDao.getHomeworkByLectureId(lectureId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it -> layoutAdapter.swapData(it); Log.d("items", it.joinToString()) }
    }

    override fun onDestroyView() {
        getTasksDisposable?.dispose()
        super.onDestroyView()
    }

    override fun onDetach() {
        childFragmentsAdder = null
        super.onDetach()
    }

    override fun handleBackPress() {
        childFragmentsAdder?.onBackPressHandled()
    }
}

