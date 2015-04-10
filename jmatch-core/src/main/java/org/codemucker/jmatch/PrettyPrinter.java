package org.codemucker.jmatch;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PrettyPrinter {

	public static String toPrettyString(Object val) {
		if (val == null) {
			return "null";
		}
		if (val instanceof String) {
			return (String) val;
		}
		if (val instanceof Map) {
			StringBuilder sb = new StringBuilder();
			Map<?, ?> map = (Map<?, ?>) val;
			sb.append("map<size:" + map.size() + ">:");
			boolean sep = false;
			for (Entry<?, ?> entry : map.entrySet()) {
				Object key = entry.getKey();
				Object value = entry.getValue();
				if (sep) {
					sb.append(',');
				}
				sb.append("\n\t").append(key).append("=").append(value);
				sep = true;
			}
			return sb.toString();
		}
		if (val instanceof Collection) {
			StringBuilder sb = new StringBuilder();
			Collection<?> col = (Collection<?>) val;
			String listType = "collection";
			if(val instanceof List){
				listType = "list";
			} else if(val instanceof Set){
				listType = "set";
			}
			sb.append(listType + "<size:" + col.size() + ">:");
			int i = 0;
			boolean sep = false;
			for (Object item : col) {
				if (sep) {
					sb.append(',');
				}
				sb.append("\n\titem[").append(i).append("]=").append(item);
				sep = true;
				i++;
			}
			return sb.toString();
		}
		if(val instanceof Date){
			Date d = (Date)val;
			return new SimpleDateFormat("yyyy/MM/dd Z HH:mm:ss.fff").format(d);
		}

		return val.toString();
	}
}
