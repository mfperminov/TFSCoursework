package xyz.mperminov.tfscoursework.repositories.lectures.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import xyz.mperminov.tfscoursework.repositories.activities.Archive
import xyz.mperminov.tfscoursework.repositories.activities.ArchiveDao
import xyz.mperminov.tfscoursework.repositories.activities.DateTypeConverter
import xyz.mperminov.tfscoursework.repositories.activities.EventTypeConverter
import xyz.mperminov.tfscoursework.repositories.models.Lecture
import xyz.mperminov.tfscoursework.repositories.models.Task
import xyz.mperminov.tfscoursework.repositories.students.db.Student
import xyz.mperminov.tfscoursework.repositories.students.db.StudentsDao

@Database(entities = [Lecture::class, Task::class, Student::class, Archive::class], version = 3, exportSchema = false)
@TypeConverters(DateTypeConverter::class, EventTypeConverter::class)
abstract class HomeworkDatabase : RoomDatabase() {
    abstract fun lectureDao(): LectureDao
    abstract fun tasksDao(): TasksDao
    abstract fun studentsDao(): StudentsDao
    abstract fun archiveDao(): ArchiveDao
}