package com.github.judrummer.concurrencymadeeasy.base

import android.content.Context
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.github.judrummer.jxadapter.JxViewHolderLayoutContainer

abstract class BaseViewHolder<T>(parent: ViewGroup, @LayoutRes layoutId: Int) :
    JxViewHolderLayoutContainer<T>(parent, layoutId) {
    inline val context: Context get() = itemView.context
}