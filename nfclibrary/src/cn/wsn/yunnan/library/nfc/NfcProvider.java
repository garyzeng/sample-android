/**
 * 
 */
package cn.wsn.yunnan.library.nfc;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Context;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcV;
import android.nfc.tech.TagTechnology;

/**
 * @author haoxinyue 20110729
 * 
 */
public class NfcProvider implements INfcProvider
{
	private Hashtable<String, INfcItem> nfcvAddress = new Hashtable<String, INfcItem>();
	private Hashtable<String, INfcItem> mifareAddress = new Hashtable<String, INfcItem>();
	
	private INfcMemoryContainer nfcvMemoryContainer = new NfcVMemoryContainer();
	private INfcMemoryContainer mifareMemoryContainer = new NfcMifareMemoryContainer();
	
	private Context context;
	private boolean initialized = false;
	
	public NfcProvider(Context context)
	{
		this.context = context;
	}
	
	public void Initialize() throws NfcException
	{
		if (!initialized)
		{
			RegisterNfcVItems();
			RegisterMifareVItems();
			
			initialized = true;
		}
	}
	
	private void RegisterNfcVItems() throws NfcException
	{
		List<INfcItem> items = getMemoryConfiguration(new NfcVMemoryConfigurationHandler(), "nfcvitems.xml");
		for (INfcItem item : items) {
			RegisterItem(item, nfcvAddress, nfcvMemoryContainer);
		}
	}
	
	private void RegisterMifareVItems() throws NfcException
	{
		List<INfcItem> items = getMemoryConfiguration(new NfcMifareMemoryConfigurationHandler(), "nfcmifareitems.xml");
		for (INfcItem item : items) {
			RegisterItem(item, mifareAddress, mifareMemoryContainer);
		}
	}
	
	private List<INfcItem> getMemoryConfiguration(NfcMemoryConfigurationHandler xmlHandler, String url) {

		List<INfcItem> itemList = null;

		try {
			InputStream iStream = context.getAssets().open(url);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			XMLReader reader = factory.newSAXParser().getXMLReader();
			reader.setContentHandler(xmlHandler);
			// 开始解析文件
			reader.parse(new InputSource(iStream));
			iStream.close();
			itemList = xmlHandler .getItemList();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return itemList;
	}
	
	public void RegisterItem(INfcItem item, Hashtable<String, INfcItem> ht, INfcMemoryContainer container) throws NfcException
	{
		try {
			String key = item.getKey();
			if (ht.containsKey(key))
			{
				throw new NfcException(NfcErrorCode.DuplicateKey, "The key is duplicated");
			}
			
			int byteCount = item.getByteCount();
			
			short[] address = container.AllocateItem(byteCount);
			
			item.setStartAddress(address[0]);
			item.setEndAddress(address[1]);
			
			ht.put(key, item);
			
		} catch (Exception e) {
			throw new NfcException(NfcErrorCode.Fatal, e.getMessage(), e);
		}
	}
	/**
	 * Get stored item in the NFC card. The key must exists in the NFC.
	 * @param nfcvTag 
	 * @param key
	 * @return
	 * @throws NfcException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getItem(Tag tag, String key) throws NfcException
	{
		if (!initialized)
		{
			throw new NfcException(NfcErrorCode.Fatal, "Not Initialized.");
		}
		try {
			INfcItem item = getItemByTagType(tag, key);
			if (item == null)
			{
				throw new NfcException(NfcErrorCode.KeyNotExists, "Please register the item first.");
			}
			return (T)item.getValue(tag);
		} 
		catch (NfcException ne) {
			throw ne;
		}		
		catch (Exception e) {
			throw new NfcException(NfcErrorCode.Fatal, "Get item error." ,e);
		}
	}
	/**
	 * Write the information to the NFC card.
	 * @param nfcvTag nfvTag
	 * @param key key
	 * @param t the instance of the type T
	 * @throws Exception 
	 */
	@Override
	public void setItem(Tag tag, String key, Object data) throws NfcException
	{
		if (!initialized)
		{
			throw new NfcException(NfcErrorCode.Fatal, "Not Initialized.");
		}
		try {
			INfcItem item = getItemByTagType(tag, key);
			if (item == null)
			{
				throw new NfcException(NfcErrorCode.KeyNotExists, "Please register the item first.");
			}
			byte[] byteArray = item.getValueByte(data);
			item.setValue(tag, byteArray);
		
		} catch (NfcException e) {
			throw e;
		}
	}
	/**
	 * Check the nfc card is availble.
	 * @param nfcvTag
	 * @return Is online
	 * @throws NfcException 
	 */
	@Override
	public boolean isOnline(Tag tag) throws NfcException {
		if (!initialized)
		{
			throw new NfcException(NfcErrorCode.Fatal, "Not Initialized.");
		}
		try 
		{
			TagTechnology tt = getTagTechnologyByTag(tag);
			tt.connect();
			boolean isConnected = tt.isConnected();
			tt.close();
			return isConnected;
		}
		catch (Exception e) {
			return false;
		}
	}

	@Override
	public void clearItem(Tag tag, String key) throws NfcException {
		if (!initialized)
		{
			throw new NfcException(NfcErrorCode.Fatal, "Not Initialized.");
		}
		try {
			INfcItem item = getItemByTagType(tag, key);
			if (item == null)
			{
				throw new NfcException(NfcErrorCode.KeyNotExists, "Please register the item first.");
			}
			item.setEmpty(tag);
		
		} catch (NfcException e) {
			throw e;
		}
	}
	/**
	 * Append string to the string item
	 * @param nfcvTag
	 * @param key
	 * @param data
	 * @throws NfcException
	 */
	@Override
	public void appendStringItem(Tag tag, String key, String data)
			throws NfcException {
		if (!initialized)
		{
			throw new NfcException(NfcErrorCode.Fatal, "Not Initialized.");
		}
		
		INfcItem item = getItemByTagType(tag, key);
		if (item == null)
		{
			throw new NfcException(NfcErrorCode.KeyNotExists, "Please register the item first.");
		}
		if (item.getType() != "String")
		{
			throw new NfcException(NfcErrorCode.TypeCastFailed, "The item with the key is not a String item");
		}
		
		String originalString = this.<String>getItem(tag, key);
		String newDataString = originalString.concat(data);
		this.setItem(tag, key, newDataString);
	}
	/**
	 * Get multiple stored item sin the NFC card. The key must exists in the NFC.
	 * @param nfcvTag
	 * @param key
	 * @return
	 */
	@Override
	public Object[] getItems(Tag tag, String[] key) throws NfcException {
		if (!initialized)
		{
			throw new NfcException(NfcErrorCode.Fatal, "Not Initialized.");
		}
		
		Object[] returnsObjects = new Object[key.length];
		
		for(int i=0; i < key.length ; i++)
		{
			returnsObjects[i] = getItem(tag, key[i]);
		}
		
		return returnsObjects;
	}
	
	private INfcItem getItemByTagType(Tag tag, String key)
	{
		INfcItem item = null;
		String[] techListStrings = tag.getTechList();
		for (String tech : techListStrings) {
			if (tech == android.nfc.tech.MifareClassic.class.getName())
			{
				item =  mifareAddress.get(key);
				break;
			}
			else if (tech == android.nfc.tech.NfcV.class.getName())
			{
				item = nfcvAddress.get(key);
				break;
			}
		}
		
		return item;
	}
	
	private TagTechnology getTagTechnologyByTag(Tag tag)
	{		
		String[] techListStrings = tag.getTechList();
		for (String tech : techListStrings) {
			if (tech == android.nfc.tech.MifareClassic.class.getName())
			{
				return MifareClassic.get(tag);
			}
			else if (tech == android.nfc.tech.NfcV.class.getName())
			{
				return NfcV.get(tag);
			}
		}
		return null;
	}
}

