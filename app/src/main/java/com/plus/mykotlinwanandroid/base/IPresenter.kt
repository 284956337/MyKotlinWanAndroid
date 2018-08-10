package com.plus.mykotlinwanandroid.base

/**
 * Created by 180311 on 2018/8/7.
 */
interface IPresenter<in V : IView> {
    fun attachView(mRootView: V)

    fun detachView()
}