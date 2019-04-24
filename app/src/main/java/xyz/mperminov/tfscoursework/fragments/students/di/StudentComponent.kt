package xyz.mperminov.tfscoursework.fragments.students.di

import dagger.Component
import xyz.mperminov.tfscoursework.di.AppComponent
import xyz.mperminov.tfscoursework.fragments.students.StudentsViewModel
import xyz.mperminov.tfscoursework.repositories.students.StudentsRepository
import xyz.mperminov.tfscoursework.repositories.students.network.NetworkStudentsRepository
import javax.inject.Qualifier
import javax.inject.Scope

@StudentScope
@Component(
    modules = [StudentModule::class],
    dependencies = [AppComponent::class]
)
interface StudentComponent {
    fun inject(networkStudentsRepository: NetworkStudentsRepository)
    fun inject(repository: StudentsRepository)
    fun inject(viewModel: StudentsViewModel)
    @Component.Builder
    interface Builder {
        fun build(): StudentComponent
        fun plus(studentsModule: StudentModule): Builder
        fun plus(appComponent: AppComponent): Builder
    }
}

@Qualifier
@Retention(value = AnnotationRetention.RUNTIME)
@Scope
annotation class StudentScope