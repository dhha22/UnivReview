package com.univreview.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by DavidHa on 2017. 10. 30..
 */
data class Register(
        val provider: String,
        val userId: Long,
        val accessToken: String,
        var profileImageUrl: String,
        var email: String? = null,
        var name: String? = null,
        var profileImageUri: Uri? = null,
        var universityId: Long? = null,
        var majorId: Long? = null) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readLong(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelable<Uri>(Uri::class.java.classLoader),
            source.readValue(Long::class.java.classLoader) as Long?,
            source.readValue(Long::class.java.classLoader) as Long?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(provider)
        writeLong(userId)
        writeString(accessToken)
        writeString(profileImageUrl)
        writeString(email)
        writeString(name)
        writeParcelable(profileImageUri, 0)
        writeValue(universityId)
        writeValue(majorId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Register> = object : Parcelable.Creator<Register> {
            override fun createFromParcel(source: Parcel): Register = Register(source)
            override fun newArray(size: Int): Array<Register?> = arrayOfNulls(size)
        }
    }
}