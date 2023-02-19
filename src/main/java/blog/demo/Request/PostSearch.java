package blog.demo.Request;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PostSearch {

    private static final int MAX_SIZE = 2000;
    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;


    public long getOffset(){
        return (long) (Math.max(1, page) - 1) * Math.min(MAX_SIZE, 2000);
    }
}
