package org.easyorm.tests.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.easyorm.modelsmapper.annotations.Id;

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
