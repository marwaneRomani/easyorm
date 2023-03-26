package org.orm.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public @Data class User {
    private String cin;
    private String cne;
    private String name;
    private String email;
    private String lastName;
    private Integer age;

    private List<Message> sentMessages;
    private List<Message> receivedMessages;
    private Filiere filiere;
    private List<Notification> notifications;
    private List<Post> posts;
    private List<Commentaire> commentaires;
    private List<SubCommentaire> subCommentaires;
    private List<LignePostReaction> lignePostReactions;
}
