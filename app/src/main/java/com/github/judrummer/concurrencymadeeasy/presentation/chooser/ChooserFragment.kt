package com.github.judrummer.concurrencymadeeasy.presentation.chooser

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.github.judrummer.concurrencymadeeasy.R
import com.github.judrummer.concurrencymadeeasy.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_chooser.*

class ChooserFragment : BaseFragment(R.layout.fragment_chooser) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRxJava.setOnClickListener {
            findNavController().navigate(R.id.action_chooserFragment_to_coroutineFragment)
        }

        btnCoroutine.setOnClickListener {
            findNavController().navigate(R.id.action_chooserFragment_to_coroutineFragment)
        }
    }
}