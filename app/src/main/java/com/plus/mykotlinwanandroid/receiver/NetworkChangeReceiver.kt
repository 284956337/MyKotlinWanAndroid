package com.cxz.wanandroid.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.event.NetworkChangeEvent
import com.cxz.wanandroid.utils.NetWorkUtil
import com.plus.mykotlinwanandroid.ext.loge
import com.plus.mykotlinwanandroid.util.Preference
import org.greenrobot.eventbus.EventBus

/**
 */
class NetworkChangeReceiver : BroadcastReceiver() {

    /**
     * 缓存上一次的网络状态
     */
    private var hasNetwork: Boolean by Preference(Constant.HAS_NETWORK_KEY, true)

    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = NetWorkUtil.isNetworkConnected(context)
        loge("onReceive：当前网络状态------>>$isConnected")
        if (isConnected) {
            if (isConnected != hasNetwork) {
                EventBus.getDefault().post(NetworkChangeEvent(isConnected))
            }
        } else {
            EventBus.getDefault().post(NetworkChangeEvent(isConnected))
        }
    }

}