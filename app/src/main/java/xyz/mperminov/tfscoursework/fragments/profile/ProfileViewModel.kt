package xyz.mperminov.tfscoursework.fragments.profile

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.models.User
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.repositories.user.network.UserNetworkRepository
import javax.inject.Inject

class ProfileViewModel : ViewModel() {
    @Inject
    lateinit var repository: UserNetworkRepository
    private var userDisposable: Disposable? = null
    val user = MutableLiveData<UserResult>()
    val avatar = MutableLiveData<AvatarResult>()
    private val handler = Handler { msg -> user.value = msg.obj as UserResult; true }

    init {
        TFSCourseWorkApp.userComponent.inject(this)
    }

    override fun onCleared() {
        userDisposable?.dispose()
        userDisposable = null
        super.onCleared()
    }

    fun getUser() {
        userDisposable =
            repository.getUser().doOnSubscribe { handler.sendMessage(handler.obtainMessage(1, UserResult.Loading())) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ user ->
                    this.user.value = UserResult.Success(user); user.avatar?.let { getUserAvatar(it) }
                },
                    { this.user.value = UserResult.Success(User.NOBODY) })
    }

    private fun getUserAvatar(avatarPath: String) {
        Picasso.get().load(Api.API_AVATAR_HOST + avatarPath).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                avatar.value = AvatarResult(bitmap, null)
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                avatar.value = AvatarResult(null, e)
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            }
        })
    }
}

class AvatarResult(val bitmap: Bitmap?, val e: Throwable?)
sealed class UserResult(val user: User?) {
    class Success(user: User) : UserResult(user)
    class Loading() : UserResult(null)
}