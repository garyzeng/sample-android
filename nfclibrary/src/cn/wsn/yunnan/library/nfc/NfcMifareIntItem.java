package cn.wsn.yunnan.library.nfc;

import java.io.IOException;

import android.nfc.Tag;
import android.nfc.tech.MifareClassic;

public class NfcMifareIntItem extends NfcMifareItemBase {
	public NfcMifareIntItem(String key) {
		super(key);
	}
	@Override
	protected Integer buildValue(byte[] value) {
		Integer intValue;

		boolean isEmpty = true;
		for (byte b : value) {
			if (b != EmptyByte) {
				isEmpty = false;
				break;
			}
		}
		if (isEmpty) {
			intValue = -1;
		} else {
			intValue = NfcConvertHelper.byteArrayToInt(value);
		}

		return intValue;
	}
	
	@Override
	public byte[] getValueByte(Object t) throws NfcException {
		try {
			int value = (Integer) t;
			return NfcConvertHelper.intToByteArray(value);
		} catch (Exception e) {
			throw new NfcException(NfcErrorCode.TypeCastFailed,
					"Object cannot cast to the Int.");
		}
	}
	
	@Override
	public Integer getValue(Tag tag) throws NfcException {
		MifareClassic mc = MifareClassic.get(tag);
		short startAddress = getStartAddress();

		byte[] response = null;

		try {
			mc.connect();
			boolean auth = false;

			int sectorAddress = getSectorAddress(startAddress);
			
			auth = mc.authenticateSectorWithKeyA(sectorAddress, MifareClassic.KEY_DEFAULT);
			if (auth) {
				//the last block of the sector is used for KeyA and KeyB cannot be overwritted
				short readAddress = (short)(sectorAddress == 0 ? startAddress : startAddress + sectorAddress);
				
				response = mc.readBlock(readAddress);

				if (response != null) {		
					mc.close();
					return buildValue(response);
				} else {
					throw new NfcException(NfcErrorCode.TemporaryError,
							"Get value error.");
				}
			} else {
				throw new NfcException(NfcErrorCode.TemporaryError,
						"Authorization Error.");
			}
		} 
		catch (NfcException ne) {
			throw ne;
		}
		catch (Exception e) {
			throw new NfcException(NfcErrorCode.TemporaryError,
					"Get value error.", e);
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
	@Override
	public int getByteCount() {
		return 4;
	}
	@Override
	public String getType() {
		return "Integer";
	}
}
