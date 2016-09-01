/**
 * 
 */
package cn.wsn.yunnan.library.nfc;

import java.io.IOException;

import android.nfc.Tag;
import android.nfc.tech.MifareClassic;

/**
 * @author haoxinyue 20110729
 * 
 */
public abstract class NfcMifareItemBase implements INfcItem {
	public NfcMifareItemBase(String key) {
		_key = key;
	}

	protected String _key = "";
	protected short _startAddress;
	protected short _endAddress;

	protected final short ByteCountPerBlock = 16;
	protected final short BlockCountPerSector = 4;
	protected final short ByteCountPerCluster = ByteCountPerBlock
			* BlockCountPerSector;
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
		MifareClassic mc = MifareClassic.get(tag);

		int maxByteCount = getByteCount();

		if (data.length > maxByteCount) {
			throw new NfcException(NfcErrorCode.OverMaxLength,
					"The length is over the max length of this type of item.");
		}

		short startAddress = getStartAddress();
		//short endAddress = getEndAddress();

		short realEndAddress = (short) (startAddress
				+ (data.length % ByteCountPerBlock == 0 ? data.length
						/ ByteCountPerBlock : data.length / ByteCountPerBlock
						+ 1) - 1);
		int valueLength = data.length;

		int loopTimes = 0;
		/**
		 * the API is only for the single block write, and one block only has 16
		 * bytes, so the value big than 16, we need to write several times.
		 */
		for (short address = startAddress; address <= realEndAddress; address++) {

			int lenLeft = valueLength - loopTimes * ByteCountPerBlock;

			/**
			 * The API must provider 16 bytes for each command, so if not
			 * enough, fill it.
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

			try {
				mc.connect();
				boolean auth = false;
				short sectorAddress = getSectorAddress(address);

				auth = mc.authenticateSectorWithKeyA(sectorAddress,
						MifareClassic.KEY_DEFAULT);
				if (auth) {
					//the last block of the sector is used for KeyA and KeyB cannot be overwritted
					short readAddress = (short)(sectorAddress == 0 ? address : address + sectorAddress);
					mc.writeBlock(readAddress, dataTemp);

					mc.close();
				} else {
					throw new NfcException(NfcErrorCode.TemporaryError,
							"Authorization Error");
				}
			}
			catch (NfcException ne) {
				throw ne;
			}
			catch (Exception e) {
				throw new NfcException(NfcErrorCode.TemporaryError,
						"Set value error.", e);
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
			loopTimes++;
		}
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
	protected void CombineByteArray(byte[] data1, byte[] data2, int startIndex) {
		for (int i = 0; i < data2.length; i++) {
			data1[startIndex + i] = data2[i];
		}
	}

	protected short getSectorAddress(short blockAddress) {
		return (short) (blockAddress / BlockCountPerSector);
	}

	public void setEmpty(Tag tag) throws NfcException {
		MifareClassic mc = MifareClassic.get(tag);
		short startAddress = getStartAddress();
		short endAddress = getEndAddress();

		byte[] data = new byte[ByteCountPerBlock];

		for (byte b : data) {
			b = EmptyByte;
		}

		try {
			mc.connect();
			int time = 0;
			for (short address = startAddress; address <= endAddress; address++, time++) {
				boolean auth = false;
				short sectorAddress = getSectorAddress(address);

				auth = mc.authenticateSectorWithKeyA(sectorAddress,
						MifareClassic.KEY_DEFAULT);
				if (auth) {
					//the last block of the sector is used for KeyA and KeyB cannot be overwritted
					short readAddress = (short)(sectorAddress == 0 ? address : address + sectorAddress);
					mc.writeBlock(readAddress, data);
				} else {
					throw new NfcException(NfcErrorCode.TemporaryError,
							"Authorization Error.");
				}
			}
			mc.close();
		}
		catch (NfcException ne) {
			throw ne;
		}
		catch (Exception e) {
			throw new NfcException(NfcErrorCode.TemporaryError,
					"Set value error.", e);
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
	}
}
