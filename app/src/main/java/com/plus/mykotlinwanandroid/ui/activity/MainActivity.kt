package com.plus.mykotlinwanandroid.ui.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.cxz.wanandroid.constant.Constant
import com.plus.mykotlinwanandroid.R
import com.plus.mykotlinwanandroid.R.id.*
import com.plus.mykotlinwanandroid.R.string.username
import com.plus.mykotlinwanandroid.base.BaseActivity
import com.plus.mykotlinwanandroid.event.ColorEvent
import com.plus.mykotlinwanandroid.event.LoginEvent
import com.plus.mykotlinwanandroid.event.RefreshHomeEvent
import com.plus.mykotlinwanandroid.ext.showToast
import com.plus.mykotlinwanandroid.ui.fragment.HomeFragment
import com.plus.mykotlinwanandroid.ui.fragment.ProjectFragment
import com.plus.mykotlinwanandroid.util.Preference
import com.plus.mykotlinwanandroid.widget.helper.BottomNavigationViewHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by 180311 on 2018/8/8.
 */
class MainActivity : BaseActivity() {

    private val FRAGMENT_HOME = 0x01
    private val FRAGMENT_KNOWLEDGE = 0x02
    private val FRAGMENT_NAVIGATION = 0x03
    private val FRAGMENT_PROJECT = 0x04

    private var mIndex = FRAGMENT_HOME

    private var mHomeFragment: HomeFragment? = null
    private var mKnowledgeTreeFragment = null
    private var mNavigationFragment = null
    private var mProjectFragment: ProjectFragment? = null

    private val username: String by Preference(Constant.USERNAME_KEY, "")
    private lateinit var nav_username: TextView

    override fun attachLayoutResId(): Int = R.layout.activity_main

    override fun initData() {
    }

    override fun initView() {
        toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
        }

        bottom_navigation.run {
            BottomNavigationViewHelper.disableShiftMode(this)
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
            selectedItemId = R.id.action_home
        }

        initDrawerLayout()

        nav_view.run {
            setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)
            nav_username = getHeaderView(0).findViewById(R.id.tv_username)
            menu.findItem(R.id.nav_logout).isVisible = isLogin
        }
        nav_username.run {
            text = if(!isLogin) {
                getString(R.string.login)
            }else {
                username
            }
            setOnClickListener({
                if(!isLogin) {
                    Intent(this@MainActivity, LoginActivity::class.java).run {
                        startActivity(this)
                    }
                }
            })
        }

        showFragment(mIndex)

        floating_action_btn.run {
            setOnClickListener(onFABClickListener)
        }
    }

    override fun start() {
    }

    override fun initColor() {
        super.initColor()
        refreshColor(ColorEvent(true))
    }

    private fun initDrawerLayout() {
        drawer_layout.run {
            var toggle = ActionBarDrawerToggle(this@MainActivity,
                    this,
                    toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close)
            addDrawerListener(toggle)
            toggle.syncState()
        }
    }

    /**
     * 展示Fragment
     */
    private fun showFragment(index: Int){
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        mIndex = index
        when (index) {
            FRAGMENT_HOME -> { // 首页
                toolbar.title = getString(R.string.app_name)
                if (mHomeFragment == null) {
                    mHomeFragment = HomeFragment.getInstance()
                    transaction.add(R.id.container, mHomeFragment, "home")
                } else {
                    transaction.show(mHomeFragment)
                }
            }
            FRAGMENT_KNOWLEDGE -> {

            }
            FRAGMENT_NAVIGATION -> {

            }
            FRAGMENT_PROJECT -> {
                toolbar.title = getString(R.string.project)
                if (mProjectFragment == null) {
                    mProjectFragment = ProjectFragment.getInstance()
                    transaction.add(R.id.container, mProjectFragment, "project")
                } else {
                    transaction.show(mProjectFragment)
                }
            }
        }
        transaction.commit()
    }

    /**
     * 隐藏所有的Fragment
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        mHomeFragment?.let { transaction.hide(it) }
        mKnowledgeTreeFragment?.let { transaction.hide(it) }
        mNavigationFragment?.let { transaction.hide(it) }
        mProjectFragment?.let { transaction.hide(it) }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: LoginEvent) {
        if(event.isLogin) {
            nav_username.text = username
            nav_view.menu.findItem(R.id.nav_logout).isVisible = true
            mHomeFragment?.lazyLoad()
        }else {
            nav_username.text = getString(R.string.login)
            nav_view.menu.findItem(R.id.nav_logout).isVisible = false
            mHomeFragment?.lazyLoad()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshHomeEvent(event: RefreshHomeEvent) {
        if(event.isRefresh) {
            mHomeFragment?.lazyLoad()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if(event.isRefresh) {
            nav_view.getHeaderView(0).setBackgroundColor(mThemeColor)
            floating_action_btn.backgroundTintList = ColorStateList.valueOf(mThemeColor)
        }
    }

    /**
     * NavigationView 监听  (侧滑菜单 事件 )
     */
    private val onDrawerNavigationItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_collect -> {
                    if(isLogin) {
                        Intent(this@MainActivity, CommonActivity::class.java).run {
                            putExtra(Constant.TYPE_KEY, Constant.Type.COLLECT_TYPE_KEY)
                            startActivity(this)
                        }
                    }else {
                        showToast(getString(R.string.login_tint))
                        Intent(this@MainActivity, LoginActivity::class.java).run {
                            startActivity(this)
                        }
                    }
                    drawer_layout.closeDrawer(GravityCompat.START)
                }
                R.id.nav_setting -> {

                }
                R.id.nav_about_us -> {

                }
                R.id.nav_logout -> {

                }
                R.id.nav_night_mode -> {

                }
                R.id.nav_todo -> {

                }
            }
            true
        }

    /**
     * NavigationItemSelect监听  (底部导航栏事件 )
     */
    private val onNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                return@OnNavigationItemSelectedListener when(item.itemId){
                    R.id.action_home -> {
                        showFragment(FRAGMENT_HOME)
                        true
                    }
                    R.id.action_knowledge_system -> {
                        showFragment(FRAGMENT_KNOWLEDGE)
                        true
                    }
                    R.id.action_navigation -> {
                        showFragment(FRAGMENT_NAVIGATION)
                        true
                    }
                    R.id.action_project -> {
                        showFragment(FRAGMENT_PROJECT)
                        true
                    }
                    else -> false
                }
            }

    /**
     * FAB 监听  （toTop）
     */
    private val onFABClickListener = View.OnClickListener {
        when(mIndex) {
            FRAGMENT_HOME -> {
                mHomeFragment?.scrollToTop()
            }
            FRAGMENT_KNOWLEDGE -> {

            }
            FRAGMENT_PROJECT -> {
                mProjectFragment?.scrollToTop()
            }
            FRAGMENT_NAVIGATION -> {

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_search -> {
                Intent(this@MainActivity, SearchActivity::class.java).run {
                    startActivity(this)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun recreate() {
        bottom_navigation.selectedItemId = R.id.action_home
        try {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (mProjectFragment != null){
                fragmentTransaction.remove(mProjectFragment)
            }
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: Exception) {
        }
        super.recreate()
    }

    private var mExitTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(System.currentTimeMillis().minus(mExitTime) <= 2000) {
                finish()
            }else {
                mExitTime = System.currentTimeMillis()
                showToast(getString(R.string.exit_tip))
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}