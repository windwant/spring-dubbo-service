package org.windwant.spring.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class StringUtil {

    private StringUtil() {
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    public static String getStringOrEmpty(String str) {
    	return str == null ? "" : str;
    }
    public static String getStringOrEmpty(String str, String defaultStr) {
    	return str == null ? defaultStr : str;
    }

    /**
     * print string
     * @param o
     * @return
     */
    public static String reflectionToString(Object o){
        if(o == null) return StringUtils.EMPTY;
        StringBuilder toStr = new StringBuilder();
        if(o instanceof Collection){
            Iterator it = ((Collection) o).iterator();
            while (it.hasNext()){
                toStr.append(reflectionToString(it.next()));
                toStr.append("\n");
            }
        }else if(o instanceof Map){
            Iterator<Map.Entry> mit = ((Map) o).entrySet().iterator();
            while (mit.hasNext()){
                Map.Entry entry = mit.next();
                String kv = entry.getKey() + ": " + reflectionToString(entry.getValue());
                toStr.append(kv);
                toStr.append(" ");
            }
        } else if(o instanceof Object[]){
            for (Object temp: (Object[])o){
                toStr.append(reflectionToString(temp));
            }
        }else if(o instanceof String){
            toStr.append(o);
        } else {
            toStr.append(ToStringBuilder.reflectionToString(o, ToStringStyle.SHORT_PREFIX_STYLE));
        }
        return toStr.toString();
    }
}
