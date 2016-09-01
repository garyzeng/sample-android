/**
 * 
 */
package cn.wsn.yunnan.library.nfc;

/**
 * @author Administrator
 *
 */
public class NfcConvertHelper {
	public static int byteArrayToInt(byte[] a){    
	    int mask = 0xFF;
	    int result = 0;   
	        result = a[0] & mask;
	        result = result + ((a[1] & mask) << 8);
	        result = result + ((a[2] & mask) << 16);
	        result = result + ((a[3] & mask) << 24);            
	    return result;
	}
	
	public static short byteArrayToShort(byte[] a){    
		int mask = 0xff;
		int result = 0;   
	        result = (a[0] & mask);
	        result = (result + ((a[1] & mask) << 8));
	    return (short) result;
	}
	
	public static byte[] intToByteArray(int a)
	{
	    byte[] ret = new byte[4];
	    ret[0] = (byte) (a & 0xFF);   
	    ret[1] = (byte) ((a >> 8) & 0xFF);   
	    ret[2] = (byte) ((a >> 16) & 0xFF);   
	    ret[3] = (byte) ((a >> 24) & 0xFF);
	    return ret;
	}
	
	public static byte[] shortToByteArray(short a)
	{
	    byte[] ret = new byte[2];
	    ret[0] = (byte) (a & 0xFF);   
	    ret[1] = (byte) ((a >> 8) & 0xFF);   
	    return ret;
	}
}
