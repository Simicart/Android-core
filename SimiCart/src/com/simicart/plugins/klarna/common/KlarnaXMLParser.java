package com.simicart.plugins.klarna.common;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class KlarnaXMLParser extends DefaultHandler {
	protected Boolean currentElement = false;
	protected String currentValue = "";
	protected String result = "";

	public String getResult() {
		return result;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentValue = "";

		if (localName.equals("body")) {
			currentElement = true;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (currentElement) {
			currentValue = currentValue + new String(ch, start, length);
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		currentElement = false;
		if (localName.equals("body")) {
			result = currentValue;
		}
	}

}
