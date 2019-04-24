package xyz.mperminov.tfscoursework.fragments.students.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.AuthHolder
import xyz.mperminov.tfscoursework.repositories.lectures.db.HomeworkDatabase
import xyz.mperminov.tfscoursework.repositories.students.StudentsRepository
import xyz.mperminov.tfscoursework.repositories.students.UpdateTimeSaverImpl
import xyz.mperminov.tfscoursework.repositories.students.db.StudentMapper
import xyz.mperminov.tfscoursework.repositories.students.network.NetworkStudentsRepository

@Module
object StudentModule {
    @Provides
    fun updateTimeSaver(sharedPreferences: SharedPreferences): StudentsRepository.UpdateTimeSaver =
        UpdateTimeSaverImpl(sharedPreferences)

    @Provides
    @StudentScope
    fun networkRepository(authHolder: AuthHolder, api: Api, mapper: StudentMapper): NetworkStudentsRepository =
        NetworkStudentsRepository(authHolder, api, mapper)

    @Provides
    @StudentScope
    fun repository(
        database: HomeworkDatabase,
        updateTimeSaver: StudentsRepository.UpdateTimeSaver
    ): StudentsRepository =
        StudentsRepository(database, updateTimeSaver)

    @Provides
    fun mapper(): StudentMapper = StudentMapper()
}