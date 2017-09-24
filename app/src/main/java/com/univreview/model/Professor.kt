package com.univreview.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by DavidHa on 2017. 8. 20..
 */
data class Professor(override var id: Long, override var name: String) : AbstractDataProvider(), Parcelable {
    constructor(source: Parcel) : this(
            source.readLong(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(name)
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Professor> = object : Parcelable.Creator<Professor> {
            override fun createFromParcel(source: Parcel): Professor = Professor(source)
            override fun newArray(size: Int): Array<Professor?> = arrayOfNulls(size)
        }
    }
}