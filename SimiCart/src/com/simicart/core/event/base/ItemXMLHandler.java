package com.simicart.core.event.base;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ItemXMLHandler extends DefaultHandler {

	Boolean currentElement = false;
	String currentValue = "";
	ItemMaster item = null;
	String tags = "";

	public void setTags(String tags) {
		this.tags = tags;
	}

	public ItemXMLHandler(String tags, ArrayList<ItemMaster> itemsList) {
		this.tags = tags;
		this.itemsList = itemsList;
	}

	private ArrayList<ItemMaster> itemsList;

	// public ArrayList<ItemMaster> getItemsList() {
	// return itemsList;
	// }

	// Called when tag starts
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElement = true;
		currentValue = "";
		if (localName.equals(this.tags)) {
			item = new ItemMaster();
		}

	}

	// Called when tag closing
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		currentElement = false;
		/** set value */
		if (localName.equalsIgnoreCase("name"))
			item.setName(currentValue);
		else if (localName.equalsIgnoreCase("package"))
			item.setPackageName(currentValue);
		else if (localName.equalsIgnoreCase("class"))
			item.setClassName(currentValue);
		else if (localName.equalsIgnoreCase("method"))
			item.setMethod(currentValue);
		else if (localName.equalsIgnoreCase("sku"))
			item.setSku(currentValue);
		else if (localName.equalsIgnoreCase("order")) {
			item.setOrder(currentValue);
		} else if (localName.equalsIgnoreCase(this.tags))
			itemsList.add(item);

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