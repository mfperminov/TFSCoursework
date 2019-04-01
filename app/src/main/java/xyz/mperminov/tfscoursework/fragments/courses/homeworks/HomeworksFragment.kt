package xyz.mperminov.tfscoursework.fragments.courses.homeworks

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_lectures.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.fragments.base.BaseChildFragment
import xyz.mperminov.tfscoursework.fragments.base.ChildFragmentsAdder
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.homeworks.HomeworksRepository
import xyz.mperminov.tfscoursework.repositories.homeworks.db.HomeworkDatabase
import xyz.mperminov.tfscoursework.repositories.homeworks.db.LectureDao
import xyz.mperminov.tfscoursework.repositories.homeworks.db.TasksDao
import xyz.mperminov.tfscoursework.repositories.homeworks.network.HomeworksNetworkRepository
import xyz.mperminov.tfscoursework.repositories.homeworks.network.Lecture
import xyz.mperminov.tfscoursework.repositories.homeworks.network.Lectures
import xyz.mperminov.tfscoursework.repositories.homeworks.network.Task
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository

class HomeworksFragment : BaseChildFragment(), UserNetworkRepository.TokenProvider {

    companion object {
        fun newInstance(): HomeworksFragment {
            return HomeworksFragment()
        }
    }

    private val compositeDisposable = CompositeDisposable()
    private lateinit var database: HomeworkDatabase
    private lateinit var lectureDao: LectureDao
    private lateinit var tasksDao: TasksDao
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var layoutAdapter: LectureAdapter

    private val repository: HomeworksRepository by lazy {
        HomeworksNetworkRepository(getToken()!!)
    }
    private var childFragmentsAdder: ChildFragmentsAdder? = null

    override fun onAttach(context: Context) {
        if (context is ChildFragmentsAdder) childFragmentsAdder = context else
            throw IllegalStateException("$context must implement ChildFragmentsAdder interface")
        database = Room.databaseBuilder(
            context.applicationContext,
            HomeworkDatabase::class.java, "lectures.db"
        )
            .build()
        lectureDao = database.lectureDao()
        tasksDao = database.tasksDao()
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lectures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutManager = LinearLayoutManager(context)
        layoutAdapter = LectureAdapter { lectureId -> showTasksFromLecture(lectureId) }
        rv_lectures.adapter = layoutAdapter
        rv_lectures.layoutManager = layoutManager
        compositeDisposable.add(lectureDao.getCount().subscribeOn(Schedulers.io())
            .subscribe { count -> if (count > 0) showLectures() else updateDb() })

    }

    private fun showTasksFromLecture(lectureId: Int) {
        tasksDao.getHomeworkByLectureId(lectureId).subscribeOn(Schedulers.io())
            .subscribe { it -> Log.d("tasks", it.joinToString()) }
    }

    private fun showLectures() {
        val d = lectureDao.getLectures().subscribeOn(Schedulers.io())
            .map {
                it.map { lecture ->
                    LectureModelView(
                        lecture.id,
                        lecture.title
                    )
                }
            }.observeOn(AndroidSchedulers.mainThread()).subscribe {
                layoutAdapter.swapData(it)
            }
        compositeDisposable.add(d)
    }

    private fun updateDb() {
        val d = repository.getLectures().observeOn(Schedulers.io()).doOnNext { saveCurrentLectures(it.lectures) }
            .flatMapCompletable { lectures ->
                database.lectureDao().insertAll(lectures.lectures)
                    .andThen(database.tasksDao().saveHomeworks(mapLecturesToTasks(lectures)))
            }
            .subscribe({ showLectures() }, { error -> Log.e("Error", error.localizedMessage) })
        compositeDisposable.add(d)
    }

    private fun mapLecturesToTasks(lectures: Lectures): List<Task> {
        val tasks = mutableListOf<Task>()
        lectures.lectures.forEach { lecture ->
            lecture.tasks.forEach { task -> tasks.add(Task(lecture.id, task.id, task.mark, task.status, task.task)) }
        }
        Log.d("tasks", tasks.joinToString())
        return tasks.toList()

    }

    private fun saveCurrentLectures(lectures: List<Lecture>) {
    }

    override fun onDetach() {
        childFragmentsAdder = null
        super.onDetach()
    }

    override fun handleBackPress() {
        childFragmentsAdder?.recreateParentFragment()
    }

    override fun getToken(): String? {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(AuthHolder.AUTH_TOKEN_ARG, null)
    }
}

data class LectureModelView(val id: Int, val title: String)
