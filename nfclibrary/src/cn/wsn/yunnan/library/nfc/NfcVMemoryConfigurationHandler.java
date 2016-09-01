/**
 * 
 */
package cn.wsn.yunnan.library.nfc;


/**
 * @author haoxinyue Handle XML configuration file
 */
public class NfcVMemoryConfigurationHandler extends
		NfcMemoryConfigurationHandler {
	@Override
	protected INfcItem CreateIntItem(String key) {
		return new NfcVIntItem(key);
	}

	@Override
	protected INfcItem CreateStringItem(String key, short byteCount) {
		return new NfcVStringItem(key, byteCount);
	}
}
