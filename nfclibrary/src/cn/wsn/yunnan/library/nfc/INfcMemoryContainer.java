package cn.wsn.yunnan.library.nfc;

public interface INfcMemoryContainer {
	public short[] AllocateItem(int byteCount) throws NfcException;
}
