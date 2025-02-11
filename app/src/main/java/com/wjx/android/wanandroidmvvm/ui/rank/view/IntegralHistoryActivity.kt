package com.wjx.android.wanandroidmvvm.ui.rank.view

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.wjx.android.wanandroidmvvm.R
import com.wjx.android.wanandroidmvvm.base.view.BaseLifeCycleActivity
import com.wjx.android.wanandroidmvvm.common.utils.ChangeThemeEvent
import com.wjx.android.wanandroidmvvm.common.utils.ColorUtil
import com.wjx.android.wanandroidmvvm.ui.activity.ArticleDetailActivity
import com.wjx.android.wanandroidmvvm.ui.rank.adapter.IntegralHistoryAdapter
import com.wjx.android.wanandroidmvvm.ui.rank.data.IntegralHistoryResponse
import com.wjx.android.wanandroidmvvm.ui.rank.viewmodel.RankViewModel
import kotlinx.android.synthetic.main.fragment_article_list.mRvArticle
import kotlinx.android.synthetic.main.fragment_article_list.mSrlRefresh
import kotlinx.android.synthetic.main.integral_header_view.view.*
import org.greenrobot.eventbus.Subscribe

class IntegralHistoryActivity : BaseLifeCycleActivity<RankViewModel>() {
    private var mCurrentPage: Int = 1
    private lateinit var headerView: View
    private lateinit var mAdapter: IntegralHistoryAdapter
    override fun getLayoutId(): Int = R.layout.fragment_article_list

    override fun initView() {
        super.initView()
        mAdapter = IntegralHistoryAdapter(R.layout.integral_history_item, null)
        initHeaderView()
        initRefresh()
        mRvArticle.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRvArticle.adapter = mAdapter
        mAdapter.setEnableLoadMore(true)
        mAdapter.setOnLoadMoreListener({ onLoadMoreData() }, mRvArticle)
    }

    override fun initData() {
        super.initData()
        mCurrentPage = 1
        mViewModel.loadIntegralHistoryList(mCurrentPage)
        mViewModel.loadMeRankInfo()
    }

    override fun initDataObserver() {
        mViewModel.mIntegralHistoryListData.observe(this, Observer { response ->
            response.let {
                addData(it.data.datas)
            }
        })
        mViewModel.mMeRankInfo.observe(this, Observer { response ->
            response.let {
                startIntegralTextAnim(it.data.coinCount)
            }
        })
    }

    fun onRefreshData() {
        mCurrentPage = 1
        mViewModel.loadIntegralHistoryList(mCurrentPage)
    }

    fun onLoadMoreData() {
        mViewModel.loadIntegralHistoryList(++mCurrentPage)
    }

    private fun initHeaderView() {
        headerView = View.inflate(this, R.layout.integral_header_view, null)
        headerView.integral_title.text = "积分记录"
        headerView.integral_rule.setOnClickListener { onRulePressed() }
        headerView.integral_back.setOnClickListener { onBackPressed() }
        mAdapter.addHeaderView(headerView)
        initColor()
    }

    private fun initColor() {
        headerView.integral_bar.setBackgroundColor(ColorUtil.getColor(this))
    }

    private fun initRefresh() {
        // 设置下拉刷新的loading颜色
        mSrlRefresh.setProgressBackgroundColorSchemeColor(ColorUtil.getColor(this))
        mSrlRefresh.setColorSchemeColors(Color.WHITE)
        mSrlRefresh.setOnRefreshListener { onRefreshData() }
    }

    private fun startIntegralTextAnim(coinCount : Int) {
        val animator = ValueAnimator.ofInt(0,coinCount)
        //播放时长
        animator.duration = 1500
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { animation ->
            //获取改变后的值
            val currentValue = animation.animatedValue as Int
            headerView.integral_text_anim.text = "$currentValue"
        }
        animator.start()
    }

    fun addData(integralHistoryList: List<IntegralHistoryResponse>) {

        // 返回列表为空显示加载完毕
        if (integralHistoryList.isEmpty()) {
            mAdapter.loadMoreEnd()
            return
        }

        // 如果是下拉刷新状态，直接设置数据
        if (mSrlRefresh.isRefreshing) {
            mSrlRefresh.isRefreshing = false
            mAdapter.setNewData(integralHistoryList)
            mAdapter.loadMoreComplete()
            return
        }

        // 初始化状态直接加载数据
        mAdapter.addData(integralHistoryList)
        mAdapter.loadMoreComplete()
    }

    override fun showCreateReveal(): Boolean = false
    override fun showDestroyReveal(): Boolean = false
    override fun onBackPressed() = finish()

    private fun onRulePressed() {
        val intent: Intent = Intent(this, ArticleDetailActivity::class.java)
        intent.putExtra("url", "https://www.wanandroid.com/blog/show/2653")
        intent.putExtra("title", getString(R.string.rank_rule))
        startActivity(intent)
    }


    @Subscribe
    fun settingEvent(event: ChangeThemeEvent) {
        initColor()
        mSrlRefresh.setProgressBackgroundColorSchemeColor(ColorUtil.getColor(this))
    }
}
