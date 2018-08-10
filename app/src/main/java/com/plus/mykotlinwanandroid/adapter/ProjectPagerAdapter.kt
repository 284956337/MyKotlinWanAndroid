package com.plus.mykotlinwanandroid.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import com.cxz.wanandroid.mvp.model.bean.ProjectTreeBean
import com.plus.mykotlinwanandroid.ui.fragment.ProjectListFragment

/**
 * Created by 180311 on 2018/8/9.
 */
class ProjectPagerAdapter(private val list: MutableList<ProjectTreeBean>, fm: FragmentManager?)
    : FragmentStatePagerAdapter(fm) {

    private val fragments = mutableListOf<Fragment>()

    init {
        fragments.clear()
        list.forEach {
            fragments.add(ProjectListFragment.getInstance(it.id))
        }
    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = list.size

    override fun getPageTitle(position: Int): CharSequence? {
        return list[position].name
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

}