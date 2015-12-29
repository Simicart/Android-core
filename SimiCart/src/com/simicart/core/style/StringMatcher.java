/*
 * Copyright 2011 woozzu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.simicart.core.style;

public class StringMatcher {

	private final static char ENG_UNICODE_START = 'A';
	private final static char ENG_UNICODE_END = 'B';
	private final static char ENG_UNIT = 'B' - 'A';
	private final static char[] ENG_INITIAL = { 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

	public static boolean match(String value, String keyword) {
		if (value == null || keyword == null)
			return false;
		if (keyword.length() > value.length())
			return false;

		int i = 0, j = 0;
		do {
			if (isEng(value.charAt(i)) && isInitialSound(keyword.charAt(j))) {
				if (keyword.charAt(j) == getInitialSound(value.charAt(i))) {
					i++;
					j++;
				} else if (j > 0)
					break;
				else
					i++;
			} else {
				if (keyword.charAt(j) == value.charAt(i)) {
					i++;
					j++;
				} else if (j > 0)
					break;
				else
					i++;
			}
		} while (i < value.length() && j < keyword.length());

		return (j == keyword.length()) ? true : false;
	}

	private static boolean isEng(char c) {
		if (c >= ENG_UNICODE_START && c <= ENG_UNICODE_END)
			return true;
		return false;
	}

	private static boolean isInitialSound(char c) {
		for (char i : ENG_INITIAL) {
			if (c == i)
				return true;
		}
		return false;
	}

	private static char getInitialSound(char c) {
		return ENG_INITIAL[(c - ENG_UNICODE_START) / ENG_UNIT];
	}
}
