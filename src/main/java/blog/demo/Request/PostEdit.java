package blog.demo.Request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
public class PostEdit {
    @NotNull(message = "타이틀을 입력해주세요.")
    @NotBlank(message = "타이틀을 입력해주세요.")//""과 null 값이 체크
    private String title;

    @NotNull(message = "콘텐츠를 입력해주세요.")
    @NotBlank(message = "콘텐츠를 입력해주세요.")
    private String content;


    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
