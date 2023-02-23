package org.orm.framework.ModelsMapper.FieldsMapper.Attribute;

public class AttributeList extends Attribute {
    private String genericType ;

    public AttributeList() {
        super();
    }

    public AttributeList(AttributeList a) {
        super(a);
        this.genericType = a.getGenericType();
    }

    public String getGenericType() {
        return genericType;
    }

    public void setGenericType(String genericType) {
        this.genericType = genericType;
    }
}
