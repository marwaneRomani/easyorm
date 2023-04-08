package org.easyorm.entitiesdtasource;

import java.util.HashMap;
import java.util.Map;

public class EntitiesDataSource {
    private static String entitiesPath;
    private static Map<String, Entity> entitiesMapSchema = new HashMap<>();
    private EntitiesDataSource() { }
    public static Map<String, Entity> getModelsSchemas() {
        return entitiesMapSchema;
    }
    public static void setSchemas(Map<String, Entity> schemas) {
        entitiesMapSchema = schemas;
    }
    public static String getEntitiesPath() {
        return entitiesPath;
    }
    public static void setEntitiesPath(String entitiesPath) {
        EntitiesDataSource.entitiesPath = entitiesPath;
    }

}
