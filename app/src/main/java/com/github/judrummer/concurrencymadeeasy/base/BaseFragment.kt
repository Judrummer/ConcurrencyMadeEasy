package com.github.judrummer.concurrencymadeeasy.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.github.judrummer.concurrencymadeeasy.extension.KodeinViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein

abstract class BaseFragment(@LayoutRes private val layoutId: Int) : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    inline fun <reified VM : ViewModel> fragmentViewModel() = lazy {
        ViewModelProviders.of(this, KodeinViewModelFactory(kodein)).get(VM::class.java)
    }

    inline fun <reified T> LiveData<T>.observe(crossinline onNext: (T) -> Unit) {
        observe(this@BaseFragment, Observer { onNext(it) })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        if (layoutId != -1) inflater.inflate(layoutId, container, false)
        else null
}