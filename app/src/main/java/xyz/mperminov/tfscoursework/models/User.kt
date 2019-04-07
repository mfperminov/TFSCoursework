package xyz.mperminov.tfscoursework.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("last_name") val lastName: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("middle_name") val patronymic: String,
    val avatar: String?
) : Parcelable {
    override fun toString(): String {
        return "$lastName $firstName $patronymic"
    }
}