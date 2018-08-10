package com.plus.mykotlinwanandroid.util

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.cxz.wanandroid.utils.SettingUtil

/**
 * Created by 180311 on 2018/8/8.
 */
object ImageLoader {
    /**
     *
     */
    fun load(context: Context?, url: String?, iv: ImageView?) {
        if(!SettingUtil.getIsNoPhotoMode()) {
            iv?.let {
                Glide.with(context!!)
                        .load(url)
                        .transition(DrawableTransitionOptions().crossFade())
                        .into(iv)
            }
        }
    }
}