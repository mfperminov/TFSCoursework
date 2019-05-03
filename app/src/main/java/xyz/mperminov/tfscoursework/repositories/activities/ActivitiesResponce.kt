package xyz.mperminov.tfscoursework.repositories.activities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ActiveResponce {
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("date_start")
    @Expose
    var dateStart: String? = null
    @SerializedName("date_end")
    @Expose
    var dateEnd: String? = null
    @SerializedName("event_type")
    @Expose
    var eventTypeResponce: EventTypeResponse? = null
    @SerializedName("custom_date")
    @Expose
    var customDate: String? = null
    @SerializedName("place")
    @Expose
    var place: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("url_external")
    @Expose
    var urlExternal: Boolean? = null
    @SerializedName("display_button")
    @Expose
    var displayButton: Boolean? = null
    @SerializedName("url_text")
    @Expose
    var urlText: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
}

class EventTypeResponse(
    @SerializedName("name")
    @Expose
    var name: String? = null
)

class ArchiveResponce {
    @SerializedName("title")
    @Expose
    var title: String? = null
    @SerializedName("date_start")
    @Expose
    var dateStart: String? = null
    @SerializedName("date_end")
    @Expose
    var dateEnd: String? = null
    @SerializedName("event_type")
    @Expose
    var eventTypeResponce: EventTypeResponse? = null
    @SerializedName("custom_date")
    @Expose
    var customDate: String? = null
    @SerializedName("place")
    @Expose
    var place: String? = null
    @SerializedName("url")
    @Expose
    var url: String? = null
    @SerializedName("url_external")
    @Expose
    var urlExternal: Boolean? = null
    @SerializedName("display_button")
    @Expose
    var displayButton: Boolean? = null
    @SerializedName("url_text")
    @Expose
    var urlText: String? = null
    @SerializedName("description")
    @Expose
    var description: String? = null
}

class ActivitiesResponce {
    @SerializedName("active")
    @Expose
    var activeResponce: List<ActiveResponce>? = null
    @SerializedName("archive")
    @Expose
    var archive: List<ArchiveResponce>? = null
}
