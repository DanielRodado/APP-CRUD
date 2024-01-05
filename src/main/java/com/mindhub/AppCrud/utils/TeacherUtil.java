package com.mindhub.AppCrud.utils;

public final class TeacherUtil {

    public static boolean verifyEmailTeacher(String teacherEmail) {
        return teacherEmail.matches(".*@mentor\\.com$");
    }

}
