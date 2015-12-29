package com.simicart.core.common;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.annotation.SuppressLint;

public class LanguageXMLHandler extends DefaultHandler {

	public static final String TAG = "language";
	public static final String TAG_ITEM = "item";

	private boolean currentElement = false;
	private String currentValue = "";
	private String key = "";
	private String value = "";

	Map<String, String> languages = new HashMap<String, String>();

	public LanguageXMLHandler() {
	}

	public Map<String, String> getLanguages() {
		return languages;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;

		if (localName.equals(TAG)) {
			// listLanguage = new ArrayList<>();
		} else if (localName.equals(TAG_ITEM)) {
			// item = new ItemLanguage();
		}
	}

	// Called when tag closing
	@SuppressLint("DefaultLocale")
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;
		/** set value */
		if (localName.equalsIgnoreCase("key")) {
			key = (String) currentValue.toLowerCase().trim();
		} else if (localName.equalsIgnoreCase("value")) {
			value = (String) currentValue.trim();
		} else if (localName.equalsIgnoreCase(TAG_ITEM)) {
			languages.put(key, value);
		}
		currentValue = "";
	}

	// Called to get tag characters
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (currentElement) {
			currentValue = currentValue + new String(ch, start, length);
		}
	}
}