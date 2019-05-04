package xyz.mperminov.tfscoursework.fragments.students.di

import dagger.Subcomponent
import xyz.mperminov.tfscoursework.fragments.courses.CourseInfoActivity
import xyz.mperminov.tfscoursework.fragments.courses.CoursesViewModel
import xyz.mperminov.tfscoursework.fragments.courses.ProgressViewModel
import xyz.mperminov.tfscoursework.fragments.students.StudentsViewModel
import xyz.mperminov.tfscoursework.repositories.students.StudentsRepository
import xyz.mperminov.tfscoursework.repositories.students.network.NetworkStudentsRepository
import javax.inject.Qualifier
import javax.inject.Scope

@StudentScope
@Subcomponent(
    modules = [StudentModule::class]
)
interface StudentComponent {
    fun inject(networkStudentsRepository: NetworkStudentsRepository)
    fun inject(repository: StudentsRepository)
    fun inject(viewModel: StudentsViewModel)
    fun inject(progressViewModel: ProgressViewModel)
    fun inject(coursesViewModel: CoursesViewModel)
    fun inject(courseInfoActivity: CourseInfoActivity)
    @Subcomponent.Builder
    interface Builder {
        fun build(): StudentComponent
        fun plus(studentsModule: StudentModule): Builder
    }
}

@Qualifier
@Retention(value = AnnotationRetention.RUNTIME)
@Scope
annotation class StudentScope