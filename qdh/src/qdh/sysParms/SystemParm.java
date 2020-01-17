package qdh.sysParms;

import java.io.InputStream;
import java.util.Properties;

public class SystemParm {
	private static final Properties parmPorperty = new Properties();
	
	public static void load() {
		try {
				InputStream is =
					QXMsgManager.class.getClassLoader()
				 .getResourceAsStream("qdh/sysParms/SystemParm.properties");
				if( is!= null) {
					parmPorperty.load(is);
				}
			} catch (Exception e) {
					e.printStackTrace();
			}
	}

	private static String getParm(String msgId){
		return parmPorperty.getProperty(msgId, "");
	}

	public static int getRecordPerPage(){
		String recordPerPageS = getParm("order.recordPerPage");
		
		if (recordPerPageS.equals(""))
			return 50;
		else 
			return Integer.parseInt(recordPerPageS);
	}
}
