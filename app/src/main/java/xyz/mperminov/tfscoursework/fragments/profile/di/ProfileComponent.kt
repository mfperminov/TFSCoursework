package xyz.mperminov.tfscoursework.fragments.profile.di

import dagger.Component
import xyz.mperminov.tfscoursework.di.AppComponent
import xyz.mperminov.tfscoursework.fragments.profile.ProfileViewModel
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository
import javax.inject.Qualifier
import javax.inject.Scope

@ProfileScope
@Component(
    modules = [ProfileModule::class],
    dependencies = [AppComponent::class]
)
interface ProfileComponent {
    fun inject(userNetworkRepository: UserNetworkRepository)
    fun inject(viewModel: ProfileViewModel)
    @Component.Builder
    interface Builder {
        fun build(): ProfileComponent
        fun plus(profileModule: ProfileModule): Builder
        fun plus(appComponent: AppComponent): Builder
    }
}

@Qualifier
@Retention(value = AnnotationRetention.RUNTIME)
@Scope
annotation class ProfileScope