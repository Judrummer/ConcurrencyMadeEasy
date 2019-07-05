package com.github.judrummer.concurrencymadeeasy.presentation.user

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.github.judrummer.concurrencymadeeasy.R
import com.github.judrummer.concurrencymadeeasy.base.BaseFragment
import com.github.judrummer.concurrencymadeeasy.extension.load
import com.github.judrummer.jxadapter.JxAdapter
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.layout_user_profile.*

class UserFragment : BaseFragment(R.layout.fragment_user) {

    val args by navArgs<UserFragmentArgs>()

    val viewModel by fragmentViewModel<UserViewModel>()

    val jxAdapter = JxAdapter {
        viewHolder(::RepoItemViewHolder)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvRepoList.adapter = jxAdapter

        viewModel.state.observe {
            contentView.visibility = if (it.loading) View.GONE else View.VISIBLE
            loadingView.visibility = if (it.loading) View.VISIBLE else View.GONE

            tvClock.text = it.clockText
            tvUsername.text = it.username
            tvDisplayName.text = it.displayName
            ivUserAvatar.load(it.avatarUrl)
            jxAdapter.items = it.repos
        }

        viewModel.error.observe {
            it?.let {
                Toast.makeText(requireContext(), "Error :$it", Toast.LENGTH_SHORT).show()
            }
        }

        //Use single live event
        viewModel.fetch(args.username)
    }
}