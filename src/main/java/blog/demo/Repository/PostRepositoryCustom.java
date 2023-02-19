package blog.demo.Repository;

import blog.demo.Domain.Post;
import blog.demo.Request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
