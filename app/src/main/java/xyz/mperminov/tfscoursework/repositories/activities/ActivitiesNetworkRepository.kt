package xyz.mperminov.tfscoursework.repositories.activities

import android.util.Log
import io.reactivex.Single
import xyz.mperminov.tfscoursework.TFSCourseWorkApp
import xyz.mperminov.tfscoursework.network.Api
import xyz.mperminov.tfscoursework.network.AuthHolder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ActivitiesNetworkRepository @Inject constructor(private val api: Api, private val authHolder: AuthHolder) {
    private val mapper = ActivitiesMapper()

    init {
        TFSCourseWorkApp.userComponent.inject(this)
    }

    private fun getLectures(): Single<ActivitiesResponce> {
        return api.getActivities(authHolder.getToken() ?: "")
    }

    fun getArchive(): Single<List<Archive>> {
        return getLectures().map { activities -> activities.archive }
            .flattenAsObservable { it }.map { it -> mapper.mapResponseToArchive(it) }
            .filter { it != Archive.NOTHING }.toList()
    }

    fun getActive(): Single<List<Active>> {
        return getLectures().map { activities -> activities.activeResponce }
            .flattenAsObservable { it }.map { it -> mapper.mapResponseToActive(it) }
            .filter { it != Active.NOTHING }.toList()
    }
}

data class Archive(val title: String, val eventType: EventType, val place: String, val dateEnd: Date) {
    companion object {
        val NOTHING = Archive("", EventType.DEFAULT, "", Date())
    }
}

data class Active(
    val title: String,
    val eventType: EventType,
    val place: String,
    val dateStart: Date,
    val dateEnd: Date,
    val descr: String
) {
    companion object {
        val NOTHING = Active("", EventType.DEFAULT, "", Date(), Date(), "")
    }
}

enum class EventType { DEFAULT, SCHOOLKIDS, INTERNSHIP, SPECIAL_COURSE, FINTECH }
class ActivitiesMapper {
    fun mapResponseToArchive(response: ArchiveResponce): Archive {
        if (response.title == null || response.place == null || response.dateEnd == null)
            return Archive.NOTHING
        val dateEnd: Date = parseDate(response.dateEnd!!) ?: return Archive.NOTHING
        val eventType = convertToEventType(response.eventTypeResponce)
        return Archive(response.title!!, eventType, response.place!!, dateEnd)
    }

    fun mapResponseToActive(response: ActiveResponce): Active {
        if (response.title == null || response.place == null || response.dateStart == null || response.dateEnd == null)
            return Active.NOTHING
        val dateStart: Date = parseDate(response.dateStart!!) ?: return Active.NOTHING
        val dateEnd: Date = parseDate(response.dateEnd!!) ?: return Active.NOTHING
        val eventType = convertToEventType(response.eventTypeResponce)
        return Active(response.title!!, eventType, response.place!!, dateStart, dateEnd, response.description ?: "")
    }

    private fun parseDate(date: String): Date? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
        return try {
            sdf.parse(date)
        } catch (e: ParseException) {
            Log.e("exception", e.message)
            null
        }
    }

    private fun convertToEventType(eventTypeResponse: EventTypeResponse?): EventType {
        if (eventTypeResponse == null) return EventType.DEFAULT
        return when (eventTypeResponse.name) {
            "Internship" -> EventType.INTERNSHIP
            "Финтех Школа" -> EventType.FINTECH
            "Спецкурс" -> EventType.SPECIAL_COURSE
            "Курсы для школьников" -> EventType.SCHOOLKIDS
            else -> EventType.DEFAULT
        }
    }
}
