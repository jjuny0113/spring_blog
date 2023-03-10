package blog.demo.Service;

import blog.demo.Domain.Post;
import blog.demo.Exception.PostNotFound;
import blog.demo.Repository.PostRepository;
import blog.demo.Request.PostCreate;
import blog.demo.Request.PostEdit;
import blog.demo.Request.PostSearch;
import blog.demo.Response.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @DisplayName("글 1페이지 조회")
    void test3() {
        //given

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post
                        .builder()
                        .title("호돌맨 제목 " + i)
                        .content("반포자이 " + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder().build();

        //when
        List<PostResponse> posts = postService.getList(postSearch);
        //then

        assertThat(posts.size()).isEqualTo(10L);
        assertThat(posts.get(0).getTitle()).isEqualTo("호돌맨 제목 30");
        assertThat(posts.get(4).getTitle()).isEqualTo("호돌맨 제목 26");

    }

    @Test
    @DisplayName("글 제목 수정")
    void test4() {
        //given


        Post post = Post
                .builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().title("호돌걸").content("반포자이").build();

        //when
        postService.edit(post.getId(), postEdit);
        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        assertThat(changedPost.getTitle()).isEqualTo("호돌걸");
        assertThat(changedPost.getContent()).isEqualTo("반포자이");

    }

    @Test
    @DisplayName("글 내용 수정")
    void test5() {
        //given


        Post post = Post
                .builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().title(null).content("초가집").build();

        //when
        postService.edit(post.getId(), postEdit);
        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id=" + post.getId()));
        assertThat(changedPost.getTitle()).isEqualTo("호돌맨");
        assertThat(changedPost.getContent()).isEqualTo("초가집");

    }

    @Test
    @DisplayName("게시글 삭제 - 존재 하는 글")
    void test6() {

        //given
        Post post = Post
                .builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);
        //when
        postService.delete(post.getId());
        //then
        assertThat(postRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("글 1개 조회 예외처리")
    void test7() {
        //given
        Post request = Post.builder().title("foo341234123412341234").content("bar").build();
        postRepository.save(request);


        //expected
        assertThrows(PostNotFound.class, () -> {
            postService.get(request.getId() + 1L);

        });


    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는글")
    void test8() {

        Post post = Post
                .builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);
        //then
        assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);

        });

    }

    @Test
    @DisplayName("글 내용 수정 - 존재하지 않는 글")
    void test9() {
        //given


        Post post = Post
                .builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().title(null).content("초가집").build();

        //when

        //then

        assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1L, postEdit);

        });

    }



}