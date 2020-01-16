package hu.gdf.szgd.dishbrary.transformer;

import hu.gdf.szgd.dishbrary.transformer.exception.TransformationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Log4j2
public class GenericReflectionBasedTransformer {

	public enum AccessorType {
		GETTER("get"), SETTER("set");

		private String value;

		AccessorType(String value) {
			this.value = value;
		}
	}

	public <T> T transform(Object inputObject, T targetObject) {
		return transform(inputObject, targetObject, null);
	}

	public <T> T transform(Object inputObject, Class<T> targetClass) {
		return transform(inputObject, createInstance(targetClass), null);
	}

	public <T> T transform(Object inputObject, Class<T> targetClass, TransformerConfig config) {
		return transform(inputObject, createInstance(targetClass), config);
	}

	public <T> T transform(Object inputObject, T targetObject, TransformerConfig config) {
		Class<?> targetClass = targetObject.getClass();
		Class<?> entityClass = inputObject.getClass();

		log.trace("Start transformation from {} to {}", entityClass.getName(), targetClass.getName());

		List<Method> allSetterOfTarget = new ArrayList<>();

		Class<?> c = targetClass;
		log.trace("Start fetching setter methods of class: {}", targetClass.getName());

		while (c != null) {
			for (Method method : c.getDeclaredMethods()) {
				if (log.isTraceEnabled()) {
					log.trace("Method[{}] found in class: {}", method.getName(), c.getName());
				}

				if (method.getName().startsWith(AccessorType.SETTER.value)) {
					if (log.isTraceEnabled()) {
						log.trace("Setter method[{}] found for class: {}", method.getName(), c.getName());
					}

					String fieldName = getFieldNameBySetterOrGetter(method);
					if (TransformerConfig.isFieldExcludedInConfig(config, fieldName)) {
						log.debug("Field marked as excluded: {}", fieldName);

						continue;
					}

					allSetterOfTarget.add(method);
				}
			}

			c = c.getSuperclass();
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
					Object getValue = getterMethod.invoke(inputObject);
					if (log.isDebugEnabled()) {
						log.debug("Getter method of {} returned with {}", entityClass.getName(), getValue);
					}

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

	public <T> T createInstance(Class<T> targetClass) {
		try {
			if (log.isDebugEnabled()) {
				log.debug("Creating instance of {} with default constructor.", targetClass.getName());
			}

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

		if (log.isDebugEnabled()) {
			log.debug("Trying to retrieve method[{}] from class: {}", targetClass.getName(), methodName);
		}

		try {
			return Optional.of(targetClass.getMethod(methodName, paramTypes));
		} catch (NoSuchMethodException e) {
			String msg = String.format("Method[%s] of class: %s has not been found during mapping.", methodName, targetClass.getName());

			log.warn(msg);
		}

		return Optional.empty();
	}

	public Optional<Method> getSetterMethodByGetter(Method getterMethod, Class<?> targetClass) {
		String fieldName = getFieldNameBySetterOrGetter(getterMethod);

		if (log.isDebugEnabled()) {
			log.debug("Computed field name from getter method[{}]: {}", getterMethod.getName(), fieldName);
		}

		return getMethodByFieldName(AccessorType.SETTER, fieldName, targetClass, getterMethod.getReturnType());
	}

	public Optional<Method> getGetterMethodBySetter(Method setterMethod, Class<?> targetClass) {
		String fieldName = getFieldNameBySetterOrGetter(setterMethod);

		if (log.isDebugEnabled()) {
			log.debug("Computed field name from setter method[{}]: {}", setterMethod.getName(), fieldName);
		}

		Optional<Method> optionalGetter = getMethodByFieldName(AccessorType.GETTER, fieldName, targetClass);

		if (optionalGetter.isPresent() && !optionalGetter.get().getReturnType().equals(setterMethod.getParameterTypes()[0])) {
			return Optional.empty();
		}

		return optionalGetter;
	}

	private String getFieldNameBySetterOrGetter(Method setterOrGetterMethod) {
		String methodName = setterOrGetterMethod.getName();
		return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
	}
}
