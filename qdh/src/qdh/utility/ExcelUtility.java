package qdh.utility;


public class ExcelUtility {
    public static String encodeExcelDownloadName(String fileName, String defaultName) {  
		try {
			fileName = java.net.URLEncoder.encode(fileName,"UTF-8");
		} catch (Exception e){
			fileName = defaultName;
			e.printStackTrace();
			loggerLocal.error(e);
		}
		return fileName;
    } 
}
