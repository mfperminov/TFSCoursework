package xyz.mperminov.tfscoursework.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Contact(val firstName: String, val lastName: String, val points: Int = (0..300).random()): Parcelable {

    fun getInitials(): String = firstName.take(1).capitalize() + lastName.take(1).capitalize()

}