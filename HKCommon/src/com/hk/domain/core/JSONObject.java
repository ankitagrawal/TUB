package com.hk.domain.core;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONValue;

/**
 * @author vaibhav
 */
@SuppressWarnings("unchecked")
public abstract class JSONObject implements Serializable, Cloneable {

    /**
     *
     */
    private static final long       serialVersionUID = 1036213105972756617L;

    public static final String      DEFAULT_ENCODING = "UTF-8";

    private static final Set<Class> numberClasses    = new HashSet<Class>();

    static {
        numberClasses.add(Long.class);
        numberClasses.add(Integer.class);
        numberClasses.add(Short.class);
        numberClasses.add(Double.class);
        numberClasses.add(Float.class);
        numberClasses.add(Byte.class);
        numberClasses.add(BigDecimal.class);
        numberClasses.add(BigInteger.class);
    };

    public static void append(String key, Object value, StringBuilder strBuilder) {
        strBuilder.append("\"");
        strBuilder.append(key);
        strBuilder.append("\":");
    }

    public static void appendValue(Object value, StringBuilder strBuilder) {
        if (value == null) {
            strBuilder.append("null");
        } else if (value instanceof String) {
            strBuilder.append("\"");
            strBuilder.append(escape((String) value));
            strBuilder.append("\"");
        } else if (value.getClass().equals(Boolean.class) || value.getClass().equals(Boolean.TYPE)) {
            strBuilder.append(value.toString());
        } else if (Map.class.isAssignableFrom(value.getClass())) {
            Map<?, ?> map = (Map<?, ?>) value;
            appendJSONMap(map, strBuilder);
        } else if (value.getClass().isArray()) {
            appendJSONList(Arrays.asList(value), strBuilder);
        } else if (Collection.class.isAssignableFrom(value.getClass())) {
            appendJSONList((Collection) value, strBuilder);
        } else if (value instanceof Date) {
            Calendar c = Calendar.getInstance();
            c.setTime((Date) value);
            strBuilder.append("new Date(");
            strBuilder.append(c.get(Calendar.YEAR));
            strBuilder.append(",");
            strBuilder.append((c.get(Calendar.MONTH)));
            strBuilder.append(",");
            strBuilder.append(c.get(Calendar.DAY_OF_MONTH));
            strBuilder.append(",");
            strBuilder.append(c.get(Calendar.HOUR_OF_DAY));
            strBuilder.append(",");
            strBuilder.append(c.get(Calendar.MINUTE));
            strBuilder.append(",0)");
        }
        // else if (value instanceof Double) {
        // return decimalFormat.format(value);
        // }
        else if (value.getClass().isEnum()) {
            strBuilder.append(value.toString());
        } else if (value instanceof JSONObject) {
            strBuilder.append(((JSONObject) value).toJSON());
        } else if (numberClasses.contains(value.getClass())) {
            strBuilder.append(value.toString());
        } else {           
          //strBuilder.append(net.sf.json.JSONObject.fromObject(value).toString());
          org.json.JSONObject jsonObject=new org.json.JSONObject(value);
          jsonObject.remove("class");
          strBuilder.append(jsonObject.toString());
        }
    }

    public static void appendJSONMap(Map<?, ?> someMap, StringBuilder strBuilder) {
        Iterator<?> iterator = someMap.keySet().iterator();
        strBuilder.append("{");
        while (iterator.hasNext()) {
            Object key = iterator.next();
            strBuilder.append("\"");
            strBuilder.append(key);
            strBuilder.append("\":");
            appendValue(someMap.get(key), strBuilder);
            if (iterator.hasNext()) {
                strBuilder.append(",");
            }
        }
        strBuilder.append("}");
    }

    public static void appendJSONList(Collection<?> someList, StringBuilder strBuilder) {
        Iterator<?> iterator = someList.iterator();
        strBuilder.append("[");
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            appendValue(obj, strBuilder);
            if (iterator.hasNext()) {
                strBuilder.append(",");
            }
        }
        strBuilder.append("]");
    }

    private final void appendJSON(StringBuilder strBuilder) {
        appendKeyValuePair(getKeys(), getValues(), strBuilder);
    }

    private static void appendKeyValuePair(String[] keys, Object[] values, StringBuilder strBuilder) {
        strBuilder.append("{");
        for (int i = 0; i < keys.length; i++) {
            strBuilder.append("\"");
            strBuilder.append(keys[i]);
            strBuilder.append("\":");
            appendValue(values[i], strBuilder);
            if (i < keys.length - 1) {
                strBuilder.append(",");
            }
        }
        strBuilder.append("}");
    }

    public final Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<String, Object>();
        String[] keys = getKeys();
        Object[] values = getValues();
        for (int i = 0; i < keys.length; i++) {
            data.put(keys[i], values[i]);
        }
        return data;
    }

    public static final Map<String, Object> toMap(String json) {
        return (Map<String, Object>) JSONValue.parse(json);
    }

    public String toJSON() {
        StringBuilder strBuilder = new StringBuilder();
        appendJSON(strBuilder);
        return strBuilder.toString();
    }

    protected abstract String[] getKeys();

    protected abstract Object[] getValues();

    public static String escape(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            switch (ch) {
                case 34: // '"'
                    sb.append("\\\"");
                    break;
                case 92: // '\\'
                    sb.append("\\\\");
                    break;
                case 8: // '\b'
                    sb.append("\\b");
                    break;
                case 12: // '\f'
                    sb.append("\\f");
                    break;
                case 10: // '\n'
                    sb.append("\\n");
                    break;
                case 13: // '\r'
                    sb.append("\\r");
                    break;
                case 9: // '\t'
                    sb.append("\\t");
                    break;
                case 47: // '/'
                    sb.append("\\/");
                    break;
                default:
                    if (ch >= 0 && ch <= '\037') {
                        String ss = Integer.toHexString(ch);
                        sb.append("\\u");
                        for (int k = 0; k < 4 - ss.length(); k++) {
                            sb.append('0');
                        }

                        sb.append(ss.toUpperCase());
                    } else {
                        sb.append(ch);
                    }
                    break;
            }
        }
        return sb.toString();
    }
}
