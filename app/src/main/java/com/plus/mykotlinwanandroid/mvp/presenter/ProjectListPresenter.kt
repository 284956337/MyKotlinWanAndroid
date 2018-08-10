package com.plus.mykotlinwanandroid.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.plus.mykotlinwanandroid.mvp.contract.ProjectListContract
import com.plus.mykotlinwanandroid.mvp.model.ProjectListModel

/**
 * Created by 180311 on 2018/8/9.
 */
class ProjectListPresenter : CommonPresenter<ProjectListContract.View>(), ProjectListContract.Presenter {

    private val projectListModel: ProjectListModel by lazy {
        ProjectListModel()
    }

    override fun requestProjectList(page: Int, cid: Int) {
        if(page == 1) {
            mRootView?.showLoading()
        }
        val disposable = projectListModel.requestProjectList(page, cid)
                .subscribe({ results ->
                    mRootView?.run {
                        if(0 != results.errorCode)
                            showError(results.errorMsg)
                        else
                            setProjectList(results.data)
                        hideLoading()
                    }
                }, { t ->
                    mRootView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }

}