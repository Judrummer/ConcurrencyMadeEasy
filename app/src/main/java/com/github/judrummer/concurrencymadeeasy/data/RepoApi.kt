package com.github.judrummer.concurrencymadeeasy.data

import com.google.gson.annotations.SerializedName
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class SearchReposApiResponseEntity(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val items: List<RepoEntity>
)

data class RepoEntity(
    @SerializedName("id") val id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("full_name") val fullName: String = "",
    @SerializedName("description") val description: String? = "",
    @SerializedName("language") val language: String = "",
    @SerializedName("owner") val owner: OwnerEntity = OwnerEntity(),
    @SerializedName("git_url") val gitUrl: String = "",
    @SerializedName("ssh_url") val sshUrl: String = "",
    @SerializedName("stargazers_count") val stargazersCount: Int = 0
)

data class OwnerEntity(
    @SerializedName("id") val id: String = "",
    @SerializedName("login") val login: String = "",
    @SerializedName("avatar_url") val avatarUrl: String = ""
)

data class ContributorEntity(
    @SerializedName("login") val login: String = "",
    @SerializedName("contributions") val contributions: Int =0,
    @SerializedName("avatar_url") val avatarUrl: String = ""
)

interface RepoApi {
    @GET("search/repositories")
    fun rxSearchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Observable<SearchReposApiResponseEntity>

    @GET("repos/{owner}/{name}/contributors")
    fun rxGetContributors(
        @Path("owner") owner: String,
        @Path("name") name: String
    ): Observable<List<ContributorEntity>>

    @GET("search/repositories")
    suspend fun searchRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchReposApiResponseEntity

    @GET("repos/{owner}/{name}/contributors")
    suspend fun getContributors(
        @Path("owner") owner: String,
        @Path("name") name: String
    ): List<ContributorEntity>
}