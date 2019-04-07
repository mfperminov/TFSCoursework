package xyz.mperminov.tfscoursework.repositories.lectures.db

import androidx.room.Database
import androidx.room.RoomDatabase
import xyz.mperminov.tfscoursework.repositories.lectures.network.Lecture
import xyz.mperminov.tfscoursework.repositories.lectures.network.Task

@Database(entities = [Lecture::class, Task::class], version = 1, exportSchema = false)
abstract class HomeworkDatabase : RoomDatabase() {

    abstract fun lectureDao(): LectureDao
    abstract fun tasksDao(): TasksDao

}