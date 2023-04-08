package org.easyorm.modelsmapper;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

import org.easyorm.entitiesdtasource.EntitiesDataSource;
import org.easyorm.entitiesdtasource.Entity;
import org.easyorm.modelsmapper.mappers.MapInheretedAttributes;
import org.easyorm.modelsmapper.mappers.MapNormalAttributes;
import org.easyorm.modelsmapper.mappers.MapRelations;
import org.easyorm.modelsmapper.mappers.ModelsLoader;
import org.easyorm.customexception.ORMException;


public class ModelsMapper {
    public static void map(String modelsPath) {
        try {

            List<Class<?>> models = ModelsLoader.getModels(modelsPath);

            EntitiesDataSource.setEntitiesPath(modelsPath);

            mapNormalAttributes(models);
            mapInheritedAttributes();
            Map<String, Entity> modelsSchemas = EntitiesDataSource.getModelsSchemas();
            mapRelations();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void mapNormalAttributes(List<Class<?>> models) {
        models
                .forEach((model) -> {
                    Entity entity = new Entity(model);
                    if (Modifier.isAbstract(model.getModifiers()))
                        entity.setAbstract(true);

                    try {
                        MapNormalAttributes.mapModels(entity);
                    } catch (ORMException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                    EntitiesDataSource
                            .getModelsSchemas()
                            .put(model.getSimpleName(), entity);
                });

    }

    private static void mapInheritedAttributes() {
        EntitiesDataSource
                .getModelsSchemas()
                .entrySet()
                .stream()
                .filter(entity -> entity.getValue().getSupperClass() != Object.class)
                .forEach(entity -> MapInheretedAttributes.mapInheritance(entity.getValue()));
    }

    private static void mapRelations() {
        EntitiesDataSource
                .getModelsSchemas()
                .forEach((model, entity) -> MapRelations.mapRelations(entity));
    }

}
