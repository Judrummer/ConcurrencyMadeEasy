package com.github.judrummer.concurrencymadeeasy.presentation.user

import android.view.ViewGroup
import com.github.judrummer.concurrencymadeeasy.R
import com.github.judrummer.concurrencymadeeasy.base.BaseViewHolder
import com.github.judrummer.concurrencymadeeasy.extension.loadCircular
import com.github.judrummer.jxadapter.JxAdapter
import kotlinx.android.synthetic.main.item_contributor.*
import kotlinx.android.synthetic.main.item_repo.*


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

    class ContributorItemViewHolder(parent: ViewGroup) :
        BaseViewHolder<ContributorItem>(parent, R.layout.item_contributor) {
        override fun bind(item: ContributorItem) {
            tvContributorName.text = item.name
            ivContributorImage.loadCircular(item.avatarUrl)
        }
    }
}