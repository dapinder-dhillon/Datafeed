/*
 * Copyright 2015-2016 Dapinder Singh, Inc. All Rights Reserved.
 *
*/
package com.dapinder.data.feed.validate.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;

import com.dapinder.data.feed.annotations.PropertyLabel;
import com.dapinder.data.feed.exception.DataFeedRuntimeException;
import com.dapinder.data.feed.exception.Notification;
import com.dapinder.data.feed.util.FeedSupportedFileTypes;
import com.dapinder.data.feed.validate.DataFeedValidate;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.configuration.annotation.AnnotationsConfigurer;
import net.sf.oval.configuration.annotation.BeanValidationAnnotationsConfigurer;

/**
 * The Class DataFeedValidateOval.
 */
@Component("dataFeedValidateOval")
public class DataFeedValidateOval implements DataFeedValidate {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataFeedValidateOval.class);

	@Override
	public <T> Notification validate(Map<String, T> fedData, Class<T> clazz, FeedSupportedFileTypes fileType) {
		LOGGER.debug("Started Validating the set of Imported files..");
		final Notification notification = new Notification();
		if (!CollectionUtils.isEmpty(fedData)) {

			BeanPropertyBindingResult result = new BeanPropertyBindingResult(clazz, clazz.getName());

			final String methodName = getInvokeMethodName(clazz, fileType);
			final net.sf.oval.Validator validator = new net.sf.oval.Validator(new AnnotationsConfigurer(),
					new BeanValidationAnnotationsConfigurer());

			String rootName = result.getObjectName();
			if (StringUtils.isNotEmpty(rootName)) {
				rootName = rootName.substring(rootName.lastIndexOf('.') + 1);
				rootName = new StringBuilder(rootName.substring(0, 1).toLowerCase())
						.append(rootName.substring(1, rootName.length())).toString();
			}

			for (final String key : fedData.keySet()) {
				try {
					final Method method = clazz.getMethod(methodName);
					Object returnValue = method.invoke(fedData.get(key));
					final List<T> csvRowList = (List<T>) returnValue;
					if (!CollectionUtils.isEmpty(csvRowList)) {
						final Map<String, String> propertyNameValueMap = getFieldPropertyNameValueMap(
								csvRowList.get(0).getClass());
						for (T t2 : csvRowList) {
							final List<ConstraintViolation> violations = validator.validate(t2);
							for (final ConstraintViolation constraintViolation : violations) {
								LOGGER.error(new StringBuilder("Found violation(s) for file: ").append(key)
										.append(" are: ").append(constraintViolation.getMessage()).toString());
								final String violationMessage = constraintViolation.getMessage();
								if (StringUtils.isNotEmpty(violationMessage)) {
									final String pkgClzFieldName = constraintViolation.getContext().toString();
									final String fieldName = pkgClzFieldName
											.substring(pkgClzFieldName.lastIndexOf(".") + 1, pkgClzFieldName.length());
									final String fieldNamePropValue = propertyNameValueMap.get(fieldName);
									String errorCode = null;
									if (StringUtils.isNotEmpty(fieldNamePropValue)) {
										errorCode = StringUtils.replace(violationMessage, pkgClzFieldName,
												new StringBuilder(fieldNamePropValue).append("('")
														.append(constraintViolation.getInvalidValue().toString())
														.append("')").toString());
									} else {
										errorCode = StringUtils.replace(violationMessage, pkgClzFieldName,
												new StringBuilder(fieldName).append("('")
														.append(constraintViolation.getInvalidValue().toString())
														.append("')").toString());
									}

									notification.addError(errorCode);
								}
							}
						}
					}

				} catch (NoSuchMethodException e) {
					LOGGER.error("NoSuchMethodException: " + e);
					throw new DataFeedRuntimeException(e);
				} catch (SecurityException e) {
					LOGGER.error("SecurityException: " + e);
					throw new DataFeedRuntimeException(e);
				} catch (IllegalAccessException e) {
					LOGGER.error("IllegalAccessException: " + e);
					throw new DataFeedRuntimeException(e);
				} catch (IllegalArgumentException e) {
					LOGGER.error("IllegalArgumentException: " + e);
					throw new DataFeedRuntimeException(e);
				} catch (InvocationTargetException e) {
					LOGGER.error("InvocationTargetException: " + e);
					throw new DataFeedRuntimeException(e);
				}
			}
			if (/* !CollectionUtils.isEmpty(validationErrors) */notification.hasErrors()) {
				// ValidationResult res = new ValidationResult();
				// res.setValidationErrors(validationErrors);
				// throw new ValidationException("ValidationException Importing
				// files from: ", res);
				// throw new ValidationException("ValidationException Importing
				// files from: ", notification);
			}
		}
		LOGGER.debug("End Validating the set of Imported files..");
		return notification;
	}

	/**
	 * Gets all fields of SO in a name value map.
	 *
	 * @param <T>
	 *            the generic type
	 * @param clazz
	 *            the clazz
	 * @return the field property name value map
	 */
	private <T> Map<String, String> getFieldPropertyNameValueMap(Class<T> clazz) {
		final Map<String, String> propertyNameValueMap = new HashMap<String, String>();
		final Field[] fields = clazz.getDeclaredFields();
		for (final Field field : fields) {
			if (null != field.getAnnotations()) {
				for (final Annotation annotation : field.getAnnotations()) {
					if (annotation instanceof PropertyLabel) {
						final PropertyLabel propertyName = (PropertyLabel) annotation;
						propertyNameValueMap.put(field.getName(), propertyName.value());
					}
				}
			}
		}

		/*
		 * Map<String, String> propertyNameValueI18N = Collections.emptyMap();
		 *
		 * if (MapUtils.isNotEmpty(propertyNameValueMap)) { final Map<String,
		 * String> i18NMap = i18nMessageSource .getMessages(new
		 * ArrayList<String>(propertyNameValueMap.values()));
		 * propertyNameValueI18N = new HashMap<String,
		 * String>(propertyNameValueMap.size()); final Set<String> keys =
		 * propertyNameValueMap.keySet(); for (final String key : keys) { final
		 * String label = propertyNameValueMap.get(key); final String i18NValue
		 * = i18NMap.get(label); if (StringUtils.isNotEmpty(i18NValue)) {
		 * propertyNameValueI18N.put(key, i18NValue); } else {
		 * propertyNameValueI18N.put(key, label); } } }
		 */

		return propertyNameValueMap;
	}

	/**
	 * Invoke method name.
	 *
	 * @param <T>
	 *            the generic type
	 * @param clazz
	 *            the clazz
	 * @param fileType
	 *            the file type
	 * @return the string
	 */
	private <T> String getInvokeMethodName(Class<T> clazz, final FeedSupportedFileTypes fileType) {

		String methodToInvoke = null;

		if (fileType.equals(FeedSupportedFileTypes.XML)) {
			final Method[] methods = clazz.getDeclaredMethods();
			for (final Method method : methods) {
				final Annotation[] annotations = method.getDeclaredAnnotations();
				for (final Annotation annotate : annotations) {
					if (annotate instanceof XmlElement) {
						methodToInvoke = new StringBuilder("g").append(method.getName().substring(1)).toString();
					}
				}
			}
		} else {
			final Field[] fields = clazz.getDeclaredFields();
			for (final Field field : fields) {
				final Annotation[] annotations = field.getDeclaredAnnotations();
				for (final Annotation annotate : annotations) {
					if (annotate instanceof JsonProperty) {
						methodToInvoke = new StringBuilder("get").append(field.getName().substring(0, 1).toUpperCase())
								.append(field.getName().substring(1)).toString();
					}
				}
			}
		}
		return methodToInvoke;
	}

}
