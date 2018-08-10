package com.plus.mykotlinwanandroid.base

/**
 * Created by 180311 on 2018/8/7.
 */
interface IView {
    fun showLoading()

    fun hideLoading()

    fun showError(errorMsg: String)
}