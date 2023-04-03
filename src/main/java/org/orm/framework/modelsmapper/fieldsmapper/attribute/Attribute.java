package org.orm.framework.modelsmapper.fieldsmapper.attribute;

import java.util.ArrayList;
import java.util.List;

public class Attribute {
    protected Class<?> clazz;

    protected String name;
    protected String originalName;
    protected String type;
    protected String genType;
    protected Boolean isInherited;
    protected Boolean mapped;
    protected Boolean nullable;
    protected Integer length;
    protected Boolean unique;
    protected List<Class<?>> heritant;
    protected String dbType;
    protected Boolean isAutoIncrement;

    public Attribute() {
        this.mapped = false;
        this.heritant = new ArrayList<>();
        this.nullable = true;
        this.isAutoIncrement = false;
        this.unique = false;
        this.isInherited = false;

    }

    public Attribute(Attribute other) {
        this.clazz = other.clazz;
        this.name = other.name;
        this.type = other.type;
        this.isInherited = other.isInherited;
        this.mapped = other.mapped;
        this.heritant = other.heritant;
        this.nullable = other.nullable;
        this.length = other.length;
        this.unique = other.unique;
        this.isAutoIncrement = other.isAutoIncrement;
        this.dbType = other.dbType;
    }

    public String getOriginalName() {
        return originalName;
    }
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }


    public Class<?> getClazz() { return clazz; }
    public void setClazz(Class<?> clazz) { this.clazz = clazz; }

    public Integer getLength() {
        return length;
    }
    public void setLength(Integer length) {
        this.length = length;
    }

    public Boolean isNullable() {
        return nullable;
    }
    public void setNullable(Boolean nullable) { this.nullable = nullable; }

    public Boolean isUnique() {
        return unique;
    }
    public void setUnique(Boolean unique) { this.unique = unique; }

    public Boolean isInherited() {
        return isInherited;
    }
    public void setInherited(Boolean inherited) { isInherited = inherited; }

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Boolean isMapped() { return mapped; }
    public void mapp(Boolean mapped) { this.mapped = mapped; }

    public void addHeritant(Class<?> heriant) {
        this.heritant.add(heriant);
    }
    public List<Class<?>> getHeritant() { return heritant; }

    public String getDbType() { return dbType; }
    public void setDbType(String dbType) { this.dbType = dbType; }

    public Boolean isAutoIncrement() { return isAutoIncrement; }
    public void setAutoIncrement(Boolean autoIncrement) { isAutoIncrement = autoIncrement; }

}