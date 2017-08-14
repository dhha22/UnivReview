package com.univreview.fragment.upload

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.otto.Subscribe
import com.univreview.Navigator
import com.univreview.R
import com.univreview.dialog.RecommendRvDialog
import com.univreview.fragment.BaseWriteFragment
import com.univreview.log.Logger
import com.univreview.model.ActivityResultEvent
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.util.Util
import com.univreview.view.contract.UploadReviewContract
import com.univreview.view.presenter.UploadReviewPresenter
import kotlinx.android.synthetic.main.fragment_upload_review.*
import kotlinx.android.synthetic.main.home_toolbar.*
import kotlinx.android.synthetic.main.upload_review_toolbar.*

/**
 * Created by DavidHa on 2017. 8. 8..
 */
class UploadReviewFragment : BaseWriteFragment(), UploadReviewContract.View {

    lateinit var presenter: UploadReviewPresenter
    override var isReviewExist: Boolean = false


    companion object {
        @JvmStatic
        fun getInstance(): UploadReviewFragment {
            val fragment = UploadReviewFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = UploadReviewPresenter().apply {
            view = this@UploadReviewFragment
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_upload_review, container, false)
        toolbar.visibility = View.GONE
        rootLayout.addView(view)
        return rootLayout
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    fun init() {
        backBtn.setOnClickListener { activity.onBackPressed() }
        okBtn.setOnClickListener { presenter.registerReview() }
        subjectTxt.setOnClickListener { Navigator.goSearch(context, ReviewSearchType.SUBJECT, false) }
        professorTxt.setOnClickListener {
            if (!subjectTxt.text.isEmpty()) {
                if (!isReviewExist) { // review 를 처음 남기는 학생
                    Navigator.goSearch(context, ReviewSearchType.PROF_FROM_SUBJ, presenter.review.subjectId, false)
                } else {
                    showAlertDialog()
                }
            } else {
                Util.simpleMessageDialog(context, "과목을 입력해주세요")
            }
        }

        // rating (difficulty, assignment, attendance, grade, achievement)
        difficultyRate.setOnRatingBarChangeListener { _, rating, _ ->
            presenter.review.difficultyRate = rating
            difficultyTxt.text = presenter.review.difficultyRateMessage
        }
        assignmentRate.setOnRatingBarChangeListener { _, rating, _ ->
            presenter.review.assignmentRate = rating
            assignmentTxt.text = presenter.review.assignmentRateMessage
        }
        attendanceRate.setOnRatingBarChangeListener { _, rating, _ ->
            presenter.review.attendanceRate = rating
            attendanceTxt.text = presenter.review.attendanceRateMessage
        }
        gradeRate.setOnRatingBarChangeListener { _, rating, _ ->
            presenter.review.gradeRate = rating
            gradeTxt.text = presenter.review.gradeRateMessage
        }
        achievementRate.setOnRatingBarChangeListener { _, rating, _ ->
            presenter.review.achievementRate = rating
            achievementTxt.text = presenter.review.achievementRateMessage
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
                Logger.v("on activity result: " + type)
                if (ReviewSearchType.SUBJECT == type) {
                    subjectTxt.text = name
                    professorTxt.text = null
                    presenter.apply {
                        subjectName = name
                        professorName = null
                        review.subjectId = id
                        review.subjectDetailId = 0
                        review.professorId = 0
                        checkReviewExist()
                    }
                } else if (ReviewSearchType.PROF_FROM_SUBJ == type) {
                    val detailId = data.getLongExtra("detailId", 0)
                    presenter.professorName = name
                    professorTxt.text = name
                    presenter.apply {
                        professorName = name
                        review.subjectDetailId = detailId
                        review.professorId = id
                    }
                }
            }
        }
    }

    override fun showAlertDialog() {
        AlertDialog.Builder(context, R.style.customDialog)
                .setMessage("이미 해당 과목 리뷰를 쓰셨습니다.\n다른 과목 리뷰를 써주시길 바랍니다.")
                .setPositiveButton("확인", null)
                .show()
    }

    override fun showRecommendRvDialog() {
        RecommendRvDialog(context, presenter.review).show()
    }


}