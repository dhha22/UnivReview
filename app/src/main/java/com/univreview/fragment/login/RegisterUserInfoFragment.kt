package com.univreview.fragment.login

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.otto.Subscribe
import com.univreview.Navigator
import com.univreview.R
import com.univreview.fragment.BaseFragment
import com.univreview.log.Logger
import com.univreview.model.ActivityResultEvent
import com.univreview.model.Register
import com.univreview.util.ImageUtil
import com.univreview.util.Util
import kotlinx.android.synthetic.main.fragment_register_user_info.*

/**
 * Created by DavidHa on 2017. 8. 5..
 */
class RegisterUserInfoFragment : BaseFragment() {
    private lateinit var register : Register
    companion object {
        @JvmStatic
        fun newInstance(register: Register): RegisterUserInfoFragment {
            val fragment = RegisterUserInfoFragment()
            val bundle = Bundle()
            bundle.putParcelable("register", register)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        register = arguments.getParcelable("register")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_register_user_info, container, false)
        setToolbarTransparent()
        toolbar.setBackBtnVisibility(true)
        rootLayout.background = Util.getDrawable(context, R.drawable.cr_login_bg)
        rootLayout.addView(view)
        return rootLayout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(register)
        setData(register)
    }

    fun init(register: Register) {
        nextBtn.setOnClickListener { _ ->
            if (formVerification() && nextBtn.isSelected) {
                Navigator.goRegisterUnivInfo(context, register)
            }
        }

        inputName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                nextBtn.isSelected = !s.isEmpty()
            }
        })
    }



    fun setData(register: Register?){
        register?.let {
            Util.setProfileImage(it.profileUrl, profileImage)
            inputName.setText(it.nickName)
            inputName.setSelection(it.nickName.length)
            nextBtn.isSelected = !it.nickName.isEmpty()
        }
    }

    private fun formVerification(): Boolean {
        if (inputName.text.isEmpty()) {
            Util.simpleMessageDialog(context, "이름을 입력해주시길 바랍니다.")
        } else if(inputEmail.text.isEmpty()){
            Util.simpleMessageDialog(context, "이메일을 입력해주시길 바랍니다.")
        }else if(!Util.isEmail(inputEmail.text.toString())){
            Util.simpleMessageDialog(context, "올바른 이메일을 입력해주시길 바랍니다.")
        }else {
            return true
        }
        return false
    }

    @Subscribe
    fun onActivityResult(activityResultEvent: ActivityResultEvent){
        if (activityResultEvent.resultCode == Activity.RESULT_OK) {
            if (activityResultEvent.requestCode == Navigator.PERMISSION_CHECKER) {
                Navigator.goAlbum(context)
            } else if (activityResultEvent.requestCode == Navigator.ALBUM) {
                val albumPath = ImageUtil.getPath(activityResultEvent.intent.data)
                Logger.v("album path: " + albumPath)
                register.profileUrl = "file://" + albumPath
                register.profileUri = activityResultEvent.intent.data
                setData(register)
            }
        }
    }
}