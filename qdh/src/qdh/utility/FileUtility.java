package qdh.utility;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.hp.hpl.sparta.xpath.ThisNodeTest;

public class FileUtility {
	
	private static final String PATH_SEP = "\\";
	public static final int BUFFER = 2048;
	private static final String rootPath = "C:\\zip\\"; 
	
	
	public static String zipWorkbooks(final Map<String, HSSFWorkbook> workbookMap) throws Exception {
		String zipFilePath = rootPath + getRandomFileName();
		
		final FileOutputStream dest = new FileOutputStream(zipFilePath);
		final ZipOutputStream out = new ZipOutputStream(
				new BufferedOutputStream(dest));
		try {
			Iterator<String> workbookNames = workbookMap.keySet().iterator();
			
			byte[] data = new byte[BUFFER];
			while (workbookNames.hasNext()){
				String workbookName = workbookNames.next();
				HSSFWorkbook wb = workbookMap.get(workbookName);
				
				ByteArrayOutputStream os = new ByteArrayOutputStream();   
				wb.write(os);   

			    byte[] content = os.toByteArray();   
			    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);   
			    
				out.putNextEntry(new ZipEntry(workbookName));
				int count;
				while ((count = byteArrayInputStream.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
			}
			out.setEncoding("UTF-8");
		} catch (Exception e){
			loggerLocal.error(e);
			throw e;
		} finally {
			out.close();
			dest.close();
		}
		
		return zipFilePath;
	}
	
	public static String getRandomFileName(){
		UUID uuid = UUID.randomUUID();
			return uuid.toString() + ".zip";
	}
}
