package org.easyorm.entitiesdtasource;

import org.easyorm.modelsmapper.fieldsmapper.attribute.Attribute;
import org.easyorm.modelsmapper.fieldsmapper.attribute.AttributeList;
import org.easyorm.modelsmapper.fieldsmapper.primarykey.PrimaryKey;
import org.easyorm.modelsmapper.fieldsmapper.relation.ManyToMany;
import org.easyorm.modelsmapper.fieldsmapper.relation.OneToMany;
import org.easyorm.modelsmapper.fieldsmapper.relation.OneToOne;
import org.easyorm.modelsmapper.fieldsmapper.relation.Relation;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    private Class<?> model;
    private String name;
    private Class<?> supperClass;
    private Boolean isAbstract;
    private PrimaryKey primaryKey;
    private List<Attribute> normalAttributes;
    private List<Attribute> relationalAtrributes;
    private List<Attribute> relationalInheritedAtrributes ;
    private List<Relation> relations;
    private List<Relation> unsavedRelationalAttributes; // relation that their relation was saved in other entity
    private List<Attribute> foreignKeys; // list of attributes that will be a fk in the database table

    public Entity(Class<?> model) {
        relations = new ArrayList<>();
        relationalInheritedAtrributes = new ArrayList<>();
        unsavedRelationalAttributes = new ArrayList<>();
        foreignKeys = new ArrayList<>();
        this.model = model;
        this.isAbstract = false;
    }

    public Class<?> getModel() {
        return model;
    }
    public void setModel(Class<?> model) {
        this.model = model;
    }

    public String getName() {
        return name.toLowerCase();
    }
    public void setName(String name) {
        this.name = name.toLowerCase();
    }

    public Class<?> getSupperClass() {
        return supperClass;
    }
    public void setSupperClass(Class<?> supperClass) { this.supperClass = supperClass; }

    public Boolean isAbstract() {
        return isAbstract;
    }
    public void setAbstract(Boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }
    public void setPrimaryKey(PrimaryKey primaryKey) {
        this.primaryKey = primaryKey;
    }

    public List<Attribute> getNormalAttributes() {
        return normalAttributes;
    }
    public void setNormalAttributes(List<Attribute> normalAttributes) {
        this.normalAttributes = normalAttributes;
    }

    public List<Attribute> getRelationalAtrributes() {
        return relationalAtrributes;
    }
    public void setRelationalAtrributes(List<Attribute> relationalAtrributes) { this.relationalAtrributes = relationalAtrributes; }

    public List<Relation> getRelations() {
        return relations;
    }
    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public List<Relation> getUnsavedRelationalAttributes() { return unsavedRelationalAttributes; }
    public void setUnsavedRelationalAttributes(List<Relation> unsavedRelationalAttributes) { this.unsavedRelationalAttributes = unsavedRelationalAttributes; }


    public List<Attribute> getForeignKeys() { return foreignKeys; }
    public void setForeignKey(Attribute foreignKey) { this.foreignKeys.add(foreignKey); }



    public void addNormalInheritedAttr(Attribute a) { this.normalAttributes.add(a);}
    public void addRelationalInheritedAttr(Attribute a) {
        this.relationalInheritedAtrributes.add(a);
    }

    public List<Attribute> getRelationalInheritedAttr() {
        return this.relationalInheritedAtrributes;
    }


    //TODO need to refactor
    public void subscribeToRelationEvent(Relation relation) { relations.add(relation); }


    //TODO need to refactor
    public void subscribeToRelationEvent(Attribute notifier,Attribute other) {
        Attribute a;
        Attribute f;
        Relation r;

        if (notifier instanceof AttributeList ) {
            a = new AttributeList();

            a.setClazz(this.model);
            a.setType(List.class.getSimpleName());
            ((AttributeList) a).setGenericType(((AttributeList) notifier).getGenericType());

            a.setName(this.name + '_' + notifier.getName());
            a.setOriginalName(notifier.getOriginalName());
            a.setInherited(true);
            a.setUnique(notifier.isUnique());
            a.setNullable(notifier.isNullable());
            a.mapp(true);


            f = new Attribute();
            f.setClazz(other.getClazz());
            f.setType(this.model.getSimpleName());
            f.setName(this.name + '_' + other.getName());
            f.setOriginalName(other.getOriginalName());
            f.setInherited(true);
            f.setUnique(other.isUnique());
            f.setUnique(other.isNullable());
            f.mapp(true);

            if (other instanceof AttributeList ) {
                f = new AttributeList();
                f.setClazz(other.getClazz());
                f.setType(List.class.getSimpleName());
                ((AttributeList)f).setGenericType(this.model.getSimpleName());
                f.setName(this.name + '_' + other.getName());
                f.setOriginalName(other.getOriginalName());
                f.setInherited(true);
                f.setUnique(other.isUnique());
                f.setUnique(other.isNullable());
                f.mapp(true);

                r = new ManyToMany(
                        f.getName().compareTo(a.getName()) <= 0 ? f : a ,
                        a.getName().compareTo(f.getName()) > 0 ? a : f
                );
            }
            else {
                r = new OneToMany(f ,(AttributeList) a);
                // register the first attribute as fk of the table
                EntitiesDataSource
                        .getModelsSchemas()
                        .get(f.getClazz().getSimpleName())
                        .setForeignKey(f);
            }
        }
        else {
            a = new Attribute();
            a.setClazz(this.model);
            a.setName(this.name + '_' + notifier.getName());
            a.setOriginalName(notifier.getOriginalName());
            a.setInherited(true);
            a.setType(other.getClazz().getSimpleName());
            a.mapp(true);

            f = new Attribute();
            f.setClazz(other.getClazz());
            f.setType(this.model.getSimpleName());
            f.setName(this.name + '_' + other.getName());
            f.setOriginalName(other.getOriginalName());
            f.setInherited(true);
            f.setUnique(other.isUnique());
            f.setUnique(other.isNullable());
            f.mapp(true);

            if ( other instanceof AttributeList ) {
                f = new AttributeList();
                f.setClazz(other.getClazz());
                f.setType(List.class.getSimpleName());
                ((AttributeList) f).setGenericType(this.model.getSimpleName());
                f.setName(this.name + '_' + other.getName());
                f.setOriginalName(other.getOriginalName());
                f.setInherited(true);
                f.setUnique(other.isUnique());
                f.setUnique(other.isNullable());
                f.mapp(true);

                r = new OneToMany(a, (AttributeList) f);
                // register the first attribute as fk of the table
                EntitiesDataSource
                        .getModelsSchemas()
                        .get(a.getClazz().getSimpleName())
                        .setForeignKey(a);
            }
            else {
                r = new OneToOne(f,a);

                // register the first attribute as fk of the table
                EntitiesDataSource
                        .getModelsSchemas()
                        .get(f.getClazz().getSimpleName())
                        .setForeignKey(f);
            }
        }


        EntitiesDataSource
                .getModelsSchemas()
                .get(other.getClazz().getSimpleName())
                .addRelationalInheritedAttr(f);

        //relationalAtrributes.add(f);
        this.addRelationalInheritedAttr(a);
        this.relations.add(r);

        if (notifier instanceof AttributeList)
            EntitiesDataSource.getModelsSchemas().get(((AttributeList) notifier).getGenericType()).getUnsavedRelationalAttributes().add(r);
        else
            EntitiesDataSource.getModelsSchemas().get(notifier.getType()).getUnsavedRelationalAttributes().add(r);
    }

}
