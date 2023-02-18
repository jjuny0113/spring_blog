package blog.demo.Contoller;

import blog.demo.Domain.Post;
import blog.demo.Request.PostCreate;
import blog.demo.Service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {//1. 매번 메서드마다 값을 검증해야 한다.
        postService.write(request);
        //repository.save(params)

    }
}
