package com.univreview.model

import android.os.Parcel
import android.os.Parcelable
import com.dhha22.bindadapter.Item

/**
 * Created by DavidHa on 2017. 8. 20..
 */
data class Subject(var id: Long, var name: String) : Parcelable, Item {
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
        @JvmField val CREATOR: Parcelable.Creator<Subject> = object : Parcelable.Creator<Subject> {
            override fun createFromParcel(source: Parcel): Subject = Subject(source)
            override fun newArray(size: Int): Array<Subject?> = arrayOfNulls(size)
        }
    }
}