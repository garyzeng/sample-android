/**
 * 
 */
package cn.wsn.yunnan.library.nfc;

import android.nfc.Tag;

/**
 * @author Administrator
 * @param <T>
 *
 */
public interface INfcProvider{
	/**
	 * Register the filed you want to stored in the NFC card.
	 * @param item
	 * @throws NfcException
	 */
	//public void RegisterItem(INfcItem item) throws NfcException;
	/**
	 * Initialize the provider.
	 * @throws NfcException 
	 */
	public void Initialize() throws NfcException;
	/**
	 * Check the nfc card is availble.
	 * @param nfcvTag
	 * @return Is online
	 * @throws NfcException 
	 */
	public boolean isOnline(Tag tag) throws NfcException;
	/**
	 * Get stored item in the NFC card. The key must exists in the NFC.k
	 * @param nfcvTag 
	 * @param key
	 * @return
	 * @throws NfcException
	 */
	public <T> T getItem(Tag tag, String key) throws NfcException;
	/**
	 * Write the information to the NFC card.
	 * @param nfcvTag nfvTag
	 * @param key key
	 * @param t the instance of the type T
	 * @throws Exception 
	 */
	public void setItem(Tag tag, String key, Object data) throws NfcException;
	/**
	 * Clear the item.
	 * @param nfcvTag nfvTag
	 * @param key key
	 * @throws NfcException
	 */
	public void clearItem(Tag tag, String key) throws NfcException;
	/**
	 * Append string to the string item
	 * @param nfcvTag
	 * @param key
	 * @param data
	 * @throws NfcException
	 */
	public void appendStringItem(Tag tag, String key, String data) throws NfcException;
	/**
	 * Get multiple stored item sin the NFC card. The key must exists in the NFC.
	 * @param nfcvTag
	 * @param key
	 * @return
	 */
	public Object[] getItems(Tag tag, String[] key) throws NfcException;
}
