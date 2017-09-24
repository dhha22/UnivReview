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
import com.univreview.model.ActivityResultEvent
import com.univreview.model.enumeration.ReviewSearchType
import com.univreview.model.Review
import com.univreview.util.Util
import com.univreview.view.contract.UploadReviewContract
import com.univreview.view.presenter.UploadReviewPresenter
import kotlinx.android.synthetic.main.fragment_upload_review.*
import kotlinx.android.synthetic.main.upload_review_toolbar.*

/**
 * Created by DavidHa on 2017. 8. 8..
 */
class UploadReviewFragment : BaseWriteFragment(), UploadReviewContract.View {

    lateinit var presenter: UploadReviewPresenter

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
        subjectTxt.setOnClickListener { Navigator.goSearch(context, ReviewSearchType.SUBJECT_WITH_RESULT) }
        professorTxt.setOnClickListener {
            if (!subjectTxt.text.isEmpty()) {
                Navigator.goSearch(context, ReviewSearchType.PROF_FROM_SUBJ, presenter.review.subjectId)
            } else {
                Util.simpleMessageDialog(context, "과목을 입력해주세요")
            }
        }

        // rating (difficulty, assignment, attendance, grade, achievement)
        difficultyRate.setOnRatingBarChangeListener { _, rating, _ ->
            presenter.review.difficultyRate = rating
            difficultyTxt.text = presenter.review.getDifficultyRateMessage()
        }
        assignmentRate.setOnRatingBarChangeListener { _, rating, _ ->
            presenter.review.assignmentRate = rating
            assignmentTxt.text = presenter.review.getAssignmentRateMessage()
        }
        attendanceRate.setOnRatingBarChangeListener { _, rating, _ ->
            presenter.review.attendanceRate = rating
            attendanceTxt.text = presenter.review.getAttendanceRateMessage()
        }
        gradeRate.setOnRatingBarChangeListener { _, rating, _ ->
            presenter.review.gradeRate = rating
            gradeTxt.text = presenter.review.getGradeRateMessage()
        }
        achievementRate.setOnRatingBarChangeListener { _, rating, _ ->
            presenter.review.achievementRate = rating
            achievementTxt.text = presenter.review.getAchievementRateMessage()
        }
    }

    @Subscribe
    fun onActivityResult(activityResultEvent: ActivityResultEvent) {
        if (activityResultEvent.resultCode == Activity.RESULT_OK) {
            if (activityResultEvent.requestCode == Navigator.SEARCH) {
                presenter.onActivityResult(activityResultEvent.intent)
            }
        }
    }

    override fun setSubjectTxt(str: String?) {
        subjectTxt.text = str
    }

    override fun setProfessorTxt(str: String?) {
        professorTxt.text = str
    }

    override fun showAlertDialog() {
        professorTxt.text = null
        AlertDialog.Builder(context, R.style.customDialog)
                .setMessage("이미 해당 과목 리뷰를 쓰셨습니다.\n다른 과목 리뷰를 써주시길 바랍니다.")
                .setPositiveButton("확인", null)
                .show()
    }

    override fun showRecommendRvDialog(review: Review) {
        RecommendRvDialog(context, review).show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}