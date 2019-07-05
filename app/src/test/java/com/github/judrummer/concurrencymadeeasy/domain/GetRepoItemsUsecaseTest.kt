package com.github.judrummer.concurrencymadeeasy.domain

import com.github.judrummer.concurrencymadeeasy.data.*
import com.github.judrummer.concurrencymadeeasy.presentation.user.ContributorItem
import com.github.judrummer.concurrencymadeeasy.presentation.user.RepoItem
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class GetRepoItemsUsecaseTest {

    val repoApi = mockk<GithubApi>(relaxed = true)
    val usecase = GetRepoItemsUsecase(repoApi)

    @Test
    fun test() {
        //Arrange
        val repoEntities = listOf(
            RepoEntity(id = "1", name = "repo1", description = "desc1", stargazersCount = 1, owner = OwnerEntity(login = "owner1")),
            RepoEntity(id = "2", name = "repo2", description = "desc2", stargazersCount = 2, owner = OwnerEntity(login = "owner2")),
            RepoEntity(id = "3", name = "repo3", description = "desc3", stargazersCount = 3, owner = OwnerEntity(login = "owner3"))
        )
        val contributors1 = listOf(ContributorEntity(login = "user1"), ContributorEntity(login = "user2"))
        val contributors2 = listOf(ContributorEntity(login = "user3"))
        val contributors3 = listOf(ContributorEntity(login = "user4"))

        every { runBlocking { repoApi.getUserRepos("kotlin") } } returns repoEntities
        every { runBlocking { repoApi.getContributors(owner = "owner1", name = "repo1") } } returns contributors1
        every { runBlocking { repoApi.getContributors(owner = "owner2", name = "repo2") } } returns contributors2
        every { runBlocking { repoApi.getContributors(owner = "owner3", name = "repo3") } } returns contributors3


        //Act
        val actual = runBlocking { usecase("kotlin") }

        //Assert
        val expected = createExpectedResult()
        assertEquals(expected, actual)
    }

    private fun createExpectedResult() = listOf(
        RepoItem(
            name = "repo1",
            description = "desc1",
            starCount = 1,
            contributors = listOf(
                ContributorItem(name = "user1"),
                ContributorItem(name = "user2")
            )
        ),
        RepoItem(
            name = "repo2",
            description = "desc2",
            starCount = 2,
            contributors = listOf(
                ContributorItem(name = "user3")
            )
        ),
        RepoItem(
            name = "repo3",
            description = "desc3",
            starCount = 3,
            contributors = listOf(
                ContributorItem(name = "user4")
            )
        )
    )
}