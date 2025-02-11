package com.wjx.android.wanandroidmvvm.ui.common.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wjx.android.wanandroidmvvm.ui.common.repository.ArticleRepository
import com.wjx.android.wanandroidmvvm.network.response.BaseResponse
import com.wjx.android.wanandroidmvvm.network.response.EmptyResponse
import com.wjx.android.wanandroidmvvm.base.viewmodel.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @date: 2020/02/25
 * Time: 20:39
 */

abstract class ArticleViewModel<T : ArticleRepository>(application: Application) :
    BaseViewModel<T>(application) {
    // RxJava2
//    var mCollectData: MutableLiveData<BaseResponse<EmptyResponse>> = MutableLiveData()


//    fun collect(id: Int) {
//        mRepository.collect(id, mCollectData)
//    }

//    fun unCollect(id: Int) {
//        mRepository.unCollect(id, mCollectData)
//    }

    // 使用协程 + Retrofit2.6以上版本
    var mCollectData : MutableLiveData<EmptyResponse> = MutableLiveData()

    fun collectCo(id: Int) {
        viewModelScope.launch {
            try {
                mCollectData.value = mRepository.collectCo(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun unCollectCo(id: Int) {
        viewModelScope.launch {
            try {
                mCollectData.value = mRepository.unCollectCo(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}