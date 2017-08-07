package com.univreview.fragment.login

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.otto.Subscribe
import com.univreview.App
import com.univreview.Navigator
import com.univreview.R
import com.univreview.fragment.BaseFragment
import com.univreview.log.Logger
import com.univreview.model.*
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.util.ImageUtil
import com.univreview.util.Util
import com.univreview.view.contract.RegisterUnivInfoContract
import com.univreview.view.presenter.RegisterUnivInfoPresenter
import kotlinx.android.synthetic.main.fragment_register_univ_info.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 8. 6..
 */
class RegisterUnivInfoFragment : BaseFragment(), RegisterUnivInfoContract.View {


    lateinit var presenter: RegisterUnivInfoPresenter

    companion object {
        private val UNIVERSITY = 0
        private val DEPARTMENT = 1
        private val MAJOR = 2
        private val NEXT = 3

        @JvmStatic
        fun getInstance(register: Register): RegisterUnivInfoFragment {
            val fragment = RegisterUnivInfoFragment()
            val bundle = Bundle()
            bundle.putParcelable("register", register)
            fragment.arguments = bundle
            return fragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = RegisterUnivInfoPresenter().apply {
            view = this@RegisterUnivInfoFragment
            register = arguments.getParcelable("register")
            context = getContext()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_register_univ_info, container, false)
        rootLayout.background = Util.getDrawable(context, R.drawable.cr_login_bg)
        setToolbarTransparent()
        toolbar.setBackBtnVisibility(true)
        rootLayout.addView(view)
        return rootLayout
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        universityTxt.setOnClickListener {
            if (formVerification(UNIVERSITY)) {
                Navigator.goSearch(context, ReviewSearchType.UNIVERSITY, false)
            }
        }

        departmentTxt.setOnClickListener {
            if (formVerification(DEPARTMENT)) {
                Navigator.goSearch(context, ReviewSearchType.DEPARTMENT, presenter.register.universityId, false)
            }
        }

        majorTxt.setOnClickListener {
            if (formVerification(MAJOR)) {
                Navigator.goSearch(context, ReviewSearchType.MAJOR, presenter.register.departmentId, false)
            }
        }
        nextBtn.setOnClickListener {
            if (formVerification(NEXT)) {
                Util.toast("등록")
            }
        }
    }

    private fun formVerification(clickPosition: Int): Boolean {
        if (universityTxt.text.isEmpty()) {
            return showAlertDialog(clickPosition, UNIVERSITY)
        } else if (departmentTxt.text.isEmpty()) {
            return showAlertDialog(clickPosition, DEPARTMENT)
        } else if (majorTxt.text.isEmpty()) {
            return showAlertDialog(clickPosition, MAJOR)
        } else {
            return true
        }
    }

    private fun showAlertDialog(clickPosition: Int, position: Int): Boolean {
        if (clickPosition <= position) {
            return true
        } else {
            if (position == UNIVERSITY) {
                Util.simpleMessageDialog(context, "대학을 선택해주세요.")
            } else if (position == DEPARTMENT) {
                Util.simpleMessageDialog(context, "학과군을 선택해주세요.")
            } else if (position == MAJOR) {
                Util.simpleMessageDialog(context, "전공을 선택해주세요.")
            }
            return false
        }
    }

    @Subscribe
    fun onActivityResult(activityResultEvent: ActivityResultEvent) {
        if (activityResultEvent.resultCode == Activity.RESULT_OK) {
            if (activityResultEvent.requestCode == Navigator.SEARCH) {
                val data = activityResultEvent.intent
                val id = data.getLongExtra("id", 0)
                val name = data.getStringExtra("name")
                val type = data.getSerializableExtra("type") as ReviewSearchType
                Logger.v("on activity result: $type, id: $id, name: $name")
                setSearchResultType(type, id, name)
            }
        }
    }

    private fun setSearchResultType(type: ReviewSearchType, id: Long, name: String) {
        when (type) {
            ReviewSearchType.UNIVERSITY -> {
                universityTxt.text = name
                departmentTxt.text = null
                majorTxt.text = null
                App.universityId = id
                presenter.register.apply {
                    universityId = id
                    departmentId = null
                    majorId = null
                }
                setLineState(universityLine, true)
                setLineState(departmentLine, false)
                setLineState(majorLine, false)
                nextBtn.isSelected = false
            }
            ReviewSearchType.DEPARTMENT -> {
                departmentTxt.text = name
                majorTxt.text = null
                presenter.register.apply {
                    departmentId = id
                    majorId = null
                }
                setLineState(departmentLine, true)
                setLineState(majorLine, false)
                nextBtn.isSelected = false
            }
            ReviewSearchType.MAJOR -> {
                majorTxt.text = name
                presenter.register.majorId = id
                setLineState(majorLine, true)
                nextBtn.isSelected = true
            }
        }
    }

    private fun setLineState(line: View, isSelected: Boolean) {
        if (isSelected) {
            line.setBackgroundColor(Util.getColor(context, R.color.white))
        } else {
            line.setBackgroundColor(Util.getColor(context, R.color.lineDisableColor))
        }
    }


}
