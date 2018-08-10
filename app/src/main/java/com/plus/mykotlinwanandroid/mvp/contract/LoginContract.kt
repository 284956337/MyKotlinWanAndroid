package com.plus.mykotlinwanandroid.mvp.contract

import com.cxz.wanandroid.mvp.model.bean.LoginData
import com.plus.mykotlinwanandroid.base.IPresenter
import com.plus.mykotlinwanandroid.base.IView

/**
 * Created by 180311 on 2018/8/7.
 */
interface LoginContract {

    interface View : IView {
        fun loginSuccess(data: LoginData)

        fun loginFail()
    }

    interface Presenter : IPresenter<View> {

        fun loginWanAndroid(username: String, password: String)
    }
}