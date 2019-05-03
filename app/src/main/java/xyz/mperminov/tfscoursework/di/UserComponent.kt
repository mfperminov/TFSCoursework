package xyz.mperminov.tfscoursework.di

import dagger.Component
import xyz.mperminov.tfscoursework.fragments.activitites.ActivitiesFragment
import xyz.mperminov.tfscoursework.fragments.activitites.archive.ArchiveActivitiesViewModel
import xyz.mperminov.tfscoursework.fragments.profile.ProfileViewModel
import xyz.mperminov.tfscoursework.fragments.students.di.StudentComponent
import xyz.mperminov.tfscoursework.repositories.activities.ActivitiesNetworkRepository
import xyz.mperminov.tfscoursework.repositories.activities.ArchiveRepository
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository
import javax.inject.Qualifier
import javax.inject.Scope

@UserScope
@Component(
    modules = [UserModule::class],
    dependencies = [AppComponent::class]
)
interface UserComponent {
    fun studentComponentBuilder(): StudentComponent.Builder
    fun inject(userNetworkRepository: UserNetworkRepository)
    fun inject(viewModel: ProfileViewModel)
    fun inject(activitiesNetworkRepository: ActivitiesNetworkRepository)
    fun inject(archiveRepository: ArchiveRepository)
    fun inject(activitiesFragment: ActivitiesFragment)
    fun inject(viewModel: ArchiveActivitiesViewModel)
}

@Qualifier
@Retention(value = AnnotationRetention.RUNTIME)
@Scope
annotation class UserScope