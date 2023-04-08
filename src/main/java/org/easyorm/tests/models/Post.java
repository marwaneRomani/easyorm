package org.easyorm.tests.models;

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

    private Userr proprietaire;

    private List<Attachement> attachementList;

    private List<Commentaire> commentaires;

    private List<LignePostReaction> lignePostReactions;

    public Post(Long id, String content, Date date) {
        this.id = id;
        this.content = content;
        this.date = date;
    }

    public Post(Long id, String content, Date date, Userr proprietaire) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.proprietaire = proprietaire;
    }
}
