/**
 * 
 */
package cn.wsn.yunnan.library.nfc;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author haoxinyue Handle XML configuration file
 */
public abstract class NfcMemoryConfigurationHandler extends DefaultHandler {
	protected List<INfcItem> itemList = null;

	public void startDocument() throws SAXException {
		itemList = new ArrayList<INfcItem>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			org.xml.sax.Attributes attributes) throws SAXException {
		if (localName.equals("item")) {
			String key = attributes.getValue("key");
			String type = attributes.getValue("type");
			short byteCount = Short.parseShort(attributes.getValue("bytecount"));
 
			INfcItem item = null;
			if (type.equalsIgnoreCase("string")) {
				item = CreateStringItem(key, byteCount);
			}

			else if (type.equalsIgnoreCase("int")) {
				item = CreateIntItem(key);
			}
			itemList.add(item);
		}
	}

	protected List<INfcItem> getItemList() {
		return itemList;
	}
	
	protected abstract INfcItem CreateIntItem(String key);
	
	protected abstract INfcItem CreateStringItem(String key, short byteCount);
}
