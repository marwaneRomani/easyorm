package org.orm.framework.ModelsMapper.FieldsMapper.Relation;


import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;
import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.AttributeList;

import java.lang.reflect.Modifier;

public class ManyToMany extends Relation {
    private String tableName;
    private Attribute first;
    private Attribute last;

    public ManyToMany(Attribute first, Attribute last) {
        super(first, last);
        this.first = first;
        this.last = last;
        this.tableName = ((AttributeList) first).getGenericType().toLowerCase() +"_"+ ((AttributeList) last).getGenericType().toLowerCase();
    }

    public Attribute getFirst() {
        return first;
    }

    public void setFirst(Attribute first) {
        this.first = first;
    }

    public Attribute getLast() {
        return last;
    }

    public void setLast(Attribute last) {
        this.last = last;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "ManyToMany{" +
                "tableName='" + tableName + '\'' +
                ", first=" + first.getType() +
                ", last=" + last.getType() +
                '}';
    }

    @Override
    public Boolean isIgnored() {
        return Modifier.isAbstract(
                first.getClazz().getModifiers()) ||
                Modifier.isAbstract(last.getClazz().getModifiers()
        );
    }

    @Override
    public String foreignKeyRef() {
        return null;
    }
}
