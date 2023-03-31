package org.orm.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.orm.framework.ModelsMapper.Annotations.Id;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
public @Data class SubCommentaire {
    @Id(autoIncrement = true)
    private Long id;
    private String content;
    private Date date;

    private Commentaire commentaire;
    private Userr userr;
}
