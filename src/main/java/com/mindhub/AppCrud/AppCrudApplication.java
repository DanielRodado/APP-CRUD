package com.mindhub.AppCrud;

import com.mindhub.AppCrud.models.subClass.Admin;
import com.mindhub.AppCrud.models.subClass.Student;
import com.mindhub.AppCrud.models.subClass.Teacher;
import com.mindhub.AppCrud.repositories.AdminRepository;
import com.mindhub.AppCrud.repositories.StudentRepository;
import com.mindhub.AppCrud.repositories.TeacherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppCrudApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(AdminRepository adminRepository, TeacherRepository teacherRepository,
									  StudentRepository studentRepository) {

		return args -> {

			Admin adminOne = new Admin("Daniel", "Rodado", "danielrodado@gmail.com", "admin123");
			adminRepository.save(adminOne);

			Teacher teacherOne = new Teacher("Martin", "Araolaza", "martin@mentor.com", "mentor123");
			teacherRepository.save(teacherOne);

			Student studentOne = new Student("Esteban", "Girardot", "estebang@estudent.com", "student123");
			studentRepository.save(studentOne);
		};

	}

}
