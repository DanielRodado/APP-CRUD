package com.mindhub.AppCrud;

import com.mindhub.AppCrud.models.Course;
import com.mindhub.AppCrud.models.CourseSchedule;
import com.mindhub.AppCrud.models.Schedule;
import com.mindhub.AppCrud.models.StudentCourse;
import com.mindhub.AppCrud.models.subClass.Admin;
import com.mindhub.AppCrud.models.subClass.Student;
import com.mindhub.AppCrud.models.subClass.Teacher;
import com.mindhub.AppCrud.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class AppCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppCrudApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(AdminRepository adminRepository, TeacherRepository teacherRepository,
									  StudentRepository studentRepository, CourseRepository courseRepository,
									  ScheduleRepository scheduleRepository,
									  CourseScheduleRepository courseScheduleRepository, StudentCourseRepository studentCourseRepository) {

		return args -> {

			Admin adminOne = new Admin("Daniel", "Rodado", "danielrodado@gmail.com", "admin123");
			adminRepository.save(adminOne);

			// Schedules
			Schedule scheduleOne = new Schedule("Monday", "14:00 hrs", "18:00 hrs");
			scheduleRepository.save(scheduleOne);

			Schedule scheduleTwo = new Schedule("Tuesday", "07:30 hrs", "10:00 hrs");
			scheduleRepository.save(scheduleTwo);

			Schedule scheduleThree = new Schedule("Wednesday", "12:00 hrs", "16:30 hrs");
			scheduleRepository.save(scheduleThree);

			Schedule scheduleFour = new Schedule("Thursday", "10:00 hrs", "12:00 hrs");
			scheduleRepository.save(scheduleFour);

			// Teachers and courses

			Teacher teacherOne = new Teacher("Martin", "Araolaza", "martin@mentor.com",
					"mentor123", List.of("Java", "Spring", "Spring Boot", "PostgreSQL"));
			teacherRepository.save(teacherOne);

			Course courseOne = new Course("Full Stack Java", "Cohort 51");
			teacherOne.addCourse(courseOne);
			courseRepository.save(courseOne);

			// CourseSchedules

			CourseSchedule courseScheduleOne = new CourseSchedule();
			courseOne.addCourseSchedule(courseScheduleOne);
			scheduleOne.addCourseSchedule(courseScheduleOne);
			courseScheduleRepository.save(courseScheduleOne);

			CourseSchedule courseScheduleTwo = new CourseSchedule();
			courseOne.addCourseSchedule(courseScheduleTwo);
			scheduleThree.addCourseSchedule(courseScheduleTwo);
			courseScheduleRepository.save(courseScheduleTwo);

			// Students

			Student studentOne = new Student("Esteban", "Girardot", "estebang@estudent.com", "student123");
			studentRepository.save(studentOne);

			Student studentTwo = new Student("Alfredo", "Orozco", "alfred@gmail.com", "pass123");
			studentRepository.save(studentTwo);

			// StudentCourses

			StudentCourse studentCourseOne = new StudentCourse(LocalDate.now());
			studentOne.addStudentCourse(studentCourseOne);
			courseOne.addStudentCourse(studentCourseOne);
			studentCourseRepository.save(studentCourseOne);

			StudentCourse studentCourseTwo = new StudentCourse(LocalDate.now());
			studentTwo.addStudentCourse(studentCourseTwo);
			courseOne.addStudentCourse(studentCourseTwo);
			studentCourseRepository.save(studentCourseTwo);
		};

	}

}
