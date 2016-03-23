package qdh.utility;

import java.util.List;

public class NumUtility {
	/**
	 * to get the projections' single value
	 * @param values
	 * @return
	 */
	public static int getProjectionIntegerValue(List<Object> values){
		if (values != null && values.size() >0 && values.get(0) != null){
			return Integer.valueOf(values.get(0).toString());
		} else 
			return 0;
	}

	/**
	 * to get the projections' sum value
	 * @param values
	 * @return
	 */
	public static double getProjectionDoubleValue(List<Object> values){
		if (values != null && values.size() >0 && values.get(0) != null){
			return Double.valueOf(values.get(0).toString());
		} else 
			return 0;
	}
}
