package org.orm.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public @Data class Notification {
    private Integer id;
    private Boolean state;
    private Date date;
    private String content;
    private String url;

    private Userr userr;
}
