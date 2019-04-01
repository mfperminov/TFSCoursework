package xyz.mperminov.tfscoursework.repositories.homeworks

import io.reactivex.Observable
import xyz.mperminov.tfscoursework.repositories.homeworks.network.Lectures


interface HomeworksRepository {
    fun getLectures(): Observable<Lectures>
}