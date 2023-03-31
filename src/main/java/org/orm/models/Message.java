package org.orm.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public @Data class Message {
    private Long id;
    private String content;
    private Date date;
    private Boolean seen;

    private Userr sender;
    private Userr receiver;
}
