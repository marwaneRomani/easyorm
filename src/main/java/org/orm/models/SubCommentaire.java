package org.orm.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public @Data class SubCommentaire {
    private Long id;
    private String content;
    private Date date;

    private Commentaire commentaire;
    private User user;
}
