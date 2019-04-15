package xyz.mperminov.tfscoursework.activities

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable

class LoginViewModel : ViewModel() {
    private val loginRepository = LoginRepository()
    private var authDisposable: Disposable? = null
    val response: MutableLiveData<Response> = MutableLiveData()
    val tokenStatus: MutableLiveData<Boolean> = MutableLiveData()

    init {
        tokenStatus.value = loginRepository.isTokenValid()
    }

    override fun onCleared() {
        authDisposable?.dispose()
        authDisposable = null
        super.onCleared()
    }

    fun attemptToLogin(email: String, password: String) {
        authDisposable = loginRepository.login(email, password).doOnSubscribe { response.value = Response.Loading() }
            .subscribe({ response.value = Response.Success() }, { e -> response.value = Response.Error(e) })
    }
}

enum class Status { LOADING, SUCCESS, ERROR }
sealed class Response(val status: Status, val error: Throwable?) {
    class Loading : Response(Status.LOADING, null)
    class Success : Response(Status.SUCCESS, null)
    class Error(error: Throwable) : Response(Status.ERROR, error)
}