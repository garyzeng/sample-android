/**
 * 
 */
package cn.wsn.yunnan.library.nfc;

import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.nfc.Tag;
import android.nfc.tech.NfcV;

/**
 * @author Administrator
 * 
 */
public class NfcVStringItem extends NfcVItemBase {
	public NfcVStringItem(String key) {
		super(key);
	}

	public NfcVStringItem(String key, short byteCount) {
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
		NfcV nfcvTag = NfcV.get(tag);
		short startAddress = getStartAddress();
		short endAddress = getEndAddress();
		short nearestClusterEndAddress = getNearestClusterEndAddress(startAddress);
		short firstBlockCountToGet = (short) (nearestClusterEndAddress - startAddress + 1);
		
		byte[] data = new byte[(endAddress - startAddress + 1 ) * ByteCountPerBlock];
		byte[] readSigleBlockCommand = null;
		short tempAddress = startAddress;
		short blockCount = (short) (endAddress - startAddress + 1);

		if (blockCount <= firstBlockCountToGet) {
			byte[] response = null;
			byte[] addressByte = NfcConvertHelper.shortToByteArray(tempAddress);
			readSigleBlockCommand = new byte[] { (byte) 0x0A, (byte) 0x23,
					addressByte[0], addressByte[1],
					(byte) (endAddress - tempAddress) };
			try {
				nfcvTag.close();
				nfcvTag.connect();
				response = nfcvTag.transceive(readSigleBlockCommand);
				nfcvTag.close();
			} catch (IOException e) {
				throw new NfcException(NfcErrorCode.TemporaryError,
						"Get value error.", e);
			}

			if ((response != null) && (response[0] == 0x00)) {
				int len = response.length - 1;
				byte[] realdata = new byte[len];
				for (int m = 0; m < len; m++) {
					realdata[m] = response[m + 1];
				}
				CombineByteArray(data, realdata, 0);
			} else {
				throw new NfcException(NfcErrorCode.TemporaryError,
						"Get response, what it is not successfully.");
			}
		} else {
			int firstCopiedCount = 0;
			byte[] response1 = null;
			byte[] addressByte1 = NfcConvertHelper.shortToByteArray(tempAddress);
			readSigleBlockCommand = new byte[] { (byte) 0x0A, (byte) 0x23,
					addressByte1[0], addressByte1[1],
					(byte) (nearestClusterEndAddress - tempAddress) };
			try {
				nfcvTag.close();
				nfcvTag.connect();
				response1 = nfcvTag.transceive(readSigleBlockCommand);
				nfcvTag.close();
			} catch (IOException e) {
				throw new NfcException(NfcErrorCode.TemporaryError,
						"Get value error.", e);
			}

			if ((response1 != null) && (response1[0] == 0x00)) {
				int len = response1.length - 1;
				byte[] realdata = new byte[len];
				for (int m = 0; m < len; m++) {
					realdata[m] = response1[m + 1];
				}
				CombineByteArray(data, realdata, 0);
				firstCopiedCount = realdata.length;
			} else {
				throw new NfcException(NfcErrorCode.TemporaryError,
						"Get response, what it is not successfully.");
			}
			
			tempAddress = (short) (nearestClusterEndAddress + 1);
			blockCount = (short) (endAddress - tempAddress + 1);
			
			int times = blockCount % BlockCountPerCluster == 0 ? blockCount
					/ BlockCountPerCluster : blockCount / BlockCountPerCluster
					+ 1;

			for (int i = 0; i < times; i++) {
				byte[] response = null;
				byte[] addressByte = NfcConvertHelper
						.shortToByteArray(tempAddress);

				if (i == times - 1) // last loop;
				{
					readSigleBlockCommand = new byte[] { (byte) 0x0A,
							(byte) 0x23, addressByte[0], addressByte[1],
							(byte) (endAddress - tempAddress) };
				} else {
					readSigleBlockCommand = new byte[] { (byte) 0x0A,
							(byte) 0x23, addressByte[0], addressByte[1],
							(byte) (BlockCountPerCluster - 1) };
				}
				try {
					nfcvTag.close();
					nfcvTag.connect();
					response = nfcvTag.transceive(readSigleBlockCommand);
					nfcvTag.close();
				} catch (IOException e) {
					throw new NfcException(NfcErrorCode.TemporaryError,
							"Get value error.", e);
				}

				tempAddress = (short) (tempAddress + BlockCountPerCluster);

				if ((response != null) && (response[0] == 0x00)) {
					int len = response.length - 1;
					byte[] realdata = new byte[len];
					for (int m = 0; m < len; m++) {
						realdata[m] = response[m + 1];
					}
					CombineByteArray(data, realdata, firstCopiedCount + i * ByteCountPerCluster);
				} else {
					throw new NfcException(NfcErrorCode.TemporaryError,
							"Get response, what it is not successfully.");
				}
			}
		}

		return buildValue(data);
	}

	/**
	 * Copy the data from @param data2 to @param data1.
	 * 
	 * @param data1
	 *            target data
	 * @param data2
	 *            source data
	 * @param startIndex
	 *            from which index to copy
	 */
	private void CombineByteArray(byte[] data1, byte[] data2, int startIndex) {
		for (int i = 0; i < data2.length; i++) {
			data1[startIndex + i] = data2[i];
		}
	}
	/**
	 * Get nearest cluster endaddress
	 * @param startAddress
	 * @return
	 */
	private short getNearestClusterEndAddress(short startAddress)
	{
		int clusterIndex =  ((startAddress + 1) / BlockCountPerCluster);
		return (short) (BlockCountPerCluster * (clusterIndex + 1) - 1);
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
