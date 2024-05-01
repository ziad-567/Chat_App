package com.mis.route.chatapp.ui

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("app : error")

fun setErrorToTexInputLayout(
    textInputLayout: TextInputLayout,
    errorText:String?
) {
    textInputLayout.error = errorText

}