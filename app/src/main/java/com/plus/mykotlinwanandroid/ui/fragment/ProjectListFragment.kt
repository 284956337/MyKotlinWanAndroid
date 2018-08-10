package com.plus.mykotlinwanandroid.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.cxz.wanandroid.constant.Constant
import com.cxz.wanandroid.mvp.model.bean.Article
import com.cxz.wanandroid.mvp.model.bean.ArticleResponseBody
import com.cxz.wanandroid.utils.NetWorkUtil
import com.plus.mykotlinwanandroid.R
import com.plus.mykotlinwanandroid.adapter.ProjectAdapter
import com.plus.mykotlinwanandroid.app.MyApp
import com.plus.mykotlinwanandroid.base.BaseFragment
import com.plus.mykotlinwanandroid.ext.showSnackMsg
import com.plus.mykotlinwanandroid.ext.showToast
import com.plus.mykotlinwanandroid.mvp.contract.ProjectListContract
import com.plus.mykotlinwanandroid.mvp.presenter.ProjectListPresenter
import com.plus.mykotlinwanandroid.ui.activity.ContentActivity
import com.plus.mykotlinwanandroid.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_refresh_layout.*

/**
 * Created by 180311 on 2018/8/9.
 */
class ProjectListFragment : BaseFragment(), ProjectListContract.View {

    companion object {
        fun getInstance(cid: Int) : ProjectListFragment {
            val fragment = ProjectListFragment()
            val args = Bundle()
            args.putInt(Constant.CONTENT_ID_KEY, cid)
            fragment.arguments = args
            return fragment
        }
    }

    /**
     * Presenter
     */
    private val mPresenter: ProjectListPresenter by lazy {
        ProjectListPresenter()
    }

    private var cid: Int = -1

    /**
     * datas
     */
    private val datas = mutableListOf<Article>()

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    private val recylerViewItemDecoration by lazy {
        activity?.let { SpaceItemDecoration(it) }
    }

    /**
     * ProjectAdapter
     */
    private val projectAdapter: ProjectAdapter by lazy {
        ProjectAdapter(activity, datas)
    }

    /**
     * isRefresh
     */
    private var isRefresh = true

    override fun attachLayoutRes(): Int = R.layout.fragment_refresh_layout

    override fun initView() {
        mPresenter.attachView(this)
        cid = arguments?.getInt(Constant.CONTENT_CID_KEY) ?: -1

        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(onRefreshListener)
        }

        recyclerView.run {
            layoutManager = linearLayoutManager
            adapter = projectAdapter
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(recylerViewItemDecoration)
        }

        projectAdapter.run {
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@ProjectListFragment.onItemClickListener
            onItemChildClickListener = this@ProjectListFragment.onItemChildClickListener
            setEmptyView(R.layout.fragment_empty_layout)
        }
    }

    override fun lazyLoad() {
        mPresenter.requestProjectList(1, cid)
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = isRefresh
    }

    override fun hideLoading() {
        swipeRefreshLayout?.isRefreshing = false
        if (isRefresh) {
            projectAdapter.run {
                setEnableLoadMore(true)
            }
        }
    }

    override fun showError(errorMsg: String) {
        projectAdapter.run {
            if (isRefresh) {
                setEnableLoadMore(true)
            }else{
                loadMoreFail()
            }
        }
    }

    override fun scrollToTop() {
        recyclerView.run {
            if (linearLayoutManager.findFirstVisibleItemPosition() > 20) {
                scrollToPosition(0)
            } else {
                smoothScrollToPosition(0)
            }
        }
    }

    override fun setProjectList(article: ArticleResponseBody) {
        article.datas.let {
            projectAdapter.run {
                if (isRefresh) {
                    replaceData(it)
                }else {
                    addData(it)
                }
                val size = it.size
                if (size < article.size) {
                    loadMoreEnd(isRefresh)
                }else {
                    loadMoreComplete()
                }
            }
        }
    }

    override fun showCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.collect_success))
        }
    }

    override fun showCancelCollectSuccess(success: Boolean) {
        if (success) {
            showToast(getString(R.string.cancel_collect_success))
        }
    }

    private val onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        isRefresh = true
        projectAdapter.setEnableLoadMore(false)
        mPresenter.requestProjectList(1, cid)
    }

    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        isRefresh = false
        swipeRefreshLayout.isRefreshing = false
        val page = projectAdapter.data.size / 15 + 1
        mPresenter.requestProjectList(page, cid)
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
        if(datas.size != 0) {
            val data = datas[position]
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, data.link)
                putExtra(Constant.CONTENT_TITLE_KEY, data.title)
                putExtra(Constant.CONTENT_ID_KEY, data.id)
                startActivity(this)
            }
        }
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
        if (datas.size != 0) {
            val data = datas[position]
            when(view.id){
                R.id.item_project_list_like_iv -> {
                    if (isLogin) {
                        if(!NetWorkUtil.isNetworkAvailable(MyApp.context)) {
                            showSnackMsg(getString(R.string.no_network))
                            return@OnItemChildClickListener
                        }
                        val collect = data.collect
                        data.collect = !collect
                        projectAdapter.setData(position, data)
                        if(collect) {
                            mPresenter.cancelCollectArticle(data.id)
                        }else {
                            mPresenter.addCollectArticle(data.id)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }

}