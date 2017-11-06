package com.univreview.model

import android.os.Parcel
import android.os.Parcelable
import com.dhha22.bindadapter.Item
import com.univreview.log.Logger
import rx.subjects.PublishSubject

/**
 * Created by DavidHa on 2017. 8. 27..
 */
data class Review(var id: Long = 0,
                  var difficultyRate: Float = 0f,
                  var assignmentRate: Float = 0f,
                  var attendanceRate: Float = 0f,
                  var gradeRate: Float = 0f,
                  var achievementRate: Float = 0f,
                  var courseId: Long = 0,
                  var subjectId: Long = 0,
                  var content: String? = null,
                  var likeCount: Long = 0,
                  var commentCount: Long = 0,
                  val reported: Boolean = false,
                  val createdAt: String? = null,
                  val user: User? = null,
                  val subject: Subject? = null,
                  val professor: Professor? = null,
                  var isLike: Boolean = false) :  Parcelable, Item {


    val updateNotificationPublisher:PublishSubject<Review> = PublishSubject.create()

    fun notifyUpdate(){
        Logger.v("update item: $updateNotificationPublisher")
        updateNotificationPublisher.onNext(this)
    }



    fun getDifficultyRateMessage(): String {
        when (Math.round(difficultyRate)) {
            1 -> return "아주 쉬움"
            2 -> return "쉬움"
            3 -> return "보통"
            4 -> return "어려움"
            5 -> return "매우 어려움"
            else -> return ""
        }

    }

    fun getAssignmentRateMessage(): String {
        when (Math.round(assignmentRate)) {
            1 -> return "아주 적음"
            2 -> return "적음"
            3 -> return "보통"
            4 -> return "많음"
            5 -> return "매우 많음"
            else -> return ""
        }
    }

    fun getAttendanceRateMessage(): String {
        when (Math.round(attendanceRate)) {
            1 -> return "거의 드묾"
            2 -> return "드묾"
            3 -> return "보통"
            4 -> return "잦음"
            5 -> return "너무 잦음"
            else -> return ""
        }
    }

    fun getGradeRateMessage(): String {
        when (Math.round(gradeRate)) {
            1 -> return "아주 쉬움"
            2 -> return "쉬움"
            3 -> return "보통"
            4 -> return "어려움"
            5 -> return "매우 어려움"
            else -> return ""
        }
    }

    fun getAchievementRateMessage(): String {
        when (Math.round(achievementRate)) {
            1 -> return "매우 불만족"
            2 -> return "불만족"
            3 -> return "보통"
            4 -> return "만족"
            5 -> return "매우 만족"
            else -> return ""
        }
    }

    fun getAlertMessage(): String? {
        if (subjectId == 0L) {
            return "과목을 입력해주세요."
        } else if (courseId == 0L) {
            return "교수명을 입력해주세요."
        } else if (difficultyRate == 0f) {
            return "난이도를 평가해주세요."
        } else if (assignmentRate == 0f) {
            return "과제량을 평가해주세요."
        } else if (attendanceRate == 0f) {
            return "출석체크를 평가해주세요."
        } else if (gradeRate == 0f) {
            return "학점을 평가해주세요."
        } else if (achievementRate == 0f) {
            return "성취감을 평가해주세요."
        } else {
            return null
        }
    }

    constructor(source: Parcel) : this(
            source.readLong(),
            source.readFloat(),
            source.readFloat(),
            source.readFloat(),
            source.readFloat(),
            source.readFloat(),
            source.readLong(),
            source.readLong(),
            source.readString(),
            source.readLong(),
            source.readLong(),
            1 == source.readInt(),
            source.readString(),
            source.readParcelable<User>(User::class.java.classLoader),
            source.readParcelable<Subject>(Subject::class.java.classLoader),
            source.readParcelable<Professor>(Professor::class.java.classLoader),
            source.readInt() != 0
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeFloat(difficultyRate)
        writeFloat(assignmentRate)
        writeFloat(attendanceRate)
        writeFloat(gradeRate)
        writeFloat(achievementRate)
        writeLong(courseId)
        writeLong(subjectId)
        writeString(content)
        writeLong(likeCount)
        writeLong(commentCount)
        writeInt((if (reported) 1 else 0))
        writeString(createdAt)
        writeParcelable(user, 0)
        writeParcelable(subject, 0)
        writeParcelable(professor, 0)
        writeInt(if (isLike) 1 else 0)    // true = 1, false = 0
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Review> = object : Parcelable.Creator<Review> {
            override fun createFromParcel(source: Parcel): Review = Review(source)
            override fun newArray(size: Int): Array<Review?> = arrayOfNulls(size)
        }
    }
}