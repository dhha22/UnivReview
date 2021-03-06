package com.univreview.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dhha22.bindadapter.BindAdapter
import com.univreview.Navigator
import com.univreview.R
import com.univreview.log.Logger
import com.univreview.model.Setting
import com.univreview.model.User
import com.univreview.util.Util
import com.univreview.view.SettingItemView
import com.univreview.view.contract.MyPageContract
import com.univreview.view.presenter.MyPagePresenter
import kotlinx.android.synthetic.main.fragment_mypage.*
import kotlinx.android.synthetic.main.my_page_top.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by DavidHa on 2017. 9. 2..
 */
class MyPageFragment : BaseFragment(), MyPageContract.View {

    private lateinit var adapter:BindAdapter
    private val settings = Arrays.asList(Setting(0, "My 리뷰", "0개"), Setting(1, "포인트", "0 point"), Setting(2, "학생 인증"), Setting(3, "설정"))
    private lateinit var presenter: MyPagePresenter


    companion object {
        @JvmStatic
        fun getInstance(): MyPageFragment {
            val fragment = MyPageFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = BindAdapter(context).addLayout(SettingItemView::class.java)
        presenter = MyPagePresenter().apply {
            view = this@MyPageFragment
            context = getContext()
            adapterModel = adapter
            adapterView = adapter
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_mypage, container, false)
        toolbar.visibility = View.GONE
        rootLayout.addView(view)
        return rootLayout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        Observable.from<Setting>(settings)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result -> adapter.addItem(result) }, { Logger.e(it) })

    }

    override fun onResume() {
        super.onResume()
        presenter.callUserProfile()
    }

    override fun setUserData(data: User) {
        nameTxt.text = data.name
        universityTxt.text = data.universityName
        majorTxt.text = data.majorName
        Util.setProfileImage(data.profileImageUrl, profileImage)
        adapter.clearItem()
        Observable.from(settings)
                .map {
                    when (it.id) {
                        0L -> it.previewStr = StringBuilder(data.reviewCount.toString() + "개").toString()
                        1L -> it.previewStr = StringBuilder(data.point.toString() + " point").toString()
                    }
                    it
                }.subscribe({ adapter.addItem(it) }, { Logger.e(it) }, {adapter.notifyData()})
        profileImage.setOnClickListener { Navigator.goProfileEdit(context, data) }
    }

}