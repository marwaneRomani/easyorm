package org.orm.framework.ModelsMapper.FieldsMapper.Relation;


import org.orm.framework.ModelsMapper.FieldsMapper.Attribute.Attribute;

public abstract class Relation {
    private Attribute one;
    private Attribute two;

    public Relation(Attribute one, Attribute two) {
        this.one = one;
        this.two = two;
    }
    public Attribute getOne() {
        return one;
    }

    public void setOne(Attribute one) {
        this.one = one;
    }

    public Attribute getTwo() {
        return two;
    }

    public void setTwo(Attribute two) {
        this.two = two;
    }

    public abstract String toString();
    public abstract Boolean isIgnored();
    public abstract String foreignKeyRef();


}