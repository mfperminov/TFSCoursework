package xyz.mperminov.tfscoursework.fragments.courses.lectures

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_lectures.*
import xyz.mperminov.tfscoursework.R
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.fragments.base.BaseChildFragment
import xyz.mperminov.tfscoursework.fragments.base.ChildFragmentsAdder
import xyz.mperminov.tfscoursework.fragments.base.ToolbarTitleSetter
import xyz.mperminov.tfscoursework.fragments.courses.tasks.TasksFragment
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.lectures.HomeworksRepository
import xyz.mperminov.tfscoursework.repositories.lectures.db.LectureDao
import xyz.mperminov.tfscoursework.repositories.lectures.db.TasksDao
import xyz.mperminov.tfscoursework.repositories.lectures.network.HomeworksNetworkRepository
import xyz.mperminov.tfscoursework.repositories.models.Lectures
import xyz.mperminov.tfscoursework.repositories.models.Task
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository
import xyz.mperminov.tfscoursework.utils.toast

class LecturesFragment : BaseChildFragment(), UserNetworkRepository.TokenProvider {
    companion object {
        fun newInstance(): LecturesFragment {
            return LecturesFragment()
        }
    }

    private val disposables = CompositeDisposable()
    private val lectureDao: LectureDao = TFSCourseWorkApp.database.lectureDao()
    private val tasksDao: TasksDao = TFSCourseWorkApp.database.tasksDao()
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var layoutAdapter: LectureAdapter
    private val repository: HomeworksRepository by lazy {
        HomeworksNetworkRepository(getToken()!!)
    }
    private var childFragmentsAdder: ChildFragmentsAdder? = null
    override fun onAttach(context: Context) {
        if (context is ChildFragmentsAdder) childFragmentsAdder = context else
            throw IllegalStateException("$context must implement ChildFragmentsAdder interface")
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lectures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as ToolbarTitleSetter).setTitle(getString(R.string.lectures))
        layoutManager = LinearLayoutManager(context)
        layoutAdapter = LectureAdapter { lectureId -> goToTasksFragment(lectureId) }
        rv_lectures.adapter = layoutAdapter
        rv_lectures.layoutManager = layoutManager
        swipe_layout.setOnRefreshListener { updateDb() }
    }

    override fun onStart() {
        val d = lectureDao.getCount().subscribeOn(Schedulers.io())
            .subscribe { count -> if (count > 0) showLectures() else fillDb() }
        disposables.add(d)
        super.onStart()
    }

    private fun goToTasksFragment(lectureId: Int) {
        childFragmentsAdder?.addChildOnTop(TasksFragment.newInstance(lectureId))
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
        disposables.add(d)
    }

    private fun fillDb() {
        val d = repository.getLectures().observeOn(Schedulers.io())
            .flatMapCompletable { lectures ->
                lectureDao.insertAll(lectures.lectures)
                    .andThen(tasksDao.saveHomeworks(mapLecturesToTasks(lectures)))
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ showLectures() }, { error -> showError(error.localizedMessage) })
        disposables.add(d)
    }

    private fun updateDb() {
        val d = repository.getLectures().take(1).observeOn(Schedulers.io())
            .flatMapCompletable { lectures ->
                lectureDao.deleteAll()
                    .andThen(tasksDao.deleteAll())
                    .andThen(lectureDao.insertAll(lectures.lectures))
                    .andThen(tasksDao.saveHomeworks(mapLecturesToTasks(lectures)))
            }.observeOn(AndroidSchedulers.mainThread())
            .subscribe({ stopRefresh(); showLectures() },
                { error -> stopRefresh(); showError(error.localizedMessage) })
        disposables.add(d)
    }

    private fun stopRefresh() {
        if (swipe_layout.isRefreshing) swipe_layout.isRefreshing = false
    }

    private fun showError(message: String?) {
        if (message != null)
            context?.toast(message)
    }

    private fun mapLecturesToTasks(lectures: Lectures): List<Task> {
        val tasks = mutableListOf<Task>()
        lectures.lectures.forEach { lecture ->
            lecture.tasks.forEach { task ->
                tasks.add(
                    Task(
                        lecture.id,
                        task.id,
                        task.mark,
                        task.status,
                        task.task
                    )
                )
            }
        }
        return tasks.toList()
    }

    override fun onStop() {
        stopRefresh()
        disposables.clear()
        super.onStop()
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
