/**
 * 
 */
package cn.wsn.yunnan.library.nfc;

import android.nfc.Tag;
import android.nfc.tech.NfcV;

/**
 * @author haoxinyue 20110729
 * 
 */
public abstract class NfcVItemBase implements INfcItem {
	public NfcVItemBase(String key) {
		_key = key;
	}
	
	protected String _key = "";
	protected short _startAddress;
	protected short _endAddress;

	protected final short ByteCountPerBlock = 4;
	protected final short BlockCountPerCluster = 32;
	protected final short ByteCountPerCluster = ByteCountPerBlock
			* BlockCountPerCluster;
	protected final byte EmptyByte = -1;

	public String getKey() {
		return _key;
	}

	public void setStartAddress(short start) {
		_startAddress = start;
	}

	public void setEndAddress(short end) {
		_endAddress = end;
	}

	public short getStartAddress() {
		return _startAddress;
	}

	public short getEndAddress() {
		return _endAddress;
	}

	protected abstract Object buildValue(byte[] data);

	public void setValue(Tag tag, byte[] data) throws NfcException {
		NfcV nfcvTag = NfcV.get(tag);
		
		int maxByteCount = getByteCount();

		if (data.length > maxByteCount) {
			throw new NfcException(NfcErrorCode.OverMaxLength,
					"The length is over the max length of this type of item.");
		}

		short startAddress = getStartAddress();
		short realEndAddress = (short) (startAddress
				+ (data.length % ByteCountPerBlock == 0 ? data.length
						/ ByteCountPerBlock : data.length / ByteCountPerBlock
						+ 1) - 1);
		int valueLength = data.length;

		int loopTimes = 0;
		/**
		 * the API is only for the single block write, and one block only has 4
		 * bytes, so the value big than 4, we need to write several times.
		 */
		for (short address = startAddress; address <= realEndAddress; address++) {

			byte[] addressByte = NfcConvertHelper.shortToByteArray(address);

			int lenLeft = valueLength - loopTimes * ByteCountPerBlock;

			/**
			 * The API must provider 4bytes for each command, so if not enough,
			 * fill it.
			 */
			byte[] dataTemp = new byte[ByteCountPerBlock];

			if (lenLeft >= ByteCountPerBlock) {
				for (int m = 0; m < ByteCountPerBlock; m++) {
					dataTemp[m] = data[loopTimes * ByteCountPerBlock + m];
				}
			} else {
				for (int m = 0; m < lenLeft; m++) {
					dataTemp[m] = data[loopTimes * ByteCountPerBlock + m];
				}
			}

			byte[] response = null;

			byte[] writeSingleBlockCommand = new byte[] { (byte) 0x0A,
					(byte) 0x21, addressByte[0], addressByte[1], dataTemp[0],
					dataTemp[1], dataTemp[2], dataTemp[3] };
			try {
				nfcvTag.close();
				nfcvTag.connect();
				response = nfcvTag.transceive(writeSingleBlockCommand);
				nfcvTag.close();
			} catch (Exception e) {
				throw new NfcException(NfcErrorCode.TemporaryError,
						"Set value error.", e);
			}
			// 0x00 is successful.
			if ((response != null) && (response[0] != 0x00)) {
				throw new NfcException(NfcErrorCode.TemporaryError,
						"Get response, what it is not successfully.");
			}

			loopTimes++;
		}
	}

	public void setEmpty(Tag tag) throws NfcException {
		NfcV nfcvTag = NfcV.get(tag);
		short startAddress = getStartAddress();
		short endAddress = getEndAddress();

		short tempStartAddress = startAddress;

		for (short address = startAddress; address <= endAddress; address++) {
			byte[] addressByte = NfcConvertHelper
					.shortToByteArray(tempStartAddress);

			byte[] response = null;

			byte[] writeSingleBlockCommand = new byte[] { (byte) 0x0A,
					(byte) 0x21, addressByte[0], addressByte[1], EmptyByte,
					EmptyByte, EmptyByte, EmptyByte };
			try {
				nfcvTag.close();
				nfcvTag.connect();
				response = nfcvTag.transceive(writeSingleBlockCommand);
				nfcvTag.close();
			} catch (Exception e) {
				throw new NfcException(NfcErrorCode.TemporaryError,
						"Set value error.", e);
			}
			// 0x00 is successful.
			if ((response != null) && (response[0] != 0x00)) {
				throw new NfcException(NfcErrorCode.TemporaryError,
						"Get response, what it is not successfully.");
			}
		}
	}
}
