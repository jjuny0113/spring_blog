package blog.demo.Service;

import blog.demo.Domain.Post;
import blog.demo.Repository.PostRepository;
import blog.demo.Request.PostCreate;
import blog.demo.Response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate){
        //postCreate -> entity

        Post post = Post.
                builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);

    }
    /**
     * /posts -> 글 전체 조회(검색 + 페이징)
     * /posts/{postId} -> 글 한개만 조회
     */

    public PostResponse get(Long id){
        // 서비스 정책이 있으면 응답 클래스를 분리하자
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public List<PostResponse> getList() {
        return postRepository.findAll().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
}
