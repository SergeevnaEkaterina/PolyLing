package model.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class AnalyseByLinkResponse {
    private Boolean success;
    private Integer result;
    private Integer total;
    private Integer classField;
    private String overallComment;
    private List<Comment> comments;


}
