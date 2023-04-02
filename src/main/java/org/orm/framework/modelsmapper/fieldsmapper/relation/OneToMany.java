package org.orm.framework.modelsmapper.fieldsmapper.relation;


import org.orm.framework.modelsmapper.fieldsmapper.attribute.Attribute;
import org.orm.framework.modelsmapper.fieldsmapper.attribute.AttributeList;

import java.lang.reflect.Modifier;

public class OneToMany extends Relation {
    private Attribute small;
    private AttributeList big;

    public OneToMany(Attribute small, AttributeList big) {
        super(small, big);
        this.small = small;
        this.big = big;
    }

    public Attribute getSmall() {
        return small;
    }

    public void setSmall(Attribute small) {
        this.small = small;
    }

    public Attribute getBig() {
        return big;
    }

    public void setBig(AttributeList big) {
        this.big = big;
    }

    @Override
    public String toString() {
        return "OneToMany{" +
                "small=" + small.getType() +
                ", big=" + big.getGenericType() +
                '}';
    }

    @Override
    public Boolean isIgnored() {
        return Modifier.isAbstract(
                small.getClazz().getModifiers()) ||
                Modifier.isAbstract(big.getClazz().getModifiers()
        );
    }

    @Override
    public String foreignKeyRef() {
        return small.getName();
    }

}
