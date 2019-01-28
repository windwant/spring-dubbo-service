package org.windwant.spring.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigUtil {

    private static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);

    private static PropertiesConfiguration config = null;

    static {
        try {
            if (config == null) {
                config = new PropertiesConfiguration("config.properties");
                //自动重新加载
                config.setReloadingStrategy(new FileChangedReloadingStrategy());
                //自动保存
                config.setAutoSave(true);
            }
        } catch (ConfigurationException e) {
            logger.error("load config.properties error", e);
            throw new RuntimeException(e);
        }
    }

    private ConfigUtil(){}

    public static String get(String key) {
        if (config != null) {
            String value = config.getString(key);
            return value == null ? "" : value;
        }
        return "";
    }

    public static Integer getInteger(String key) {
    	int result = -1;
        if (config != null) {
        	try {
        		result = Integer.parseInt(config.getString(key));
        	} catch (NumberFormatException e) {
        		result = -1;
        	}
        }
        return result;
    }

    public static List getList(String key) {
        if (config != null) {
            String result = config.getString(key);
            if (result == null || result.isEmpty()) {
                return new ArrayList<>();
            }
            String[] values = result.split("|");
            return Arrays.asList(values);
        }
        return new ArrayList<>();
    }
    
    public static boolean isInList(String listName, String key) {
		  if (config != null) {
	          String result = config.getString(listName);
	          if (result == null || result.isEmpty()) {
	        	  return false;
	          }
	          String[] values = result.split("_");
	          for (int i=0; i<values.length; i++) {
	        	  if (values[i].equals(key)) {
	        		  return true;
	        	  }
	          }
	      }
		  return false;
    }
}
