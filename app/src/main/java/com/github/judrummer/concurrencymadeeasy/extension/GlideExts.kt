package com.github.judrummer.concurrencymadeeasy.extension

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.judrummer.concurrencymadeeasy.R

fun ImageView.loadCircular(url: String, @DrawableRes placeholderRes: Int = R.drawable.ic_person_black_24dp) {
    Glide.with(this)
        .load(url)
        .apply(RequestOptions.placeholderOf(placeholderRes))
        .apply(RequestOptions.circleCropTransform())
        .into(this)
}
