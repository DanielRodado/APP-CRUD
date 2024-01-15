package com.mindhub.AppCrud.DTO;

import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.subClass.Student;

public record RecordStudentCourse(Student student, Course course) {
}
