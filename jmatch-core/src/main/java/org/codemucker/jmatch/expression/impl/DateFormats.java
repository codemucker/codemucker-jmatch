package org.codemucker.jmatch.expression.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import org.codemucker.lang.annotation.ThreadSafe;

@ThreadSafe 
class DateFormats {

		private static final String[] DEFAULT_FORMATS = {
			"yyyy.MM.dd HH:mm:ss.SSS 'GMT'X", 
			"yyyy.MM.dd HH:mm:ss.SSS 'GMT'X", 
			"yyyy/MM/dd HH:mm:ss.SSS 'GMT'X", 
			
			"yyyy.MM.dd HH:mm:ss.SSS X", 
			"yyyy/MM/dd HH:mm:ss.SSS X", 
			"yyyy-MM-dd HH:mm:ss.SSS X", 
			
			"yyyy.MM.dd HH:mm:ss.SSS", 
			"yyyy/MM/dd HH:mm:ss.SSS", 
			"yyyy-MM-dd HH:mm:ss.SSS", 
			
			"yyyy.MM.dd HH:mm:ss", 
			"yyyy/MM/dd HH:mm:ss", 
			"yyyy-MM-dd HH:mm:ss",
			
			"yyyy.MM.dd", 
			"yyyy/MM/dd", 
			"yyyy-MM-dd",

			"HH:mm:ss.SSS", 
			"HH:mm:ss"
		};
		
		private final String[] formatStrings;
		private final ThreadLocal<DateFormat[]> localFormats = new ThreadLocal<>();
		
		public DateFormats(){
			this(DEFAULT_FORMATS);
		}
		
		public DateFormats(String[] formats){
			this.formatStrings = formats;
		}

		public Date parse(String s){
			//s = s.replace('-', '.').replace('/', '.');
System.out.println("parse dt:" + s);
			for(DateFormat df:getOrCreateFormats()){
				 try {
					return df.parse(s);
				} catch (ParseException e) {
					//ignore,try a different format
				}
			}
			
			return null;
		}
		
		private DateFormat[] getOrCreateFormats(){
			DateFormat[] formats = localFormats.get();
			if(formats == null){
				formats = createFormats();
				localFormats.set(formats);
			}
			return formats;
		}
		
		private DateFormat[] createFormats(){
			DateFormat[] formats = new DateFormat[formatStrings.length];
			for(int i = 0; i < formatStrings.length;i++){
				formats[i] = newDateFormat(formatStrings[i]);
			}
			return formats;
		}
		
		private static DateFormat newDateFormat(String format){
			SimpleDateFormat df = new SimpleDateFormat(format);
			df.setTimeZone(TimeZone.getTimeZone("GMT"));
			//df.setLenient(false);
			return df;
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName() + "[formats=" + Arrays.toString(formatStrings)+ "]";
		}
		
		
		
	}