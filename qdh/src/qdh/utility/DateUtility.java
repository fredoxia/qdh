package qdh.utility;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtility {
	public static  final SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd");
	public static  final SimpleDateFormat dateFormat_f =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static Timestamp getToday() {
		Timestamp today = new Timestamp(new Date().getTime());
		return today;
	}
}
