package com.wjx.android.wanandroidmvvm.network

import androidx.lifecycle.MutableLiveData
import com.wjx.android.wanandroidmvvm.common.state.State
import com.wjx.android.wanandroidmvvm.common.state.StateType
import com.wjx.android.wanandroidmvvm.common.state.UserInfo
import com.wjx.android.wanandroidmvvm.common.utils.Constant
import com.wjx.android.wanandroidmvvm.network.response.BaseResponse

/**
 * Created with Android Studio.
 * Description:数据解析扩展函数
 * @author: Wangjianxian
 * @CreateDate: 2020/4/19 17:35
 */

fun <T> BaseResponse<T>.dataConvert(
    loadState: MutableLiveData<State>
) : T{
    when (errorCode) {
        Constant.SUCCESS -> {
            if (data is List<*>) {
                if ((data as List<*>).isEmpty()) {
                    loadState.postValue(State(StateType.EMPTY))
                }
            }
            loadState.postValue(State(StateType.SUCCESS))
            return data
        }
        Constant.NOT_LOGIN -> {
            UserInfo.instance.logoutSuccess()
            loadState.postValue(State(StateType.ERROR, message = "请重新登录"))
            return data
        }
        else -> {
            loadState.postValue(State(StateType.ERROR, message = errorMessage))
            throw Exception(errorMessage)
        }
    }
}

