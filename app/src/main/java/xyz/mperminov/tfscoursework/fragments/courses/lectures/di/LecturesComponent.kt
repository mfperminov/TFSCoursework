package xyz.mperminov.tfscoursework.fragments.courses.lectures.di

import dagger.Component
import xyz.mperminov.tfscoursework.di.AppComponent
import xyz.mperminov.tfscoursework.fragments.courses.RatingViewModel
import xyz.mperminov.tfscoursework.fragments.courses.lectures.LecturesViewModel
import xyz.mperminov.tfscoursework.repositories.lectures.LecturesRepository
import xyz.mperminov.tfscoursework.repositories.lectures.network.HomeworksNetworkRepository
import javax.inject.Qualifier
import javax.inject.Scope

@LecturesScope
@Component(
    modules = [LecturesModule::class],
    dependencies = [AppComponent::class]
)
interface LecturesComponent {
    fun inject(lecturesRepository: LecturesRepository)
    fun inject(viewModel: LecturesViewModel)
    fun inject(ratingViewModel: RatingViewModel)
    fun inject(networkRepository: HomeworksNetworkRepository)
    @Component.Builder
    interface Builder {
        fun build(): LecturesComponent
        fun plus(lecturesModule: LecturesModule): Builder
        fun plus(appComponent: AppComponent): Builder
    }
}

@Qualifier
@Retention(value = AnnotationRetention.RUNTIME)
@Scope
annotation class LecturesScope