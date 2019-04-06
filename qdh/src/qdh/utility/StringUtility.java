package qdh.utility;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class StringUtility {
	public static HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat();  
	/**
	 * function to get the pinyin code's first combination
	 * etc: 我是中国人 -> wszgr
	 *      我是:中国人 -> wszgr
	 *      我是:chinese -> wschinese
	 * @param src
	 * @return
	 */
	public static String getPinyinCode(String src, boolean isCapital){
		StringBuffer pinyinCodes = new StringBuffer("");
		
		if (src != null) {
			char[] charArray = src.toCharArray();
			for (char charc: charArray){
				if(String.valueOf(charc).matches("[\\u4E00-\\u9FA5]+")){  
					try {
						String[] pinyinStrings = PinyinHelper.toHanyuPinyinStringArray(charc, hanYuPinOutputFormat);
						char[] pinyinCode = pinyinStrings[0].toCharArray();
						pinyinCodes.append(pinyinCode[0]);
						
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						loggerLocal.error("Error to get the Pinyin for : " + src + " , " + charc);
					}
				} else if(((int)charc>=65 && (int)charc<=90) || ((int)charc>=97 && (int)charc<=122) || ((int)charc>=48 && (int)charc<=57)){  
					pinyinCodes.append(charc);
				}
			}
		}
		
		if (isCapital)
		    return pinyinCodes.toString().toUpperCase();
		else 
			return pinyinCodes.toString();
		
	}
	
	
	public static String getRandomPassword(){

		int ranomInt = (int)(Math.random()*9000)+1000;
		
		return String.valueOf(ranomInt);
	}


	public static String decode(String var){
		String var_decode = "";
		try {
			var_decode =  java.net.URLDecoder.decode(var,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			var_decode = var;
			loggerLocal.error(e);
		}
		return var_decode;
	}
}
