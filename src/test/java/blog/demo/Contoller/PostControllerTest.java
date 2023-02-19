package blog.demo.Contoller;

import blog.demo.Domain.Post;
import blog.demo.Repository.PostRepository;
import blog.demo.Request.PostCreate;
import blog.demo.Request.PostEdit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다")
    void test() throws Exception {
        //given
        PostCreate request = PostCreate
                .builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        Post post = Post.builder().title("제목입니다.").content("내용입니다.").build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                ) //application/json

                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());

    }

    @Test
    @DisplayName("/posts 요청시 title 값은 필수다")
    void test2() throws Exception {
        // 글 제목
        PostCreate request = PostCreate
                .builder()
                .title(null)
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);
        // 글 내용
        //expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                ) //application/json
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());

    }

    @Test
    @DisplayName("/posts 요청시 db에 값이 저장된다.")
    void test3() throws Exception {

        PostCreate request = PostCreate
                .builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);
        mockMvc.perform(
                        post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        assertThat(postRepository.count()).isEqualTo(1);

        Post post = postRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("제목입니다.");
        assertThat(post.getContent()).isEqualTo("내용입니다.");
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //given
        Post post = Post.builder().title("foo182739812738712893719").content("bar").build();
        postRepository.save(post);
        //when
        mockMvc.perform(
                        get("/posts/{postId}", post.getId())
                                .contentType(MediaType.APPLICATION_JSON)

                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("foo1827398"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());

        //then
    }
    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        //given

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post
                        .builder()
                        .title("호돌맨 제목 " + i)
                        .content("반포자이 " +  i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);
        //when
        mockMvc.perform(
                        get("/posts?page=1&size=10")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("호돌맨 제목 30"))
                .andExpect(jsonPath("$[0].content").value("반포자이 30"))
                .andDo(print());

        //then
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가녀온다.")
    void test6() throws Exception {
        //given

        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post
                        .builder()
                        .title("호돌맨 제목 " + i)
                        .content("반포자이 " +  i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);
        //when
        mockMvc.perform(
                        get("/posts?page=0&size=10")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].title").value("호돌맨 제목 30"))
                .andExpect(jsonPath("$[0].content").value("반포자이 30"))
                .andDo(print());

        //then
    }

    @Test
    @DisplayName("글 내용 수정")
    void test7() throws Exception {
        //given
        Post post = Post
                .builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().title("호돌맨").content("초가집").build();
        //when
        mockMvc.perform(
                        patch("/posts/{postId}",post.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
    }

    @Test
    @DisplayName("글 제목 수정")
    void test8() throws Exception {
        //given
        Post post = Post
                .builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder().title("호돌걸").content("반포자이").build();
        //when
        mockMvc.perform(
                        patch("/posts/{postId}",post.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
    }
}