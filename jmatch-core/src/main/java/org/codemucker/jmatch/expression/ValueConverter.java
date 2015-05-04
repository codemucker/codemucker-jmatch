package org.codemucker.jmatch.expression;

import java.util.List;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.lang3.ClassUtils;

import com.google.common.collect.Lists;

public class ValueConverter {

	private static final List<String> PRIMITIVE_OBJECT_TYPES = Lists
			.newArrayList("java.lang.Short", "java.lang.Character",
					"java.lang.Boolean", "java.lang.Integer",
					"java.lang.Float", "java.lang.Long", "java.lang.Double");
	
	private static final ConvertUtilsBean CONVERTER = new ConvertUtilsBean();

	public boolean canConvert(Object from, Class<?> to) {
		if (from == null) {
			return to == String.class;
		}
		if (to == String.class) {
			return true;
		}
		if (ClassUtils.isAssignable(from.getClass(), to, /* auto box */true)) {
			return true;
		}
		if (from.getClass() == String.class && (to.isPrimitive() || PRIMITIVE_OBJECT_TYPES.contains(to.getName()))) {
			return true;
		}
		return false;
	}

	public Object convertTo(Object from, Class<?> to) {
		return CONVERTER.convert(from, to);
	}

}
