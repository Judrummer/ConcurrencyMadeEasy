package com.github.judrummer.concurrencymadeeasy.presentation.login

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.github.judrummer.concurrencymadeeasy.R
import com.github.judrummer.concurrencymadeeasy.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment(R.layout.fragment_login) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToUserFragment(etGithubUser.text.toString()))
        }
    }
}