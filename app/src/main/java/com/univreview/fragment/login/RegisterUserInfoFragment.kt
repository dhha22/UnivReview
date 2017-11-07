package com.univreview.fragment.login

import android.Manifest
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dhha22.permissionchecker.PermissionsChecker
import com.dhha22.permissionchecker.listener.PermissionListener
import com.univreview.Navigator
import com.univreview.R
import com.univreview.fragment.BaseFragment
import com.univreview.listener.KeyboardListener
import com.univreview.log.Logger
import com.univreview.model.ActivityResultEvent
import com.univreview.model.Register
import com.univreview.network.Retro
import com.univreview.util.ErrorUtils
import com.univreview.util.ImageUtil
import com.univreview.util.Util
import kotlinx.android.synthetic.main.fragment_register_user_info.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by DavidHa on 2017. 8. 5..
 */
class RegisterUserInfoFragment : BaseFragment(), PermissionListener {
    private lateinit var register: Register

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
        toolbar.setTitleTxt("계정 등록")
        toolbar.setWhiteToolbarStyle()
        rootLayout.addView(view)
        return rootLayout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(register)
    }

    private fun init(register: Register) {
        Util.setProfileImage(register.profileImageUrl, profileImage)
        inputName.addTextChangedListener(textWatcher)
        inputName.setOnKeyListener(KeyboardListener(context, inputName))
        profileImage.setOnClickListener {
            PermissionsChecker(context)
                    .setListener(this)
                    .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .checkPermission()
        }

        nextBtn.setOnClickListener { _ ->
            if (formVerification() && nextBtn.isSelected) {
                validateName(inputName.text.toString())
            }
        }

    }


    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            nextBtn.isSelected = inputName.text.isNotEmpty()
        }
    }


    private fun formVerification(): Boolean {
        if (inputName.text.isEmpty()) {
            Util.simpleMessageDialog(context, "이름을 입력해주시길 바랍니다.")
        } else {
            return true
        }
        return false
    }


    private fun validateName(name : String){
        Retro.instance.loginService.validateName(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({if(it.data.isBoundName) {
                    Util.simpleMessageDialog(context, "이름을 중복됩니다.")
                }else{
                    register.name = name
                    Navigator.goRegisterUnivInfo(context, register)
                }}, { ErrorUtils.parseError(it) })
    }


    override fun denyPermission(p0: MutableList<String>?) {
        showWriteStorageAuthDialog()
    }

    override fun grantPermission() {
        Navigator.goAlbum(context)
    }

    override fun onActivityResult(activityResultEvent: ActivityResultEvent) {
        super.onActivityResult(activityResultEvent)
        if (activityResultEvent.requestCode == Navigator.ALBUM) {
            val albumPath = ImageUtil.getPath(activityResultEvent.intent.data)
            Logger.v("album path: " + albumPath)
            Util.setProfileImage("file://" + albumPath, profileImage)
            register.profileImageUri = activityResultEvent.intent.data
        }
    }

}