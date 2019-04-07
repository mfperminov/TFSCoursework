package xyz.mperminov.tfscoursework.repositories.user.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import xyz.mperminov.tfscoursework.models.User

class UserSchema {
    @SerializedName("user")
    @Expose
    var user: User? = null
    @SerializedName("status")
    @Expose
    var status: String? = null
}
