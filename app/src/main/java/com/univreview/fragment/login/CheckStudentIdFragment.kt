package com.univreview.fragment.login

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.univreview.App
import com.univreview.Navigator
import com.univreview.R
import com.univreview.fragment.BaseFragment
import com.univreview.log.Logger
import com.univreview.model.ActivityResultEvent
import com.univreview.util.ImageUtil
import kotlinx.android.synthetic.main.fragment_check_user_photo.*

/**
 * Created by DavidHa on 2017. 11. 21..
 */
class CheckStudentIdFragment : BaseFragment() {
    private lateinit var type: String
    private lateinit var path: String

    companion object {
        fun getInstance(type: String, path: String): CheckStudentIdFragment {
            val fragment = CheckStudentIdFragment()
            val bundle = Bundle()
            bundle.putString("type", type)
            bundle.putString("path", path)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        type = arguments.getString("type")
        path = arguments.getString("path")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater!!.inflate(R.layout.fragment_check_user_photo, container, false)
        toolbar.setTitleTxt("학생증 확인")
        toolbar.setCancelToolbarStyle()
        //reselectBtn.setOnClickListener();
        setCheckImage(path)
        okBtn.setOnClickListener({ v -> upload() })
        rootLayout.addView(view)
        return rootLayout
    }

    private fun upload() {
        Logger.v("upload path: " + path)
        /* Retro.instance.fileService(path, "studentCard")
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    Navigator.goUserAuthCompleted(getContext());
                    getActivity().finish();
                })
                .subscribe(result -> Logger.v("result: " + result), error -> Logger.e(error));*/
    }


    public override fun onActivityResult(activityResultEvent: ActivityResultEvent) {
        if (activityResultEvent.resultCode == Activity.RESULT_OK) {
            if (activityResultEvent.requestCode == Navigator.PERMISSION_CHECKER) {
                val type = activityResultEvent.intent.getStringExtra("type")
                if ("camera" == type) {
                    Navigator.goCamera(context)
                }
            } else if (activityResultEvent.requestCode == Navigator.CAMERA) {
                path = ImageUtil.IMAGE_PATH + "tmp.jpg"
                setCheckImage(path)
            }
        }
    }

    private fun setCheckImage(path: String) {
        Logger.v("path: " + path)
        App.picasso.invalidate("file://" + path)
        App.picasso.load("file://" + path)
                .fit()
                .centerCrop()
                .into(checkImage)
    }
}