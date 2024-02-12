package s17challange.s17challange.entity;

import org.springframework.stereotype.Component;

@Component
public class MediumCourseGpa implements CourseGpa{
    @Override
    public int getGpa() {
        return 5;
    }
}
