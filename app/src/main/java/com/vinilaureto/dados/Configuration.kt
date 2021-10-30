package com.vinilaureto.dados

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Configuration(var numDices: Int = 1, var numFace: Int = 6): Parcelable