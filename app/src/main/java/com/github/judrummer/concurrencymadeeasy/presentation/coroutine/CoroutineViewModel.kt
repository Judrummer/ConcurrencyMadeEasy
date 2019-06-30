package com.github.judrummer.concurrencymadeeasy.presentation.coroutine

import androidx.lifecycle.*
import com.github.judrummer.concurrencymadeeasy.data.ContributorEntity
import com.github.judrummer.concurrencymadeeasy.data.RepoApi
import com.github.judrummer.concurrencymadeeasy.data.RepoEntity
import io.reactivex.Observable
import kotlinx.coroutines.*

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

data class CoroutineViewState(
    val repos: List<RepoItem> = emptyList(),
    val loading: Boolean = false,
    val timer: Int? = null
)

class CoroutineViewModel(private val repoApi: RepoApi) : ViewModel() {

    private val _state = MutableLiveData<CoroutineViewState>().apply { value = CoroutineViewState() }
    val state: LiveData<CoroutineViewState> = _state
    private var timerJob: Job? = null

    fun clickTimer() {
        if (state.value?.timer != null) {
            timerJob?.cancel()
            timerJob = null
            setState { copy(timer = null) }
        } else {
            timerJob = viewModelScope.launch {
                setState { copy(timer = 0) }
                while (true) {
                    println("MYDEBUG Timer ${state.value?.timer}")
                    setState { copy(timer = timer?.let { it + 1 }) }
                    delay(1000L)
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            try {
                setState { copy(loading = true) }
                val repos = getRepoItems()
                setState { copy(repos = repos, loading = false) }
            } catch (e: Throwable) {
                setState { copy(loading = false) }
            }
        }
    }

    private suspend fun getRepoItems() = coroutineScope {
        repoApi.searchRepos(query = "kotlin coroutine", page = 0, perPage = 3).items
            .map { repo -> async { repo to repoApi.getContributors(owner = repo.owner.login, name = repo.name) } }
            .awaitAll()
            .map { (repo, contributors) ->
                RepoItem(
                    name = repo.name,
                    description = repo.description ?: "",
                    starCount = repo.stargazersCount,
                    contributors = contributors.map {
                        ContributorItem(
                            name = it.login,
                            avatarUrl = it.avatarUrl,
                            contributions = it.contributions
                        )
                    }
                )
            }
    }

    private fun rxGetRepoItems() =
        repoApi.rxSearchRepos(query = "kotlin coroutine", page = 0, perPage = 3)
            .map { it.items }
            .flatMap {
                val getContributorsList = it
                    .map { repo ->
                        repoApi.rxGetContributors(repo.owner.login, repo.name).map { repo to it }
                    }
                Observable.zip(getContributorsList) {
                    (it as? Array<Pair<RepoEntity, List<ContributorEntity>>>)?.let {
                        it.map { (repo, contributors) ->
                            RepoItem(
                                name = repo.name,
                                description = repo.description ?: "",
                                starCount = repo.stargazersCount,
                                contributors = contributors.map {
                                    ContributorItem(
                                        name = it.login,
                                        avatarUrl = it.avatarUrl,
                                        contributions = it.contributions
                                    )
                                }
                            )
                        }
                    }
                }
            }

    /*
     get seller profile by orderId ->   getOrder(orderId).sellerId -> getSellerProfile(sellerId)
    * */

    private inline fun setState(builder: CoroutineViewState.() -> CoroutineViewState) {
        _state.value = (state.value ?: CoroutineViewState()).builder()
    }
}