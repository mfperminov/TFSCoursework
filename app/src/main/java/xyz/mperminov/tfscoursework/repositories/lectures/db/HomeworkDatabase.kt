package xyz.mperminov.tfscoursework.repositories.lectures.db

import androidx.room.Database
import androidx.room.RoomDatabase
import xyz.mperminov.tfscoursework.repositories.models.Lecture
import xyz.mperminov.tfscoursework.repositories.models.Task
import xyz.mperminov.tfscoursework.repositories.students.db.Student
import xyz.mperminov.tfscoursework.repositories.students.db.StudentsDao

@Database(entities = [Lecture::class, Task::class, Student::class], version = 2, exportSchema = false)
abstract class HomeworkDatabase : RoomDatabase() {
    abstract fun lectureDao(): LectureDao
    abstract fun tasksDao(): TasksDao
    abstract fun studentsDao(): StudentsDao
}