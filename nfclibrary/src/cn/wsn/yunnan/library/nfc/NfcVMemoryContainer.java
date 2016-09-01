/**
 * 
 */
package cn.wsn.yunnan.library.nfc;

/**
 * @author Administrator
 * 
 */
public class NfcVMemoryContainer implements INfcMemoryContainer {
	/**
	 * The min Address in the NFC card.
	 */
	private short minAvailbleBlockAddress = 0x0;
	/**
	 * the max address in the NFC card.
	 */
	private short maxAvailbleBlockAddress = 0x7ff;
	/**
	 * 
	 */
	private short byteCountPerBlock = 4;

	/**
	 * @throws NfcException
	 * 
	 */
	@Override
	public short[] AllocateItem(int byteCount) throws NfcException {

		try {
			short blockCount = (short) (byteCount % byteCountPerBlock == 0 ? byteCount / byteCountPerBlock
					: byteCount / byteCountPerBlock + 1);
			
			if (minAvailbleBlockAddress + blockCount > maxAvailbleBlockAddress) {
				throw new NfcException(NfcErrorCode.OutofSpace, "Out of memory");
			}

			short startAddress = minAvailbleBlockAddress;
			short endAddress = (short) (minAvailbleBlockAddress + blockCount - 1);
			minAvailbleBlockAddress = (short) (endAddress + 1);

			return new short[] { startAddress, endAddress };

		} catch (Exception e) {
			throw new NfcException(NfcErrorCode.AllocateMemoryFailed,
					"Allocate memory failed", e);
		}

	}
}
