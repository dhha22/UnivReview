package com.univreview.fragment.setting

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dhha22.bindadapter.BindAdapter
import com.univreview.App
import com.univreview.BuildConfig
import com.univreview.Navigator
import com.univreview.R
import com.univreview.fragment.BaseFragment
import com.univreview.log.Logger
import com.univreview.model.Setting
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.view.SettingItemView
import kotlinx.android.synthetic.main.fragment_setting.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by DavidHa on 2017. 9. 20..
 */
class SettingFragment : BaseFragment() {
    private val VERSION = 0
    //private static final int NOTIFICATION = 1;
    private val LOGOUT = 1
    private val USER_DELETE = 2
    private lateinit var adapter: BindAdapter

    companion object {
        @JvmStatic
        fun getInstance(): SettingFragment {
            val fragment = SettingFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_setting, container, false)
        toolbar.setBackBtnVisibility(true)
        toolbar.setTitleTxt("설정")
        rootLayout.addView(view)
        return rootLayout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        val settings: List<Setting>
        if (BuildConfig.DEBUG) {
            settings = Arrays.asList(Setting(0, "버전 정보"), Setting(1, "로그아웃"), Setting(2, "회원탈퇴"))
        } else {
            settings = Arrays.asList(Setting(0, "버전 정보"), Setting(1, "로그아웃"))
            //settings = Arrays.asList(new Setting(0, "버전 정보"), new Setting(1, "알림 설정"), new Setting(2, "로그아웃"));
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = BindAdapter(context).addLayout(SettingItemView::class.java)
        recyclerView.adapter = adapter
        Observable.from(settings)
                .map { setting ->
                    when (setting.id.toInt()) {
                        VERSION -> setting.previewStr = activity.resources.getString(R.string.app_version)
                    }/*case NOTIFICATION:
                                    break;*/
                    setting
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result -> adapter.addItem(result) }, { Logger.e(it) })

        adapter.setOnItemClickListener { _, position ->
            when (position) {
            /* case NOTIFICATION:
                    break;*/
                LOGOUT -> Navigator.goLogin(context)
                USER_DELETE -> deleteUser()
            }
        }
    }

    private fun deleteUser() {
        Retro.instance.loginService.deleteAuth(App.getHeader())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ Navigator.goLogin(context) }, { ErrorUtils.parseError(it) })
    }
}