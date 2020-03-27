package com.shibuyaxpress.nuclearcodeinspector.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Preview:Parcelable {
    var image: String? = null
    var title: String? = null
    var description: String? = null
}