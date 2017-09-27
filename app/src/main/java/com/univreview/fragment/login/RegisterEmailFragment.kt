package com.univreview.fragment.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.univreview.Navigator
import com.univreview.R
import com.univreview.fragment.BaseFragment
import com.univreview.listener.KeyboardListener
import com.univreview.model.User
import com.univreview.util.Util
import kotlinx.android.synthetic.main.fragment_register_email.*


/**
 * Created by DavidHa on 2017. 9. 24..
 */
class RegisterEmailFragment : BaseFragment() {
    private lateinit var register: User

    companion object {
        @JvmStatic
        fun getInstance(register: User): RegisterEmailFragment {
            val fragment = RegisterEmailFragment()
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
        val view = inflater?.inflate(R.layout.fragment_register_email, container, false)
        toolbar.setTitleTxt("이메일 등록")
        toolbar.setWhiteToolbarStyle()
        rootLayout.addView(view)
        return rootLayout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        inputEmail.addTextChangedListener(textWatcher)
        inputEmail.setText(register.email)
        inputEmail.setSelection(register.email?.length ?: 0)
        inputEmail.setOnKeyListener(KeyboardListener(context, inputEmail))
        nextBtn.isSelected = inputEmail.text.isNotEmpty()
        nextBtn.setOnClickListener {
            if (formVerification() && nextBtn.isSelected) {
                register.email = inputEmail.text.trim().toString()
                Navigator.goRegisterUserInfo(context, register)
            }
        }
    }

    private fun formVerification(): Boolean {
        if (inputEmail.text.isEmpty()) {
            Util.simpleMessageDialog(context, "이메일을 입력해주시길 바랍니다.")
        } else if (!Util.isEmail(inputEmail.text.toString())) {
            Util.simpleMessageDialog(context, "올바른 이메일을 입력해주시길 바랍니다.")
        } else {
            return true
        }
        return false
    }


    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            nextBtn.isSelected = inputEmail.text.trim().isNotEmpty()
        }
    }
}