package com.univreview.fragment.login

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dhha22.permissionchecker.PermissionsChecker
import com.dhha22.permissionchecker.listener.PermissionListener
import com.univreview.Navigator
import com.univreview.R
import com.univreview.fragment.BaseFragment
import com.univreview.log.Logger
import com.univreview.model.ActivityResultEvent
import com.univreview.util.ImageUtil
import kotlinx.android.synthetic.main.fragment_register_student_id.*


/**
 * Created by DavidHa on 2017. 11. 2..
 */
class RegisterStudentIdFragment : BaseFragment(), PermissionListener {
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
        cameraBtn.setOnClickListener {
            PermissionsChecker(context)
                    .setListener(this)
                    .addPermission(Manifest.permission.CAMERA)
                    .checkPermission()
        }
    }

    override fun denyPermission(p0: MutableList<String>?) {
        showCameraAuthDialog()
    }

    override fun grantPermission() {
        Navigator.goCamera(context)

    }

    override fun onActivityResult(activityResultEvent: ActivityResultEvent) {
        super.onActivityResult(activityResultEvent)
        if (activityResultEvent.resultCode == Activity.RESULT_OK) {
            if (activityResultEvent.requestCode == Navigator.CAMERA) {
                val albumPath = ImageUtil.IMAGE_PATH + "tmp.jpg"
                Logger.v("album path: $albumPath")
                Navigator.goCheckUserPhoto(context, CAMERA, albumPath)
                activity.finish()
            }
        }
    }


}