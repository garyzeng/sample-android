package cn.wsn.yunnan.library.nfc;

@SuppressWarnings("serial")
public class NfcException extends Exception {

	public NfcException() 
	{
		super();
	}
	
	public NfcException(String msg) 
	{
		super(msg);
		_errorDescriptionString = msg;
	}
	
	public NfcException(NfcErrorCode errorCode, String msg) 
	{
		super(msg);
		_errorDescriptionString = msg;
		_errorCode = errorCode;
	}
	
	public NfcException(NfcErrorCode errorCode, String msg, Throwable cause) 
	{
		super(msg, cause);
		_errorDescriptionString = msg;
		_errorCode = errorCode;
	}
	
	public NfcException(Throwable cause) 
	{
		super(cause);
	}	
		
	protected NfcErrorCode _errorCode;
	protected String _errorDescriptionString = "";
	
	public void setErrorCode(NfcErrorCode errorCode)
	{
		_errorCode = errorCode;
	}
	
	public void setDescription(String description)
	{
		_errorDescriptionString = description;
	}
	
	public NfcErrorCode getErrorCode()
	{
		return _errorCode;
	}
	
	public String getDescription(String description)
	{
		return _errorDescriptionString;
	}
}
