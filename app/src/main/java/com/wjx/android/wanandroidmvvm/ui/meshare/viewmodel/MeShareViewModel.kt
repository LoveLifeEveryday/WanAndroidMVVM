package com.wjx.android.wanandroidmvvm.ui.meshare.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.wjx.android.wanandroidmvvm.ui.common.viewmodel.ArticleViewModel
import com.wjx.android.wanandroidmvvm.network.response.BaseResponse
import com.wjx.android.wanandroidmvvm.network.response.EmptyResponse
import com.wjx.android.wanandroidmvvm.ui.meshare.data.MeShareArticleResponse
import com.wjx.android.wanandroidmvvm.ui.meshare.data.MeShareResponse
import com.wjx.android.wanandroidmvvm.ui.meshare.repository.MeShareRepository

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @date: 2020/03/26
 * Time: 17:36
 */
class MeShareViewModel(application: Application) :
    ArticleViewModel<MeShareRepository>(application) {
    var mMeShareData: MutableLiveData<BaseResponse<MeShareResponse<MeShareArticleResponse>>> =
        MutableLiveData()
    var mDeleteMeShareData: MutableLiveData<BaseResponse<EmptyResponse>> = MutableLiveData()

    fun loadMeShareArticle(pageNum: Int) {
        mRepository.loadShareArticle(pageNum, mMeShareData)
    }

    fun deleteMeShareArticle(id: Int) {
        mRepository.deleteShareArticle(id, mDeleteMeShareData)
    }
}