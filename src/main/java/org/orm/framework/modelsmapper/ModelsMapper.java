package org.orm.framework.modelsmapper;

import java.lang.reflect.Modifier;
import java.util.List;

import org.orm.framework.entitiesdtasource.EntitiesDataSource;
import org.orm.framework.entitiesdtasource.Entity;
import org.orm.framework.modelsmapper.mappers.MapInheretedAttributes;
import org.orm.framework.modelsmapper.mappers.MapNormalAttributes;
import org.orm.framework.modelsmapper.mappers.MapRelations;
import org.orm.framework.modelsmapper.mappers.ModelsLoader;
import org.orm.framework.customexception.ORMException;


public class ModelsMapper {
    public static void map(String modelsPath) {
        try {

            List<Class<?>> models = ModelsLoader.getModels(modelsPath);

            EntitiesDataSource.setEntitiesPath(modelsPath);

            mapNormalAttributes(models);
            mapInheritedAttributes();
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
