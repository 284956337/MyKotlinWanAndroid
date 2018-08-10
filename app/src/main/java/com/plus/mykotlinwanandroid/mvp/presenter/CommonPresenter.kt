package com.plus.mykotlinwanandroid.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.plus.mykotlinwanandroid.base.BasePresenter
import com.plus.mykotlinwanandroid.mvp.contract.CommonContract
import com.plus.mykotlinwanandroid.mvp.model.CommonModel

/**
 * Created by 180311 on 2018/8/8.
 */
open class CommonPresenter<V : CommonContract.View>
    : BasePresenter<V>(), CommonContract.Presenter<V>{

    private val mModel: CommonModel by lazy {
        CommonModel()
    }

    override fun addCollectArticle(id: Int) {
        val disposable = mModel.addCollectArticle(id)
                .subscribe({results ->
                    mRootView?.run {
                        if(results.errorCode != 0) {
                            showError(results.errorMsg)
                        }else {
                            showCollectSuccess(true)
                        }
                    }
                }, {t ->
                    mRootView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)
    }

    override fun cancelCollectArticle(id: Int) {
        val disposable = mModel.cancelCollectArticle(id)
                .subscribe({ results ->
                    mRootView?.run {
                        if (results.errorCode != 0) {
                            showError(results.errorMsg)
                        }else {
                            showCancelCollectSuccess(true)
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