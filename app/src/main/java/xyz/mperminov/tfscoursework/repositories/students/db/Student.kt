package xyz.mperminov.tfscoursework.repositories.students.db

import xyz.mperminov.tfscoursework.repositories.students.network.StudentSchema

data class Student(val id: Int, val name: String, val mark: Double) {
    companion object {
        val NOBODY = Student(0, "", 0.0)
    }
}

class StudentMapper {
    fun mapToDbModel(studentSchemas: List<StudentSchema>): List<Student> {
        return if (studentSchemas[1].grades != null) {
            studentSchemas[1].grades?.map { grade ->
                if (grade.studentId != null && grade.student != null && !grade.grades.isNullOrEmpty() && grade.grades!!.last().mark != null)
                    Student(
                        grade.studentId!!,
                        grade.student!!,
                        grade.grades!!.last().mark!!
                    )
                else Student.NOBODY
            }!!
        } else emptyList()
    }
}