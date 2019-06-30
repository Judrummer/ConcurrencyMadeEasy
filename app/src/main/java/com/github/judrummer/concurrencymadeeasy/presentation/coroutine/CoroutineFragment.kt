package com.github.judrummer.concurrencymadeeasy.presentation.coroutine

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.github.judrummer.concurrencymadeeasy.R
import com.github.judrummer.concurrencymadeeasy.base.BaseFragment
import com.github.judrummer.concurrencymadeeasy.base.BaseViewHolder
import com.github.judrummer.concurrencymadeeasy.extension.loadCircular
import com.github.judrummer.jxadapter.JxAdapter
import kotlinx.android.synthetic.main.fragment_repo_list.*
import kotlinx.android.synthetic.main.item_contributor.*
import kotlinx.android.synthetic.main.item_repo.*

class ContributorItemViewHolder(parent: ViewGroup) :
    BaseViewHolder<ContributorItem>(parent, R.layout.item_contributor) {
    override fun bind(item: ContributorItem) {
        tvContributorName.text = item.name
        ivContributorImage.loadCircular(item.avatarUrl)
    }
}

class RepoItemViewHolder(parent: ViewGroup) : BaseViewHolder<RepoItem>(parent, R.layout.item_repo) {

    private val contributorAdapter = JxAdapter {
        viewHolder(::ContributorItemViewHolder)
    }

    init {
        rvContributorList.adapter = contributorAdapter
    }

    override fun bind(item: RepoItem) {
        contributorAdapter.items = item.contributors
        tvRepoName.text = item.name
        tvRepoStar.text = item.starCount.toString()
        tvRepoDescription.text = item.description
    }
}

class CoroutineFragment : BaseFragment(R.layout.fragment_repo_list) {

    val viewModel by fragmentViewModel<CoroutineViewModel>()

    val repoListAdapter = JxAdapter { viewHolder(::RepoItemViewHolder) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //setupView
        btnTimer.setOnClickListener { viewModel.clickTimer() }
        srlRepoList.setOnRefreshListener { viewModel.refresh() }

        rvRepoList.adapter = repoListAdapter

        //observeViewModel
        viewModel.refresh()
        viewModel.state.observe { state ->
            srlRepoList.isRefreshing = state.loading
            repoListAdapter.items = state.repos
            btnTimer.text = state.timer?.let { "Stop ($it)" } ?: "Start"
        }
    }
}