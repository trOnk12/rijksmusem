package com.example.maxdoroassigment.core.bindingadapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.maxdoroassigment.R
import com.squareup.picasso.Picasso


@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, url: String) {
    Picasso.get()
        .load(url)
        .placeholder(R.drawable.place_holder)
        .fit()
        .into(imageView)
}