package qdh.utility;

import java.sql.Timestamp;
import java.util.Date;

public class DateUtility {
	public static Timestamp getToday() {
		Timestamp today = new Timestamp(new Date().getTime());
		return today;
	}
}
