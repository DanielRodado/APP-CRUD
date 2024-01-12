package com.mindhub.AppCrud;

import com.mindhub.AppCrud.models.*;
import com.mindhub.AppCrud.models.subClass.Admin;
import com.mindhub.AppCrud.models.subClass.Student;
import com.mindhub.AppCrud.models.subClass.Teacher;
import com.mindhub.AppCrud.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@SpringBootApplication
public class AppCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppCrudApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(AdminRepository adminRepository, TeacherRepository teacherRepository,
									  StudentRepository studentRepository, CourseRepository courseRepository,
									  ScheduleRepository scheduleRepository,
									  CourseScheduleRepository courseScheduleRepository, StudentCourseRepository studentCourseRepository) {

		return args -> {

			Admin adminOne = new Admin("Daniel", "Rodado", "danielrodado@admin.com",
					passwordEncoder.encode("admin123"));
			adminRepository.save(adminOne);

			// Schedules

			Schedule scheduleOne = new Schedule(DayWeek.MONDAY, ShiftType.AFTERNOON, LocalTime.of(14, 0),
					LocalTime.of(18, 0));
			scheduleRepository.save(scheduleOne);

			Schedule scheduleTwo = new Schedule(DayWeek.THURSDAY, ShiftType.MORNING, LocalTime.of(8, 30),
					LocalTime.of(10, 30));
			scheduleRepository.save(scheduleTwo);

			Schedule scheduleThree = new Schedule(DayWeek.WEDNESDAY, ShiftType.AFTERNOON, LocalTime.of(12, 0),
					LocalTime.of(16, 30));
			scheduleRepository.save(scheduleThree);

			Schedule scheduleFour = new Schedule(DayWeek.THURSDAY, ShiftType.EVENING, LocalTime.of(18, 30),
					LocalTime.of(21, 30));
			scheduleRepository.save(scheduleFour);

			// Teachers and courses

			Teacher teacherOne = new Teacher("Martin", "Araolaza", "martin@mentor.com",
					passwordEncoder.encode("mentor123"), List.of("Java", "Spring", "Spring Boot", "PostgreSQL"));
			teacherRepository.save(teacherOne);

			Course courseOne = new Course("Full Stack Java", "Cohort 51");
			teacherOne.addCourse(courseOne);
			courseRepository.save(courseOne);

			Course courseTwo = new Course("MERN", "Cohort 52");
			courseRepository.save(courseTwo);

			// CourseSchedules

			CourseSchedule courseScheduleOne = new CourseSchedule();
			courseOne.addCourseSchedule(courseScheduleOne);
			scheduleOne.addCourseSchedule(courseScheduleOne);
			courseScheduleRepository.save(courseScheduleOne);

			CourseSchedule courseScheduleTwo = new CourseSchedule();
			courseOne.addCourseSchedule(courseScheduleTwo);
			scheduleThree.addCourseSchedule(courseScheduleTwo);
			courseScheduleRepository.save(courseScheduleTwo);

			CourseSchedule courseScheduleThree = new CourseSchedule();
			courseTwo.addCourseSchedule(courseScheduleThree);
			scheduleFour.addCourseSchedule(courseScheduleThree);
			courseScheduleRepository.save(courseScheduleThree);

			// Students

			Student studentOne = new Student("Esteban", "Girardot", "estebang@student.com",
					passwordEncoder.encode("student123"));
			studentRepository.save(studentOne);

			Student studentTwo = new Student("Alfredo", "Orozco", "alfred@student.com",
					passwordEncoder.encode("student123"));
			studentRepository.save(studentTwo);

			Student studentThree = new Student("Felipe", "Risaralda", "felpr@student.com",
					passwordEncoder.encode("student123"));
			studentRepository.save(studentThree);

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
