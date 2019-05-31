package cn.edu.cuc.logindemo.domain;

public class MitiException extends Exception{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final String CONNECT_TIMEOUT_EXCEPTION= "ConnectionTimeout";				//客户端已经与服务器建立了socket连接，但是服务器并没有处理客户端的请求
	public static final String INTERRUPTED_IO_EXCEPTION = "ConnectionTimeout";				//请求和服务器建立socket连接，但是很长时间内都没有建立socket连接
	public static final String TIMEOUT = "Timeout";

	public MitiException(String msg){
		super(msg);
	}

//	public XmcException(String msg,int statusCode){
//		super(msg);
//	}

	public MitiException(String msg,Exception cause){
		super(msg,cause);
	}

//	public XmcException(String msg,Exception cause,int statusCode){
//		super(msg,cause);
//	}


}
