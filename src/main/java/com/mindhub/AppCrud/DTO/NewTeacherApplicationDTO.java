package com.mindhub.AppCrud.DTO;

import java.util.List;

public record NewTeacherApplicationDTO(String firstName, String lastName, String email,
                                       String password,  List<String> specializations) {
}
