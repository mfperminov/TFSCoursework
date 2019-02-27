package xyz.mperminov.tfscoursework.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(val lastName: String, val firstName: String, val patronymic: String):Parcelable {
    override fun toString(): String {
        return "$lastName $firstName $patronymic"
    }
}