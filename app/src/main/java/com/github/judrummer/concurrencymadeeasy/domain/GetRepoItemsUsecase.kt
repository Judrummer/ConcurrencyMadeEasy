package com.github.judrummer.concurrencymadeeasy.domain

import com.github.judrummer.concurrencymadeeasy.data.RepoApi
import com.github.judrummer.concurrencymadeeasy.presentation.coroutine.ContributorItem
import com.github.judrummer.concurrencymadeeasy.presentation.coroutine.RepoItem
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class GetRepoItemsUsecase(private val repoApi: RepoApi) {
    suspend operator fun invoke() = coroutineScope {
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
}