/**
 * 
 */
package cn.wsn.yunnan.library.nfc;

import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.nfc.Tag;
import android.nfc.tech.MifareClassic;

/**
 * @author Administrator
 * 
 */
public class NfcMifareStringItem extends NfcMifareItemBase {
	public NfcMifareStringItem(String key) {
		super(key);
	}

	public NfcMifareStringItem(String key, short byteCount) {
		super(key);
		_byteCount = byteCount;
	}

	@Override
	protected String buildValue(byte[] value) {
		int index = 0;
		int len = value.length;

		boolean isEmpty = true;
		for (int i = 0; i < len; i++)
		{
			if (value[i] != 0)
			{
				isEmpty = false;
				break;
			}
		}
		
		if (isEmpty)
		{
			return "";
		}
		
		for (int i = 0; i < len; i++) {
			if (value[i] == seprator) {
				int count = 1;
				for (int m = 1; m < sepratorCount; m++) {
					if (value[i + m] == seprator) {
						count++;
					}
				}

				if (count == sepratorCount) {
					index = i;
					break;
				}
			}
		}
		if (index == 0) {
			return "";
		}

		byte[] realData = new byte[index];

		for (int i = 0; i < index; i++) {
			realData[i] = value[i];
		}

		String strValueString = EncodingUtils.getString(realData, encodingCode);
		return strValueString;
	}

	@Override
	public byte[] getValueByte(Object t) throws NfcException {
		try {
			String strValueString = (String) t;
			byte[] pureData = EncodingUtils.getBytes(strValueString, encodingCode);
			int realLen = pureData.length + sepratorCount;
			byte[] dataWithEndSign = new byte[realLen];
			for (int i = 0; i < realLen; i++) {
				if (i < realLen - sepratorCount) {
					dataWithEndSign[i] = pureData[i];
				} else {
					dataWithEndSign[i] = seprator;
				}
			}

			return dataWithEndSign;
		} catch (Exception e) {
			throw new NfcException(NfcErrorCode.TypeCastFailed,
					"Object cannot cast to the String.");
		}

	}

	@Override
	public String getValue(Tag tag) throws NfcException {
		MifareClassic mc = MifareClassic.get(tag);
		short startAddress = getStartAddress();
		short endAddress = getEndAddress();

		byte[] data = new byte[(endAddress - startAddress + 1 ) * ByteCountPerBlock];
		
		try {			
			mc.connect();
			int time = 0;
			for (short i = startAddress; i <= endAddress; i++ ,time++) {
				boolean auth = false;
				short sectorAddress = getSectorAddress(i);
				auth = mc.authenticateSectorWithKeyA(sectorAddress, MifareClassic.KEY_DEFAULT);
				if (auth){
					
					//the last block of the sector is used for KeyA and KeyB cannot be overwritted
					short readAddress = (short)(sectorAddress == 0 ? i : i + sectorAddress);
					
					byte[] response = mc.readBlock(readAddress);
					CombineByteArray(data, response, time * ByteCountPerBlock);
				}
				else{
					throw new NfcException(NfcErrorCode.TemporaryError,
							"Authorization Error.");
				}
			}

			mc.close();
			
		}
		catch (NfcException ne) {
			throw ne;
		}
		catch (IOException e) {
			throw new NfcException(NfcErrorCode.TemporaryError,
					"Get response, what it is not successfully.", e);
		}
		finally
		{
			try {
				mc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return buildValue(data);
	}

	/**
	 * The sign to flag the string is end.
	 */
	private byte seprator = (byte) 0xFF;
	/**
	 * The count of seprator to specify the string is end.
	 */
	private int sepratorCount = 2;
	/**
	 * defalut for string item, the length is 40.
	 */
	private short _byteCount = 62;

	private String encodingCode = "gbk";
	
	@Override
	public int getByteCount() {
		return _byteCount + sepratorCount;
	}

	@Override
	public String getType() {
		return "String";
	}
}
