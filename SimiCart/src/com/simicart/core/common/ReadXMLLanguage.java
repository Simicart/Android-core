package com.simicart.core.common;

import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.content.res.AssetManager;

public class ReadXMLLanguage {

	private Context mContext;
	private LanguageXMLHandler mHandler;

	public ReadXMLLanguage(Context context) {
		this.mContext = context;
	}

	public void parseXML(String filename) {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp;
		XMLReader xr;
		AssetManager assetManager = mContext.getAssets();
		try {
			InputStream inputStream = assetManager.open(filename);
			InputSource inStream = new InputSource(inputStream);
			sp = spf.newSAXParser();
			xr = sp.getXMLReader();
			mHandler = new LanguageXMLHandler();
			xr.setContentHandler(mHandler);
			xr.parse(inStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> getLanguages() {
		return mHandler.getLanguages();
	}
}
