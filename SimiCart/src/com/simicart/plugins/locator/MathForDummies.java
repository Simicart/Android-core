package com.simicart.plugins.locator;

public class MathForDummies {
	public static double round(double number, int digit) {
		if (digit > 0) {
			int temp = 1, i;
			for (i = 0; i < digit; i++)
				temp = temp * 10;
			number = number * temp;
			number = Math.round(number);
			number = number / temp;
			return number;
		} else
			return 0.0;
	}
}
