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
import com.univreview.model.model_kotlin.User
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
        private val MAJOR = 1
        private val NEXT = 2

        @JvmStatic
        fun getInstance(register: User): RegisterUnivInfoFragment {
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
        toolbar.setLoginToolbarStyle()
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


        majorTxt.setOnClickListener {
            if (formVerification(MAJOR)) {
                Navigator.goSearch(context, ReviewSearchType.MAJOR, presenter.register.departmentId, false)
            }
        }
        nextBtn.setOnClickListener {
            if (formVerification(NEXT)) {
               presenter.registerUser()
            }
        }
    }

    private fun formVerification(clickPosition: Int): Boolean {
        if (universityTxt.text.isEmpty()) {
            return showAlertDialog(clickPosition, UNIVERSITY)
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
            }  else if (position == MAJOR) {
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
                majorTxt.text = null
                App.universityId = id
                presenter.register.apply {
                    universityId = id
                    majorId = null
                }
                nextBtn.isSelected = false
            }

            ReviewSearchType.MAJOR -> {
                majorTxt.text = name
                presenter.register.majorId = id
                nextBtn.isSelected = true
            }
        }
    }


}
