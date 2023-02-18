package blog.demo.Response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

/**
 *  {
 *      "code":"400",
 *      "message":"잘못된 요청입니다.",
 *      "validation":{
 *          "title":"값을 입력해 주세요"
 *      }
 *  }
 */

@Getter
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String,String> validation = new HashMap<>();

    @Builder
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }



    public void addValidation(FieldError fieldError){
        this.validation.put(fieldError.getField(), fieldError.getDefaultMessage());
    }

}
