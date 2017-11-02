package com.univreview.fragment.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.otto.Subscribe
import com.univreview.BuildConfig
import com.univreview.Navigator
import com.univreview.R
import com.univreview.fragment.BaseFragment
import com.univreview.model.ActivityResultEvent
import com.univreview.util.ImageUtil
import com.univreview.util.Util
import kotlinx.android.synthetic.main.fragment_register_student_id.*


/**
 * Created by DavidHa on 2017. 11. 2..
 */
class RegisterStudentIdFragment : BaseFragment() {
    private val CAMERA = "camera"



    companion object {
        fun getInstance(): RegisterStudentIdFragment {
            return RegisterStudentIdFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater!!.inflate(R.layout.fragment_register_student_id, container, false)
        toolbar.setCancelToolbarStyle()
        toolbar.setTitleTxt("학생증 확인")
        rootLayout.addView(view)
        return rootLayout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cameraBtn.setOnClickListener {  Navigator.goPermissionChecker(context, "camera") }
    }

    @Subscribe
    fun onActivityResult(activityResultEvent: ActivityResultEvent) {
        if (activityResultEvent.resultCode == Activity.RESULT_OK) {
            if (activityResultEvent.requestCode == Navigator.PERMISSION_CHECKER) {
                val type = activityResultEvent.intent.getStringExtra("type")
                if ("camera" == type) {
                    Navigator.goCamera(context)
                }
            } else if (activityResultEvent.requestCode == Navigator.CAMERA) {
                val albumPath = ImageUtil.IMAGE_PATH + "tmp.jpg"
                if (BuildConfig.DEBUG) {
                    Util.toast(albumPath)
                }
                Navigator.goCheckUserPhoto(context, CAMERA, albumPath)
                activity.finish()
            }
        }
    }
}