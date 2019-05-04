package xyz.mperminov.tfscoursework.repositories.students.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import xyz.mperminov.tfscoursework.repositories.students.network.StudentSchema
import xyz.mperminov.tfscoursework.utils.round

@Entity(tableName = "students")
@Parcelize
data class Student(
    @PrimaryKey val id: Int, val name: String,
    val mark: Double,
    val userProfile: Boolean
) : Parcelable {
    private fun collectLastNameAndName(): List<String> {
        return name.split("\\s".toRegex(), 2)
    }

    fun getFirstName(): String = name.split("\\s".toRegex(), 2)[1]

    fun getInitials(): String {
        return collectLastNameAndName().reduceIndexed { i, acc, s -> acc.take(i).plus(s[0]).capitalize() }
    }

    companion object {
        val NOBODY = Student(0, "", 0.0, false)
    }
}

class StudentMapper {
    private val MARK_ROUND_PRECISION = 2
    fun mapToDbModel(studentSchemas: List<StudentSchema>): List<Student> {
        val students = studentSchemas[1].grades?.filter { grade -> (grade.studentId != null && grade.student != null) }
            ?.map { grade ->
                val mark =
                    if (!grade.grades.isNullOrEmpty() && grade.grades!!.last().mark != null) roundMarkWithPrecision(
                        grade.grades!!.last().mark!!,
                        MARK_ROUND_PRECISION
                    ) else 0.0
                Student(
                    grade.studentId!!,
                    grade.student!!,
                    mark,
                    false
                )
            }
        return students ?: emptyList()
    }

    private fun roundMarkWithPrecision(rawMark: Double, precision: Int): Double {
        return rawMark.round(precision)
    }
}