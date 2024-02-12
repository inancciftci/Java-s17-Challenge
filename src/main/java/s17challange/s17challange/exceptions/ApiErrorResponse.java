package s17challange.s17challange.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiErrorResponse {
    private Integer status;
    private String message;
    private Long timestamp;
}
