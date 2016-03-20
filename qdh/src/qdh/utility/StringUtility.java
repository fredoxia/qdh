package qdh.utility;

import java.util.Random;

public class StringUtility {
	public static String getRandomPassword(){

		int ranomInt = (int)(Math.random()*9000)+1000;
		
		return String.valueOf(ranomInt);
	}
}
