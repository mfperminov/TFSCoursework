package xyz.mperminov.tfscoursework.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    @SerializedName("last_name") val lastName: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("middle_name") val patronymic: String?,
    val avatar: String?,
    val email: String,
    val phone_mobile: String?,
    val region: String?,
    val school: String?,
    val school_graduation: Int?,
    val university: String?,
    val faculty: String?,
    val university_graduation: Int?,
    val department: String?,
    val current_work: String?,
    val resume: String?
) : Parcelable {
    companion object {
        val NOBODY = User(
            "", "", "", "", "",
            "", "", "", 0, "",
            "", 0, "", "", ""
        )
    }

    fun hasEducationInfo(): Boolean {
        with(this) {
            return (school != null || school_graduation != null || university != null ||
                    faculty != null || university_graduation != null || department != null)
        }
    }

    fun hasWorkInfo(): Boolean {
        with(this) {
            return (current_work != null || resume != null)
        }
    }

    override fun toString(): String {
        return "$lastName $firstName $patronymic $email"
    }
}