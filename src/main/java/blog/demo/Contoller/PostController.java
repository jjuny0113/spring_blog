package blog.demo.Contoller;

import blog.demo.Domain.Post;
import blog.demo.Request.PostCreate;
import blog.demo.Response.PostResponse;
import blog.demo.Service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
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


    /**
     * /posts -> 글 전체 조회(검색 + 페이징)
     * /posts/{postId} -> 글 한개만 조회
     */

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId){
        return postService.get(postId);
    }


    @GetMapping("/posts")
    public List<PostResponse> getList(@PageableDefault(size = 5) Pageable pageable){
        return postService.getList(pageable);
    }
}
