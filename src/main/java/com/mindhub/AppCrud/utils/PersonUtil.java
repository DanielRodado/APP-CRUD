package com.mindhub.AppCrud.utils;

public final class PersonUtil {

    public static boolean verifyEmailByType(String teacherEmail, String typeEmail) {
        return teacherEmail.matches(".*@"+typeEmail+"\\.com$");
    }

}
