package blog.demo.Service;

import blog.demo.Domain.Post;
import blog.demo.Repository.PostRepository;
import blog.demo.Request.PostCreate;
import blog.demo.Response.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("글 작성")
    void test1() {
        // given
        PostCreate postCreate = PostCreate
                .builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(postCreate);
        // then
        assertThat(postRepository.count()).isEqualTo(1L);
        Post post = postRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("제목입니다.");
        assertThat(post.getContent()).isEqualTo("내용입니다.");
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        //given
        Post requestPost = Post.builder().title("foo341234123412341234").content("bar").build();
        postRepository.save(requestPost);


        //when
        PostResponse post = postService.get(requestPost.getId());
        //then

        assertThat(post).isNotNull();
        assertThat(postRepository.count()).isEqualTo(1L);
        assertThat(post.getTitle()).isEqualTo("foo3412341");
        assertThat(post.getContent()).isEqualTo("bar");
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test3() {
        //given

        postRepository.saveAll(List.of(
                Post
                        .builder()
                        .title("foo1")
                        .content("bar1")
                        .build(),
                Post
                        .builder()
                        .title("foo2")
                        .content("bar2")
                        .build()
        ));

        //when
        List<PostResponse> posts = postService.getList();
        //then

        assertThat(posts.size()).isEqualTo(2L);

    }
}