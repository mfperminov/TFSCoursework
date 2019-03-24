package xyz.mperminov.tfscoursework.repositories.user.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import xyz.mperminov.tfscoursework.models.User;

// Мне было лень это конвертить в котлин, скопировал с http://www.jsonschema2pojo.org/

public class UserSchema {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("status")
    @Expose
    private String status;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
