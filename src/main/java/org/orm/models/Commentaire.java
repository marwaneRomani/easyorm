package org.orm.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public @Data class Commentaire {
    private Integer id;
    private String content;
    private Date date;

    private Post post;
    private User user;
    private List<SubCommentaire> subCommentaires;
}
