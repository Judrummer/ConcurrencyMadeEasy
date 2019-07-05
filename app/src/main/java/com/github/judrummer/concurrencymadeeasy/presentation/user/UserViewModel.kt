package com.github.judrummer.concurrencymadeeasy.presentation.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.judrummer.concurrencymadeeasy.data.GithubApi
import com.github.judrummer.concurrencymadeeasy.data.UserEntity
import com.github.judrummer.concurrencymadeeasy.domain.GetRepoItemsUsecase
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

data class RepoItem(
    val name: String,
    val description: String,
    val starCount: Int,
    val contributors: List<ContributorItem>
)

data class ContributorItem(
    val name: String,
    val contributions: Int = 0,
    val avatarUrl: String = ""
)

data class UserViewState(
    val displayName: String = "",
    val username: String = "",
    val avatarUrl: String = "",
    val repos: List<RepoItem> = emptyList(),
    val loading: Boolean = true,
    val timer: Int? = null,
    val clockText: String = "00:00:00"
)

class UserViewModel(
    private val githubApi: GithubApi,
    private val getRepoItemsUsecase: GetRepoItemsUsecase
) : ViewModel(), CoroutineScope {

    private val coroutineExceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        hideLoading()
        showError(throwable)
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Main + SupervisorJob() + coroutineExceptionHandler

    var clockJob: Job? = null

    init {
        clockJob = launch {
            val dateFormat = SimpleDateFormat("hh:mm:ss")
            while (true) {
                val newClockText = Date().let { dateFormat.format(it) }
                updateClockText(newClockText)
                delay(1000L)
            }
        }
    }

    override fun onCleared() {
        clockJob?.cancel()
        coroutineContext.cancel()
        super.onCleared()
    }

    fun fetch(username: String) {
        launch {
            showLoading()
            val (userProfile, repoList) = coroutineScope {
                val userProfileAsync = async { fetchUserProfile(username) }
                val repoListAsync = async { fetchRepoList(username) }
                userProfileAsync.await() to repoListAsync.await()
            }
            hideLoading()
            renderUI(userProfile, repoList)
        }
    }

    private suspend fun fetchUserProfile(username: String) = githubApi.getUser(username)
    private suspend fun fetchRepoList(username: String): List<RepoItem> = getRepoItemsUsecase(username)

    private val _error = MutableLiveData<Throwable?>().apply { value = null } //TODO: SingleLiveEvent
    val error: LiveData<Throwable?> = _error
    private val _state = MutableLiveData<UserViewState>().apply { value = UserViewState() }
    val state: LiveData<UserViewState> = _state

    private fun showLoading() {
        setState { copy(loading = true) }
    }

    private fun hideLoading() {
        setState { copy(loading = false) }
    }

    private fun renderUI(userProfile: UserEntity, repoList: List<RepoItem> = emptyList()) {
        setState { copy(username = userProfile.login, displayName = userProfile.name, avatarUrl = userProfile.avatarUrl, repos = repoList) }
    }

    private fun updateClockText(clockText: String) {
        setState { copy(clockText = clockText) }
    }

    private fun showError(throwable: Throwable) {
        _error.value = throwable
    }

    private inline fun setState(builder: UserViewState.() -> UserViewState) {
        _state.value = (state.value ?: UserViewState()).builder()
    }
}
