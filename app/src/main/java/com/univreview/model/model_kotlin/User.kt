package com.univreview.model.model_kotlin

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by DavidHa on 2017. 8. 19..
 */
data class User(var provider: String, // facebook, kakao
                var uid: Long,
                var name: String,
                var email: String?,
                var profileImageUrl: String?,
                var accessToken: String,
                var profileImageUri: Uri? = null,
                var client: String? = null,
                var universityId: Long? = null,
                var departmentId: Long? = null,
                var majorId: Long? = null,
                val authenticated : Boolean = false,
                val departmentName : String? = null,
                val majorName : String? = null,
                val point:Int = 0,
                val reviewCount:Int = 0) : Parcelable {


    constructor(source: Parcel) : this(
            source.readString(),
            source.readLong(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelable<Uri>(ClassLoader.getSystemClassLoader()),
            source.readString(),
            source.readLong(),
            source.readLong(),
            source.readLong(),
            source.readInt() != 0,
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(provider)
        writeLong(uid)
        writeString(name)
        writeString(email)
        writeString(profileImageUrl)
        writeString(accessToken)
        dest.writeParcelable(profileImageUri, flags)
        writeString(client)
        writeLong(universityId ?: 0)
        writeLong(departmentId ?: 0)
        writeLong(majorId ?: 0)
        dest.writeInt(if(authenticated) 1 else 0)  // true = 1, false 0
        writeString(departmentName)
        writeString(majorName)
        writeInt(point)
        writeInt(reviewCount)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}