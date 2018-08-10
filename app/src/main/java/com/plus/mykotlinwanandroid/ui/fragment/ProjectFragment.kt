package com.plus.mykotlinwanandroid.ui.fragment

import android.support.design.widget.TabLayout
import com.cxz.wanandroid.mvp.model.bean.ProjectTreeBean
import com.cxz.wanandroid.utils.SettingUtil
import com.plus.mykotlinwanandroid.R
import com.plus.mykotlinwanandroid.adapter.ProjectPagerAdapter
import com.plus.mykotlinwanandroid.base.BaseFragment
import com.plus.mykotlinwanandroid.event.ColorEvent
import com.plus.mykotlinwanandroid.mvp.contract.ProjectContract
import com.plus.mykotlinwanandroid.mvp.presenter.ProjectPresenter
import com.plus.mykotlinwanandroid.widget.helper.TabLayoutHelper
import kotlinx.android.synthetic.main.fragment_project.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by 180311 on 2018/8/9.
 */
class ProjectFragment : BaseFragment(), ProjectContract.View {

    companion object {
        fun getInstance() = ProjectFragment()
    }

    private val mPresenter: ProjectPresenter by lazy {
        ProjectPresenter()
    }

    /**
     *
     */
    private var projectTree = mutableListOf<ProjectTreeBean>()

    /**
     * ViewPagerAdapter
     */
    private val viewPagerAdapter: ProjectPagerAdapter by lazy {
        ProjectPagerAdapter(projectTree, fragmentManager)
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_project

    override fun useEventBus(): Boolean = true

    override fun initView() {
        mPresenter.attachView(this)

        viewPager.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        }

        tabLayout.run {
            setupWithViewPager(viewPager)
            TabLayoutHelper.setUpIndicatorWidth(tabLayout)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
        }

        refreshColor(ColorEvent(true))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if(event.isRefresh) {
            if(!SettingUtil.getIsNightMode()) {
                tabLayout.setBackgroundColor(SettingUtil.getColor())
            }
        }
    }

    override fun lazyLoad() {
        mPresenter.requestProjectTree()
    }

    override fun doReConnected() {
        if (projectTree.size == 0) {
            super.doReConnected()
        }
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(errorMsg: String) {
    }

    override fun scrollToTop() {
        if(viewPagerAdapter.count == 0) {
            return
        }
        val fragment: ProjectListFragment = viewPagerAdapter.getItem(viewPager.currentItem) as ProjectListFragment
        fragment.scrollToTop()
    }

    override fun setProjectTree(list: List<ProjectTreeBean>) {
        list.let {
            projectTree.addAll(it)
            viewPager.run {
                adapter = viewPagerAdapter
                offscreenPageLimit = projectTree.size
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}