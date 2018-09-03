package org.windwant.spring.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LangUtil {

	@Autowired
	private MessageSource messageSource;
	
	Locale locale = LocaleContextHolder.getLocale();
	
	/**
	 * 获取对应语言的内容
	 * @param key
	 * @return
	 */
	public String getMsg(String key) {
		String msg = null;
		try {
			msg = messageSource.getMessage(key, null, locale);
		}catch (Exception e){
		}
		if (msg == null)
			return "";
		return msg;
	}

	public String getValueReplaceWith(String key, String[] replaceValues) {
		String value = null;
		try {
			value = messageSource.getMessage(key, replaceValues, Locale.CHINA);
		} catch (Exception e) {
			return "";
		}
		return value;
	}
}
