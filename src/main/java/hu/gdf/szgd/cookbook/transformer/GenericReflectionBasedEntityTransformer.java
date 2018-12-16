package hu.gdf.szgd.cookbook.transformer;

import hu.gdf.szgd.cookbook.db.entity.AbstractEntity;
import hu.gdf.szgd.cookbook.transformer.exception.TransformationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Log4j2
public class GenericReflectionBasedEntityTransformer {

    public enum AccessorType {
        GETTER("get"), SETTER("set");

        private String value;

        AccessorType(String value) {
            this.value = value;
        }
    }

    public <T> T transform(AbstractEntity entity, T targetObject) {
        Class<?> targetClass = targetObject.getClass();
        Class<? extends AbstractEntity> entityClass = entity.getClass();

        log.trace("Start transformation from {} to {}", entityClass.getName(), targetClass.getName());

        List<Method> allSetterOfTarget = new ArrayList<>();

        Class<?> c = targetClass;
        log.trace("Start fetching setter methods of class: {}", targetClass.getName());

        while (c != null) {
            for (Method method : c.getDeclaredMethods()) {
                log.trace("Method[{}] found in class: {}", method.getName(), c.getName());

                if (method.getName().startsWith(AccessorType.SETTER.value)) {
                    log.trace("Setter method[{}] found for class: {}", method.getName(), c.getName());
                    allSetterOfTarget.add(method);
                }
            }

            c = targetClass.getSuperclass();
        }

        if (log.isDebugEnabled()) {
            log.debug("All setter method names for class {}: {}",
                    () -> targetClass.getName(),
                    () -> allSetterOfTarget.stream().map(method -> method.getName()).collect(Collectors.joining(", ")));
        }

        for (Method setterMethod : allSetterOfTarget) {
            Optional<Method> getterMethodOfInput = getGetterMethodBySetter(setterMethod, entityClass);

            getterMethodOfInput.map(getterMethod -> {
                try {
                    Object getValue = getterMethod.invoke(entity);
                    log.debug("Getter method of {} returned with {}", entityClass.getName(), getValue);

                    return setterMethod.invoke(targetObject, getValue);
                } catch (Exception e) {
                    String msg = String.format("Unexpected exception during transformation of classes: input[%s], target[%s]!",
                            entityClass.getName(),
                            targetClass.getName());

                    log.error(msg, e);

                    throw new TransformationException(msg, e);
                }
            });
        }

        return targetObject;
    }

    public <T> T transform(AbstractEntity entity, Class<T> targetClass) {
        return transform(entity, createInstance(targetClass));
    }

    public <T> T createInstance(Class<T> targetClass) {
        try {
            log.debug("Creating instance of {} with default constructor.", targetClass.getName());

            return targetClass.newInstance();
        } catch (Exception e) {
            String msg = String.format("Unexpected exception during creating instance of class: %s", targetClass.getName());

            log.error(msg, e);

            throw new TransformationException(msg, e);
        }
    }

    public Optional<Method> getMethodByFieldName(AccessorType accessorType, String fieldName, Class<?> targetClass, Class<?>... paramTypes) {
        StringBuilder methodNameBuilder = new StringBuilder(accessorType.value);

        methodNameBuilder.append(Character.toUpperCase(fieldName.charAt(0)))
                .append(fieldName.substring(1));

        String methodName = methodNameBuilder.toString();

        log.debug("Trying to retrieve method[{}] from class: {}", targetClass.getName(), methodName);

        try {
            return Optional.of(targetClass.getMethod(methodName, paramTypes));
        } catch (NoSuchMethodException e) {
            String msg = String.format("Unexpected exception during access method[%s] of class: %s",  methodName, targetClass.getName());

            log.warn(msg);
        }

        return Optional.empty();
    }

    public Optional<Method> getSetterMethodByGetter(Method getterMethod, Class<?> targetClass) {
        String getterName = getterMethod.getName();
        String fieldName = Character.toLowerCase(getterName.charAt(3)) + getterName.substring(4);

        log.debug("Computed field name from getter method[{}]: {}", getterName, fieldName);

        return getMethodByFieldName(AccessorType.SETTER, fieldName, targetClass, getterMethod.getReturnType());
    }

    public Optional<Method> getGetterMethodBySetter(Method setterMethod, Class<?> targetClass) {
        String setterName = setterMethod.getName();
        String fieldName = Character.toLowerCase(setterName.charAt(3)) + setterName.substring(4);

        log.debug("Computed field name from setter method[{}]: {}", setterName, fieldName);

        return getMethodByFieldName(AccessorType.GETTER, fieldName, targetClass);
    }
}
