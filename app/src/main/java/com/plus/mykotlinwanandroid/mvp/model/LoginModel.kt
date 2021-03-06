package com.plus.mykotlinwanandroid.mvp.model

import com.cxz.wanandroid.http.RetrofitHelper
import com.cxz.wanandroid.mvp.model.bean.HttpResult
import com.cxz.wanandroid.mvp.model.bean.LoginData
import com.cxz.wanandroid.rx.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by 180311 on 2018/8/7.
 */
class LoginModel {

    fun loginWanAndroid(username: String, password: String): Observable<HttpResult<LoginData>> {
        return RetrofitHelper.service.loginWanAndroid(username, password)
                .compose(SchedulerUtils.ioToMain())
    }
}