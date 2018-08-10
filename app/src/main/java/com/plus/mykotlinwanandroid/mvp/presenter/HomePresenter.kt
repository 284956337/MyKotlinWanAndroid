package com.plus.mykotlinwanandroid.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.plus.mykotlinwanandroid.mvp.contract.HomeContract
import com.plus.mykotlinwanandroid.mvp.model.HomeModel

/**
 * Created by 180311 on 2018/8/8.
 */
class HomePresenter : CommonPresenter<HomeContract.View>(), HomeContract.Presenter {

    private val homeModel: HomeModel by lazy {
        HomeModel()
    }

    override fun requestBanner() {
        val disposable = homeModel.requestBanner()
                .subscribe({ results ->
                    mRootView?.apply {
                        setBanner(results.data)
                    }
                }, { t ->
                    mRootView?.apply {
                        hideLoading()
                        showError(ExceptionHandle.handleException(t))
                    }
                })
        addSubscription(disposable)

    }

    override fun requestArticles(num: Int) {
        if(num == 0)
            mRootView?.showLoading()
        val disposable = homeModel.requestArticles(num)
                .subscribe({ results ->
                    mRootView?.apply {
                        if(results.errorCode != 0) {
                            showError(results.errorMsg)
                        }else {
                            setArticles(results.data)
                        }
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