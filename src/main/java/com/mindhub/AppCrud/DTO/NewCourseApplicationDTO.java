package com.mindhub.AppCrud.DTO;

import java.util.List;

public record NewCourseApplicationDTO(String name, String place, String teacherId, List<String> idSchedules) {
}
