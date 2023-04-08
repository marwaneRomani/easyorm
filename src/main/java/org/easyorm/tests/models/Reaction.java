package org.easyorm.tests.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public @Data class Reaction {
    private Integer id;
    private String title;
    private String iconPath;

    private List<LignePostReaction> lignePostReactionList;
}
