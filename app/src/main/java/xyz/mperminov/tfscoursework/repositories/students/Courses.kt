package xyz.mperminov.tfscoursework.repositories.students

data class ConnectionResponse(
    val active: List<Any>,
    val archive: List<Any>,
    val courses: List<CourseResponse>
)

data class CourseResponse(
    val diploma_url: String,
    val event_date_start: String,
    val is_teacher: Boolean,
    val pending_tasks: PendingTasks,
    val status: String,
    val title: String,
    val url: String
)

data class PendingTasks(
    val lecture_tests: List<Any>,
    val tasks: List<Any>
)
