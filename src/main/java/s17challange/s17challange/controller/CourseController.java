package s17challange.s17challange.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import s17challange.s17challange.entity.ApiResponse;
import s17challange.s17challange.entity.Course;
import s17challange.s17challange.entity.CourseGpa;
import s17challange.s17challange.exceptions.ApiException;
import s17challange.s17challange.validation.CourseValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private List<Course> courses;

    private CourseGpa lowCourseGpa;
    private CourseGpa highCourseGpa;
    private CourseGpa mediumCourseGpa;

    @Autowired
    public CourseController(@Qualifier("highCourseGpa") CourseGpa highCourseGpa,
                            @Qualifier("mediumCourseGpa") CourseGpa mediumCourseGpa,
                            @Qualifier("lowCourseGpa") CourseGpa lowCourseGpa) {
        this.highCourseGpa = highCourseGpa;
        this.mediumCourseGpa = mediumCourseGpa;
        this.lowCourseGpa = lowCourseGpa;
    }

    @PostConstruct
    public void init(){
        courses = new ArrayList<>();
    }

    @GetMapping
    public List<Course> getAll(){
        return this.courses;
    }

    @GetMapping("/{name}")
    public Course getByName(@PathVariable("name") String name){
        CourseValidation.checkName(name);
        return courses.stream()
                .filter(course -> course.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new ApiException("course not found with name: " + name, HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@RequestBody Course course){
        CourseValidation.checkName(course.getName());
        CourseValidation.checkCredit(course.getCredit());
        CourseValidation.checkNameExist(courses, course.getName());
        courses.add(course);
        Integer totalGpa = getTotalGpa(course);
        ApiResponse apiResponse = new ApiResponse(course,totalGpa);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    private Integer getTotalGpa(Course course){
        if(course.getCredit()<=2){
            return course.getGrade().getCoefficient() * course.getCredit() * lowCourseGpa.getGpa();
        } else if (course.getCredit() == 3) {
            return course.getGrade().getCoefficient() * course.getCredit() * mediumCourseGpa.getGpa();
        } else {
            return course.getGrade().getCoefficient() * course.getCredit() * highCourseGpa.getGpa();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Integer id,
                                              @RequestBody Course course){
        CourseValidation.checkId(id);
        CourseValidation.checkCredit(course.getCredit());
        CourseValidation.checkName(course.getName());

        Course existingCourse = courses.stream()
                .filter(course1 -> course1.getName().equalsIgnoreCase(course.getName()))
                .findAny()
                .orElseThrow(() -> new ApiException("record not fount with id: " + id , HttpStatus.NOT_FOUND));

        Integer indexOfExistingCourse = courses.indexOf(existingCourse);
        course.setId(id);
        CourseValidation.checkNameExist(courses, course.getName());
        courses.set(indexOfExistingCourse, course);
        Integer totalGpa = getTotalGpa(course);
        ApiResponse apiResponse = new ApiResponse(courses.get(indexOfExistingCourse), totalGpa);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        CourseValidation.checkId(id);
        Course course = courses.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ApiException("record not fount with id: " + id , HttpStatus.NOT_FOUND));
        courses.remove(course);
    }
}
