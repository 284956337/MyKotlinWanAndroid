package com.plus.mykotlinwanandroid.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.plus.mykotlinwanandroid.base.BasePresenter
import com.plus.mykotlinwanandroid.mvp.contract.ProjectContract
import com.plus.mykotlinwanandroid.mvp.model.ProjectModel

/**
 * Created by 180311 on 2018/8/9.
 */
class ProjectPresenter : BasePresenter<ProjectContract.View>(), ProjectContract.Presenter {

    private val mProjectModel: ProjectModel by lazy {
        ProjectModel()
    }

    override fun requestProjectTree(){
        mRootView?.showLoading()
        val disposable = mProjectModel.requestProjectTree()
                .subscribe({ results ->
                    mRootView?.apply {
                        if (0 != results.errorCode) {
                            showError(results.errorMsg)
                        } else {
                            setProjectTree(results.data)
                        }
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