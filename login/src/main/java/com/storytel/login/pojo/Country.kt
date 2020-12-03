package com.storytel.login.pojo

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes

data class Country(val locale: String?, val iso: String?, val forceAcceptMarketing: Boolean,
                   val marketingConsentTitle: String?, val marketingConsentBody: String?,
                   var displayName: String?, @DrawableRes var flagRes : Int): Parcelable {

    constructor(parcel: Parcel): this(parcel.readString(), parcel.readString(),
            parcel.readByte() != 0.toByte(), parcel.readString(), parcel.readString(), parcel.readString(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(locale)
        parcel.writeString(iso)
        parcel.writeByte(if (forceAcceptMarketing) 1 else 0)
        parcel.writeString(marketingConsentTitle)
        parcel.writeString(marketingConsentBody)
        parcel.writeString(displayName)
        parcel.writeInt(flagRes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR: Parcelable.Creator<Country> {
        override fun createFromParcel(parcel: Parcel): Country {
            return Country(parcel)
        }

        override fun newArray(size: Int): Array<Country?> {
            return arrayOfNulls(size)
        }
    }

}