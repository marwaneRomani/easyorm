package org.orm.framework.datamapper.ObjectBuilders.find;

import java.util.ArrayList;
import java.util.List;

public class Condition {
    private List<String> conditons = new ArrayList<>();
    private List<Object> values = new ArrayList<>();

    public void addCondition(String condition, Object value) {
        conditons.add(condition);
        values.add(value);
    }

    public String[] getContions() {
        return (String[]) conditons.toArray();
    }

    public Object[] getValues() {
        return values.toArray();
    }
}
