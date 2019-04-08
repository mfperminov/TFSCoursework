package xyz.mperminov.tfscoursework.repositories.lectures

import io.reactivex.Observable
import xyz.mperminov.tfscoursework.repositories.models.Lectures


interface HomeworksRepository {
    fun getLectures(): Observable<Lectures>
}