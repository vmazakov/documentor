package bg.documentor.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Application Error used by {@link GlobalExceptionHandler}
 */
@Data @Builder @AllArgsConstructor public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss") private LocalDateTime timestamp;

    private List<String> errorMessages;

    private String path;

}
