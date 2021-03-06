package com.univreview.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import com.dhha22.bindadapter.Item

/**
 * Created by DavidHa on 2017. 8. 19..
 */
data class User(var uid: Long, // sns id
                var id: Long, // user id
                var name: String?,
                var email: String?,
                var profileImageUrl: String?,
                var profileImageUrlThumb: String?,
                var accessToken: String,
                var client: String?,
                var universityId: Long?,
                var majorId: Long?,
                val authenticated: Boolean = false,
                val universityName: String,
                val majorName: String,
                val point: Int = 0,
                val reviewCount: Int = 0) : Parcelable, Item {
    constructor(source: Parcel) : this(
            source.readLong(),
            source.readLong(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?,
            1 == source.readInt(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(uid)
        writeLong(id)
        writeString(name)
        writeString(email)
        writeString(profileImageUrl)
        writeString(profileImageUrlThumb)
        writeString(accessToken)
        writeString(client)
        writeValue(universityId)
        writeValue(majorId)
        writeInt((if (authenticated) 1 else 0))
        writeString(universityName)
        writeString(majorName)
        writeInt(point)
        writeInt(reviewCount)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}