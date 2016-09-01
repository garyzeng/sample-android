package cn.wsn.yunnan.library.nfc;

import android.nfc.Tag;
import android.nfc.tech.NfcV;

public class NfcVIntItem extends NfcVItemBase {
	public NfcVIntItem(String key) {
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
		NfcV nfcvTag = NfcV.get(tag);
		short startAddress = getStartAddress();
		byte[] address = NfcConvertHelper.shortToByteArray(startAddress);

		byte[] readSigleBlockCommand = null;
		byte[] response = null;

		readSigleBlockCommand = new byte[] { (byte) 0x0A, (byte) 0x20,
				address[0], address[1] };
		try {
			nfcvTag.close();
			nfcvTag.connect();
			response = nfcvTag.transceive(readSigleBlockCommand);
			nfcvTag.close();
		} catch (Exception e) {
			throw new NfcException(NfcErrorCode.TemporaryError,
					"Get value error.", e);
		}

		if ((response != null) && (response[0] == 0x00)) {
			int len = response.length - 1;
			byte[] data = new byte[len];
			for (int m = 0; m < len; m++) {
				data[m] = response[m + 1];
			}

			return buildValue(data);
		} else {
			throw new NfcException(NfcErrorCode.TemporaryError,
					"Get response, what it is not successfully.");
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
