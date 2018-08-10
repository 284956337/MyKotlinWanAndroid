package com.plus.mykotlinwanandroid.mvp.contract

import com.plus.mykotlinwanandroid.base.IPresenter
import com.plus.mykotlinwanandroid.base.IView

/**
 * Created by 180311 on 2018/8/8.
 * 收藏功能
 */
interface CommonContract {

    interface View : IView {

        fun showCollectSuccess(success: Boolean)

        fun showCancelCollectSuccess(success: Boolean)
    }

    interface Presenter<in V : View> : IPresenter<V>{

        fun addCollectArticle(id: Int)

        fun cancelCollectArticle(id: Int)

    }
}