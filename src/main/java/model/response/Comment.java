package model.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Comment {
    private String key;
    private Boolean value;
    private String title;
    private String comment;
}
