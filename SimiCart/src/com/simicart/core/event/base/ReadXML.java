package com.simicart.core.event.base;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.res.AssetManager;

public class ReadXML {

	Context context;
	String tags = "event";
	public ReadXML(Context context) {
		this.context = context;
	}

	public void read() {
		String[] files = this.readXml();
		for (int i = 0; i < files.length; i++) {
			this.getItemMaster(files[i]);
		}
	}

	public String[] readXml() {
		AssetManager assetManager = context.getAssets();
		try {
			String[] files = assetManager.list("plugins");
			return files;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void getItemMaster(String filename) {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp;
		XMLReader xr;
		AssetManager assetManager = context.getAssets();		

		try {
			InputStream inputStream = assetManager.open("plugins/" + filename);
			InputSource inStream = new InputSource(inputStream);
			sp = spf.newSAXParser();
			xr = sp.getXMLReader();
			ItemXMLHandler myXMLHandler = new ItemXMLHandler(tags, UtilsEvent.itemsList);
			xr.setContentHandler(myXMLHandler);
			xr.parse(inStream);		
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
