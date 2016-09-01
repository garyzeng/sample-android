package cn.wsn.yunnan.library.nfc;

import android.nfc.Tag;

/**
 * @author haoxinyue 20110729
 * The interface if the item in the NFC card.
 */
public interface INfcItem{
	/**
	 * Get the key of the item, the key is the identification of the item.
	 * @return String key
	 */
	public String getKey();
	/**
	 * get the type of the item.
	 * @return type
	 */
	public String getType();
	/**
	 * Set the start address of the NFC card.
	 * @param startAddress
	 */
	public void setStartAddress(short startAddress);
	/**
	 * Set the end address of the NFC card.
	 * @param endAddress
	 */
	public void setEndAddress(short endAddress);
	/**
	 * Convert the specific data to byte array.
	 * @param t
	 * @return
	 * @throws NfcException 
	 */
	public byte[] getValueByte(Object t) throws NfcException;
	/**
	 * Get the value in the NFC card.
	 * @param nfcvTag
	 * @return Generic Object
	 * @throws NfcException
	 */
	public Object getValue(Tag tag) throws NfcException;
	/**
	 * Set the value to the NFC card.
	 * @param nfcvTag
	 * @param data
	 * @throws NfcException
	 */
	public void setValue(Tag tag, byte[] data) throws NfcException;
	/**
	 * Get the byte count of the item will be stored in the NFC card. 
	 * @return short the byte count of the item will be stored in the NFC card.
	 */
	public int getByteCount();
	/**
	 * Clear the data of the item
	 * @param nfcvTag
	 * @throws NfcException
	 */
	public void setEmpty(Tag tag) throws NfcException;
}
