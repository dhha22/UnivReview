package com.univreview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.univreview.activity.LoginActivity
import com.univreview.activity.MainActivity
import com.univreview.activity.NavigationActivity
import com.univreview.fragment.PointListFragment
import com.univreview.fragment.ProfileEditFragment
import com.univreview.fragment.SearchFragment
import com.univreview.fragment.TestFragment
import com.univreview.fragment.login.*
import com.univreview.fragment.review.ReviewDetailFragment
import com.univreview.fragment.review.ReviewListFragment
import com.univreview.fragment.setting.SettingFragment
import com.univreview.fragment.upload.UploadReviewDetailFragment
import com.univreview.fragment.upload.UploadReviewFragment
import com.univreview.model.Register
import com.univreview.model.Review
import com.univreview.model.User
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.util.ImageUtil
import java.io.File

/**
 * Created by DavidHa on 2017. 10. 10..
 */
class Navigator {
    companion object {
        val SEARCH = 636
        val CAMERA = 387
        val ALBUM = 549

        val PERMISSION_CHECKER = 405

        @JvmStatic
        fun goLogin(context: Context) {
            App.userLogout()
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
            (context as Activity).onBackPressed()
        }

        //register

        fun goRegisterEmail(context: Context, register: Register) {
            val intent = Intent(context, NavigationActivity::class.java)
            NavigationActivity.fragment = RegisterEmailFragment.getInstance(register)
            context.startActivity(intent)
        }


        fun goRegisterUserInfo(context: Context, register: Register) {
            val intent = Intent(context, NavigationActivity::class.java)
            NavigationActivity.fragment = RegisterUserInfoFragment.newInstance(register)
            context.startActivity(intent)
        }

        fun goRegisterUnivInfo(context: Context, register: Register) {
            val intent = Intent(context, NavigationActivity::class.java)
            NavigationActivity.fragment = RegisterUnivInfoFragment.getInstance(register)
            context.startActivity(intent)
        }

        fun goRegisterStudentId(context: Context) {
            val intent = Intent(context, NavigationActivity::class.java)
            NavigationActivity.fragment = RegisterStudentIdFragment.getInstance()
            context.startActivity(intent)
        }


        fun goUserAuthCompleted(context: Context) {
            val intent = Intent(context, NavigationActivity::class.java)
            NavigationActivity.fragment = UserAuthCompletedFragment.getInstance()
            context.startActivity(intent)
        }


        fun goCamera(context: Context) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val filePath = File(ImageUtil.IMAGE_PATH)
            if (!filePath.isDirectory) {
                filePath.mkdirs()
            }
            val file = File(ImageUtil.IMAGE_PATH + "tmp.jpg")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, "com.univreview.provider", file))
            (context as Activity).startActivityForResult(intent, CAMERA)
        }

        fun goAlbum(context: Context) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            (context as Activity).startActivityForResult(intent, ALBUM)
        }


        //upload

        fun goUploadReview(context: Context) {
            val intent = Intent(context, NavigationActivity::class.java)
            NavigationActivity.fragment = UploadReviewFragment.getInstance()
            context.startActivity(intent)
        }

        fun goUploadReviewDetail(context: Context, review: Review, isFirst: Boolean = false) {
            val intent = Intent(context, NavigationActivity::class.java)
            NavigationActivity.fragment = UploadReviewDetailFragment.getInstance(review, isFirst)
            context.startActivity(intent)
        }



        //main

        fun goMain(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        //review
        fun goReviewList(context: Context, type: ReviewSearchType, id: Long, name: String) {
            val intent = Intent(context, NavigationActivity::class.java)
            NavigationActivity.fragment = ReviewListFragment.getInstance(type, id, name)
            context.startActivity(intent)
        }

        fun goReviewDetail(context: Context, data: Review) {
            val intent = Intent(context, NavigationActivity::class.java)
            NavigationActivity.fragment = ReviewDetailFragment.getInstance(data)
            context.startActivity(intent)
        }


        //search

        fun goSearch(context: Context, type: ReviewSearchType) {
            val intent = Intent(context, NavigationActivity::class.java)
            NavigationActivity.fragment = SearchFragment.getInstance(type, 0)
            (context as Activity).startActivityForResult(intent, SEARCH)
        }


        fun goSearch(context: Context, type: ReviewSearchType, id: Long?) {
            val intent = Intent(context, NavigationActivity::class.java)
            NavigationActivity.fragment = SearchFragment.getInstance(type, id!!)
            (context as Activity).startActivityForResult(intent, SEARCH)
        }


        fun goCheckStudentId(context: Context, type: String, path: String) {
            val intent = Intent(context, NavigationActivity::class.java)
            NavigationActivity.fragment = CheckStudentIdFragment.getInstance(type, path)
            context.startActivity(intent)
        }


        //mypage
        fun goProfileEdit(context: Context, user: User) {
            val intent = Intent(context, NavigationActivity::class.java)
            NavigationActivity.fragment = ProfileEditFragment.getInstance(user)
            context.startActivity(intent)
        }

        fun goPointList(context: Context, point: Int=0) {
            val intent = Intent(context, NavigationActivity::class.java)
            NavigationActivity.fragment = PointListFragment.getInstance(point)
            context.startActivity(intent)
        }

        fun goSetting(context: Context) {
            val intent = Intent(context, NavigationActivity::class.java)
            NavigationActivity.fragment = SettingFragment.getInstance()
            context.startActivity(intent)
        }


        fun goAppSetting(context: Context) {
            val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = Uri.parse("package:" + context.packageName)
            context.startActivity(intent)
            // activity.overridePendingTransition(R.anim.in_from_left, R.anim.out_to_left);
        }

        @JvmStatic
        fun goGooglePlayStore() {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.univreview"))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            App.getCurrentActivity().startActivity(intent)
            App.getCurrentActivity().finish()
        }

        fun goTest(context: Context) {
            val intent = Intent(context, NavigationActivity::class.java)
            NavigationActivity.fragment = TestFragment.getInstance()
            context.startActivity(intent)
        }

    }
}