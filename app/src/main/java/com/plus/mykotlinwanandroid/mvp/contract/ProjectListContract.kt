package com.plus.mykotlinwanandroid.mvp.contract

import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody

/**
 * Created by 180311 on 2018/8/9.
 */
interface ProjectListContract {

    interface View : CommonContract.View {

        fun scrollToTop()

        fun setProjectList(article: ArticleResponseBody)
    }

    interface Presenter : CommonContract.Presenter<View> {

        fun requestProjectList(page: Int, cid: Int)
    }
}