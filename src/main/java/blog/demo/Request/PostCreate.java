package blog.demo.Request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
public class PostCreate {

    @NotNull(message = "타이틀을 입력해주세요.")
    @NotBlank(message = "타이틀을 입력해주세요.")//""과 null 값이 체크
    private String title;

    @NotNull(message = "콘텐츠를 입력해주세요.")
    @NotBlank(message = "콘텐츠를 입력해주세요.")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }
    // 빌더의 장점
    // - 가독성에 좋다.
    // - 필요한 값만 받을 수 있다.
    // - 오버로딩 가능한 조건 찾아보기
    // - 객체의 붊변성
}
