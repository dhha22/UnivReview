package com.univreview.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by DavidHa on 2017. 8. 15..
 */
data class Register(val userType: String, // F: facebook, K: kakao
                    val userId: String, // SNS user id
                    val accessToken: String, // SNS token
                    var nickName: String, // 닉네임
                    var profileURL: String? = null, // 유저 프로필 사진
                    var email: String? = null,
                    var universityId: Long? = null,
                    var departmentId: Long? = null,
                    var majorId: Long? = null,
                    var profileUri : Uri? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readLong(),
            source.readLong(),
            source.readLong(),
            source.readParcelable<Uri>(ClassLoader.getSystemClassLoader())
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(userType)
        writeString(userId)
        writeString(accessToken)
        writeString(nickName)
        writeString(profileURL)
        writeString(email)
        writeLong(universityId?:0)
        writeLong(departmentId?:0)
        writeLong(majorId?:0)
        dest.writeParcelable(profileUri, flags)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Register> = object : Parcelable.Creator<Register> {
            override fun createFromParcel(source: Parcel): Register = Register(source)
            override fun newArray(size: Int): Array<Register?> = arrayOfNulls(size)
        }
    }
}

