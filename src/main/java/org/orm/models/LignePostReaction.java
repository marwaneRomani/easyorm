package org.orm.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public @Data class LignePostReaction {
    private Integer id;

    private Userr userr;
    private Post post;
    private Reaction reaction;
}
