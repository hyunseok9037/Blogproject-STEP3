package shop.mtcoding.blog.model;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Love {
    private Integer id;
    private Integer boardId;
    private Integer userId;
    private Timestamp createdAt;
}
