package qdh.sysParms;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import qdh.utility.loggerLocal;


public class DefaultFunction {
	public static final String SPLITER = ",";

	private static final List<String> DEFAULT_FUNCT_LIST = new ArrayList<String>();
	
	private static Properties defaultFunctionProperties = new Properties();
	
	public static void load() {
		try {
			InputStream is = DefaultFunction.class.getClassLoader()
					 .getResourceAsStream("qdh/sysParms/default-functions.properties");
			
			defaultFunctionProperties.load(is);
			
			Iterator<Object> functionKeys = defaultFunctionProperties.keySet().iterator();
			
			while (functionKeys.hasNext()){
				String functionKey = (String)functionKeys.next();
				String[] functions = ((String)defaultFunctionProperties.get(functionKey)).split(SPLITER);
				for (String function: functions){
				    if (!function.trim().equals(""))
					    DEFAULT_FUNCT_LIST.add(function);
				}
			}
			
		} catch (IOException e) {
			loggerLocal.error(e);
		}
	}
	
	/**
	 * to get the function mapping by one mapping id
	 * @param mappingId
	 * @return
	 */
	public static boolean isDefaultFunction(String function){
		return DEFAULT_FUNCT_LIST.contains(function);
	}


}
