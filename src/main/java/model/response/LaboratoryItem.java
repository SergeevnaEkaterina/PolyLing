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
public class LaboratoryItem {
    private String description;
    private TeamItem leader;
    private List<TeamItem> team;
}
