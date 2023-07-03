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
public class MaterialsItemWithTitle {
    private String title;
    private List<MaterialsItem> presentationArticles;
}
