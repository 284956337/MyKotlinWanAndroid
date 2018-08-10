package com.plus.mykotlinwanandroid.mvp.presenter

import com.cxz.wanandroid.http.exception.ExceptionHandle
import com.plus.mykotlinwanandroid.base.BasePresenter
import com.plus.mykotlinwanandroid.mvp.contract.LoginContract
import com.plus.mykotlinwanandroid.mvp.model.LoginModel

/**
 * Created by 180311 on 2018/8/7.
 */
class LoginPresenter : BasePresenter<LoginContract.View>(), LoginContract.Presenter {

    private val loginModel: LoginModel by lazy {
        LoginModel()
    }

    override fun loginWanAndroid(username: String, password: String) {
        mRootView?.showLoading()
        val disposable = loginModel.loginWanAndroid(username, password)
                .subscribe({result ->
                    mRootView?.apply {
                        if(result.errorCode != 0){
                            showError(result.errorMsg)
                            loginFail()
                        }else {
                            loginSuccess(result.data)
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