package xyz.mperminov.tfscoursework.repositories.students.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StudentSchema {
    @SerializedName("grouped_tasks")
    @Expose
    var groupedTasks: List<List<GroupedTask>>? = null
    @SerializedName("grades")
    @Expose
    var grades: List<Grade>? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("mentor")
    @Expose
    var mentor: Any? = null
    @SerializedName("mentor_id")
    @Expose
    var mentorId: Any? = null
    @SerializedName("id")
    @Expose
    var id: Int? = null
}

class Grade {
    @SerializedName("student")
    @Expose
    var student: String? = null
    @SerializedName("student_id")
    @Expose
    var studentId: Int? = null
    @SerializedName("grades")
    @Expose
    var grades: List<Grade_>? = null
    @SerializedName("group_id")
    @Expose
    var groupId: Int? = null

    override fun toString(): String {
        return "Student $student with id#$studentId has ${grades?.last()?.mark}"
    }
}

class Grade_ {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("mark")
    @Expose
    var mark: Double? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("task_type")
    @Expose
    var taskType: String? = null
}

class GroupedTask {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("short_name")
    @Expose
    var shortName: String? = null
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("max_score")
    @Expose
    var maxScore: Double? = null
    @SerializedName("contest__status")
    @Expose
    var contestStatus: Int? = null
}