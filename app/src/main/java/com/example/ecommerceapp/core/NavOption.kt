package com.example.ecommerceapp.core

import androidx.navigation.NavOptions
import com.example.ecommerceapp.R

class NavOption {

    companion object {
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.fade_in)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.fade_out)
            .build()
    }
}