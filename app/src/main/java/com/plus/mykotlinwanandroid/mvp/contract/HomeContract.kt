package com.plus.mykotlinwanandroid.mvp.contract

import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody
import com.cxz.wanandroid.mvp.model.bean.Banner

/**
 * Created by 180311 on 2018/8/8.
 */
interface HomeContract {

    interface View : CommonContract.View {

        fun scrollToTop()

        fun setBanner(banner: List<Banner>)

        fun setArticles(articles: ArticleResponseBody)
    }

    interface Presenter: CommonContract.Presenter<View> {

        fun requestBanner()

        fun requestArticles(num: Int)

    }
}