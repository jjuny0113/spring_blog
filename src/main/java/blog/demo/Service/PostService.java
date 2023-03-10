package blog.demo.Service;

import blog.demo.Domain.Post;
import blog.demo.Domain.PostEditor;
import blog.demo.Exception.PostNotFound;
import blog.demo.Repository.PostRepository;
import blog.demo.Request.PostCreate;
import blog.demo.Request.PostEdit;
import blog.demo.Request.PostSearch;
import blog.demo.Response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    // 글이 너무 많은 경우 -> 비용이 너무 많이 든다.
    // 글이 -> 100,000,000 -> db글 모두 조회하는 경우 -> db가 뻗을 수 있다.
    // db-> 어플리케이션 서버로 전달하는 시간, 트래픽 비용 등이 많이 발생 할 수 있다.
    public List<PostResponse> getList(PostSearch postSearch) {

//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC,"id"));
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit){
        Post post = postRepository
                .findById(id)
                .orElseThrow(PostNotFound::new);
        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();
        PostEditor postEditor = postEditorBuilder.title(postEdit.getTitle()).content(postEdit.getContent()).build();
        post.edit(postEditor);

        return new PostResponse(post);
    }

    public void delete(Long id) {
        Post post = postRepository
                .findById(id)
                .orElseThrow(PostNotFound::new);

        postRepository.delete(post);
    }
}
