package xyz.mperminov.tfscoursework.repositories.models

import androidx.room.*
import com.google.gson.annotations.SerializedName


data class Lectures(
    @SerializedName("homeworks") val lectures: List<Lecture>
)

@Entity(tableName = "lectures")
data class Lecture(
    @PrimaryKey
    @ColumnInfo(name = "lecturesId")
    var id: Int = 0,
    @Ignore
    val tasks: List<Task> = listOf(),
    var title: String = ""
)

@Entity(tableName = "tasks")
data class Task(
    @ColumnInfo(name = "lecturesId")
    var lectureId: Int = 0,
    @PrimaryKey
    var id: Int = 0,
    var mark: String = "",
    var status: String = "",
    @Embedded
    var taskDetails: TaskDetails = TaskDetails()
)

data class TaskDetails(
    @Ignore
    var contest_info: Any = "",
    var deadline_date: String? = "",
    @ColumnInfo(name = "taskDetailsId")
    var id: Int = 0,
    var max_score: String = "",
    var short_name: String = "",
    var task_type: String = "",
    var title: String = ""
)