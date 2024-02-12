package s17challange.s17challange.validation;

import org.springframework.http.HttpStatus;
import s17challange.s17challange.entity.Course;
import s17challange.s17challange.exceptions.ApiException;

import java.util.List;
import java.util.Optional;

public class CourseValidation {
    public static void checkName(String name) {
        if(name == null || name.isEmpty()) {
            throw new ApiException("name cannot be null or empty!", HttpStatus.BAD_REQUEST);
        }
    }

    public static void checkCredit(Integer credit) {
        if(credit == null || credit < 0 || credit > 4) {
            throw new ApiException("credit cannot be null or not between 0-4!", HttpStatus.BAD_REQUEST);
        }
    }

    public static void checkNameExist(List<Course> courses, String name){
        Optional<Course> courseOptional = courses.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findAny();
        if(courseOptional.isPresent()) {
            throw new ApiException("course already exist with name: " + name, HttpStatus.BAD_REQUEST);
        }
    }

    public static void checkId(Integer id){
        if(id==null || id<0){
            throw new ApiException("ID cannot be null or less than zero: " + id, HttpStatus.BAD_REQUEST);
        }
    }
}
