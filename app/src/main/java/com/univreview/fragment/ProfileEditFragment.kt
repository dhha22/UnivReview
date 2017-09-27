package com.univreview.fragment

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
import com.univreview.log.Logger
import com.univreview.model.ActivityResultEvent
import com.univreview.model.User
import com.univreview.util.ImageUtil
import com.univreview.util.Util
import com.univreview.view.contract.ProfileEditContract
import com.univreview.view.presenter.ProfileEditPresenter
import kotlinx.android.synthetic.main.fragment_profile_edit.*


/**
 * Created by DavidHa on 2017. 9. 25..
 */
class ProfileEditFragment : BaseFragment(), ProfileEditContract.View {

    private lateinit var user: User
    private lateinit var presenter: ProfileEditPresenter

    companion object {
        @JvmStatic
        fun getInstance(user: User): ProfileEditFragment {
            val fragment = ProfileEditFragment()
            val bundle = Bundle()
            bundle.putParcelable("user", user)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = arguments.getParcelable("user")
        presenter = ProfileEditPresenter().apply {
            view = this@ProfileEditFragment
            context = getContext()
            userName = user.name!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_profile_edit, container, false)
        toolbar.setWhiteToolbarStyle()
        toolbar.setTitleTxt("내 정보 수정")
        rootLayout.addView(view)
        return rootLayout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }


    private fun init() {
        profileImage.setOnClickListener { Navigator.goPermissionChecker(context, "album") }
        saveBtn.setOnClickListener {
            if (formVerification() && saveBtn.isSelected) {
                presenter.userName = inputName.text.toString().trim()
                presenter.saveUserInfo()
            }
        }
        inputName.addTextChangedListener(textWatcher)
        Util.setProfileImage(user.profileImageUrl, profileImage)
        inputName.setText(user.name)
        inputName.setSelection(inputName.text.length)
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            saveBtn.isSelected = inputName.text.isNotEmpty()
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

    @Subscribe
    fun onActivityResult(activityResultEvent: ActivityResultEvent) {
        Logger.v("on activity result")
        if (activityResultEvent.resultCode == Activity.RESULT_OK) {
            if (activityResultEvent.requestCode == Navigator.PERMISSION_CHECKER) {
                Navigator.goAlbum(context)
            } else if (activityResultEvent.requestCode == Navigator.ALBUM) {
                val albumPath = ImageUtil.getPath(activityResultEvent.intent.data)
                Logger.v("album path: file://" + albumPath)
                Util.setProfileImage("file://" + albumPath, profileImage)
                presenter.imagePath = albumPath
            }
        }
    }

}