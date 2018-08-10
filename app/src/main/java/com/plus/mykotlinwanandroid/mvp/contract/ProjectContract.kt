package com.plus.mykotlinwanandroid.mvp.contract

import com.cxz.wanandroid.mvp.model.bean.ProjectTreeBean
import com.plus.mykotlinwanandroid.base.IPresenter
import com.plus.mykotlinwanandroid.base.IView

/**
 * Created by 180311 on 2018/8/9.
 */
interface ProjectContract {

    interface View : IView {

        fun scrollToTop()

        fun setProjectTree(list: List<ProjectTreeBean>)
    }

    interface Presenter : IPresenter<View> {

        fun requestProjectTree()

    }
}