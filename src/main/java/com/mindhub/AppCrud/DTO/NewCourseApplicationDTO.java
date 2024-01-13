package com.mindhub.AppCrud.DTO;

import java.util.Set;

public record NewCourseApplicationDTO(String name, String place, String teacherId, Set<String> idSchedules) {
}
