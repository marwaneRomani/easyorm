package org.orm.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public @Data class Post {
    private Long id;
    private String content;
    private Date date;

    private User proprietaire;

    private List<Attachement> attachementList;

    private List<Commentaire> commentaires;

    private List<LignePostReaction> lignePostReactions;

}
