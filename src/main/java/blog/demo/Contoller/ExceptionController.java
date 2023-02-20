package blog.demo.Contoller;

import blog.demo.Exception.BlogException;
import blog.demo.Exception.InvalidRequest;
import blog.demo.Response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse inValidRequestHandler(MethodArgumentNotValidException e) {

        ErrorResponse errorResponse = ErrorResponse
                .builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();
        e.getFieldErrors().forEach(errorResponse::addValidation);

        return errorResponse;


    }

    @ResponseBody
    @ExceptionHandler(BlogException.class)
    public ResponseEntity<ErrorResponse>  blogException(BlogException e){
        int statusCode = e.getStatusCode();
        ErrorResponse body = ErrorResponse
                .builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();


        //응답 json validation -> title: 제목에 바보를 포함할 수 없습니다.
        return ResponseEntity.status(statusCode).body(body);
    }


}
