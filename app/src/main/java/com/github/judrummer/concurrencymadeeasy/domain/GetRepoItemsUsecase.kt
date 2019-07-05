package com.github.judrummer.concurrencymadeeasy.domain

import com.github.judrummer.concurrencymadeeasy.data.ContributorEntity
import com.github.judrummer.concurrencymadeeasy.data.GithubApi
import com.github.judrummer.concurrencymadeeasy.data.RepoEntity
import com.github.judrummer.concurrencymadeeasy.presentation.user.ContributorItem
import com.github.judrummer.concurrencymadeeasy.presentation.user.RepoItem
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class GetRepoItemsUsecase(private val githubApi: GithubApi) {

    suspend operator fun invoke(username: String): List<RepoItem> = coroutineScope {
        githubApi.getUserRepos(username)
            .take(3) //Limit because github free usage limit
            .map { repo -> async { repo to githubApi.getContributors(owner = repo.owner.login, name = repo.name) } }
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

    private fun mapRepoItem(repo: RepoEntity, contributors: List<ContributorEntity>) =
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