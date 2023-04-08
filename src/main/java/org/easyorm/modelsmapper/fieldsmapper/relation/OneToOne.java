package org.easyorm.modelsmapper.fieldsmapper.relation;


import org.easyorm.modelsmapper.fieldsmapper.attribute.Attribute;

import java.lang.reflect.Modifier;

public class OneToOne extends Relation {
    private Attribute first;
    private Attribute second;

    public OneToOne(Attribute first, Attribute second) {
        super(first, second);
        this.first = first;
        this.second = second;
    }

    public Attribute getFirst() {
        return first;
    }

    public void setFirst(Attribute first) {
        this.first = first;
    }

    public Attribute getSecond() {
        return second;
    }

    public void setSecond(Attribute second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return "OneToOne{" +
                "first=" + first.getType() +
                ", second=" + second.getType() +
                '}';
    }

    @Override
    public Boolean isIgnored() {
        return Modifier.isAbstract(
                first.getClazz().getModifiers()) ||
                Modifier.isAbstract(second.getClazz().getModifiers()
        );
    }

    @Override
    public String foreignKeyRef() {
        return first.getName();
    }

}
