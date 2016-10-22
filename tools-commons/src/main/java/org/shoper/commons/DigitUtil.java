package org.shoper.commons;

import sun.misc.FloatingDecimal;

/**
 * Digital util
 */
public class DigitUtil {
	public static boolean StrIsDigit (String digit)  {
		try {
			FloatingDecimal.parseDouble(digit);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
