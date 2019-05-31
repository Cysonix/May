package cn.edu.cuc.logindemo.http;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import cn.edu.cuc.logindemo.AndroidApplication;
import cn.edu.cuc.logindemo.domain.Enums;

/*
 * 网络连接
 */
public class HttpClient {

	private static final int OK = 200;// OK: Success!
	private static final int NOT_MODIFIED = 304;
	private static final int BAD_REQUEST = 400;
	private static final int NOT_AUTHORIZED = 401;
	private static final int FORBIDDEN = 403;
	private static final int NOT_FOUND = 404;
	private static final int NOT_ACCEPTABLE = 406;
	private static final int INTERNAL_SERVER_ERROR = 500;
	private static final int BAD_GATEWAY = 502;
	private static final int SERVICE_UNAVAILABLE = 503;
	private static final int NETWORK_DISABLED=601;

	String multipart_form_data = "multipart/form-data";
	String boundary = "---------------------------114782935826962";
	String twoHyphens="--";
	String lineEnd="\r\n";

	private int retryCount = Configuration.getRetryCount();

	/**
	 * 用户登录认证
	 * @param connectionUrl
	 * @param username
	 * @param password
	 * @return
	 * @throws XException
	 */
	public boolean login(String connectionUrl, String username, String password) throws XException
	{
		boolean result = false;
		try
		{
			HttpGet httpGet = new HttpGet(connectionUrl);
			DefaultHttpClient httpClient = new DefaultHttpClient();

			BasicCredentialsProvider bcp = new BasicCredentialsProvider();
			bcp.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
			httpClient.setCredentialsProvider(bcp);

			HttpResponse httpResponse = httpClient.execute(httpGet);

			int status = httpResponse.getStatusLine().getStatusCode();

			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
			for (String s = reader.readLine(); s != null; s = reader.readLine()) {
				builder.append(s);
			}

			if (status == 200)
				result = true;
			else if (status == 401)
				result = false;
			else
				throw new XException(getCause(status));
		}catch (Exception e){
			throw new XException(e.getMessage(), e);
		}

		return result;
	}

	/**
	 * Post请求
	 * @param postParams
	 * @param connectionUrl
	 * @param connecTimeout
	 * @return
	 * @throws XmcException
	 */
	public String doPost(PostParameter[] postParams,String connectionUrl,int connectTimeout) throws XException{

		Enums.NetStatus netstatus = AndroidApplication.getNetStatus();
		if(netstatus == Enums.NetStatus.Disable){
			return null;
		}
		int retriedCount = Configuration.getRetryCount();
		Response response = null;

		for (retriedCount = 0; retriedCount < retryCount; retriedCount++) {

			int responseCode = -1;
			HttpURLConnection connection = null;
			OutputStream os = null;
			try {

				connection = (HttpURLConnection) new URL(connectionUrl).openConnection();
				if(connectTimeout !=0){
					connection.setConnectTimeout(connectTimeout);
				}
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

				connection.setDoOutput(true);

				String postParam = "";
				if (postParams != null) {
					postParam = encodeParameters(postParams);
				}
				byte[] bytes = postParam.getBytes("UTF-8");
				connection.setRequestProperty("Content-Length",Integer.toString(bytes.length));
				os = connection.getOutputStream();
				os.write(bytes);
				os.flush();
				os.close();
				response = new Response(connection);
				responseCode = response.getStatusCode();

				if (responseCode != OK) {
					if (responseCode < INTERNAL_SERVER_ERROR || retriedCount == retryCount)
						//throw new XmcException(getCause(responseCode) + "\n" + response.asString(), responseCode);
						throw new XException(getCause(responseCode));
				} else {break;}
			}catch(ConnectTimeoutException e){
				throw new XException(XException.CONNECT_TIMEOUT_EXCEPTION,e);
			}catch(InterruptedIOException e){
				throw new XException(XException.INTERRUPTED_IO_EXCEPTION,e);
			}catch (Exception e) {
				throw new XException(e.getMessage(), e);
			}
		}
		return response.asString();
	}

	/**
	 * Get请求
	 * @param getParams
	 * @param connectionUrl
	 * @param connecTimeout
	 * @return
	 * @throws XmcException
	 */
	public String doGet(PostParameter[] getParams, String connectionUrl,int connectTimeout) throws XException{

		Enums.NetStatus netstatus = AndroidApplication.getNetStatus();
		if(netstatus == Enums.NetStatus.Disable)
			return null;
		int retriedCount = 0;
		Response response = null;

		for(retriedCount = 0; retriedCount < retryCount; retriedCount++){

			int responseCode = -1;
			try {
				HttpURLConnection connection = null;
				try {

					if(getParams!=null)
					{
						connection = (HttpURLConnection) new URL(connectionUrl+"?"+encodeParameters(getParams)).openConnection();

					}
					else
					{
						connection = (HttpURLConnection) new URL(connectionUrl).openConnection();
					}
					if(connectTimeout !=0){
						connection.setConnectTimeout(connectTimeout);
					}
					response = new Response(connection);
					responseCode = response.getStatusCode();

					if (responseCode != OK) {
						if (responseCode < INTERNAL_SERVER_ERROR || retriedCount == retryCount)
							//throw new XmcException(getCause(responseCode)+ "\n" + response.asString(), responseCode);
							throw new XException(getCause(responseCode));
					} else {
						break;
					}

				} finally {

				}
			}catch(ConnectTimeoutException e){
				throw new XException(XException.CONNECT_TIMEOUT_EXCEPTION,e);
			}catch(InterruptedIOException e){
				throw new XException(XException.INTERRUPTED_IO_EXCEPTION,e);
			}catch (Exception e) {
				throw new XException(e.getMessage(), e);
			}
		}
		return response.asString();
	}

	/**
	 * 网络请求封装之前对参数进行编码
	 * @param postParams
	 * @return
	 * @throws Exception
	 */
	private static String encodeParameters(PostParameter[] postParams) throws Exception {
		StringBuffer buffer = new StringBuffer();
		if(postParams!=null)
		{

			for (int i = 0; i < postParams.length; i++) {
				if (i != 0) {
					buffer.append("&");
				}
				buffer.append(
						URLEncoder.encode(postParams[i].getName(), "UTF-8"))
						.append("=").append(
						URLEncoder.encode(postParams[i].getObject()
								.toString(), "UTF-8"));
			}
		}
		return buffer.toString();
	}

	/***
	 * 根据response状态码得到网络访问错误的具体原因
	 * @param statusCode
	 * @return
	 */
	private static String getCause(int statusCode) {
		String cause = null;
		switch (statusCode) {
			case NOT_MODIFIED:
				break;
			case BAD_REQUEST:
				cause = "The request was invalid.  An accompanying error message will explain why. This is the status code will be returned during rate limiting.";
				break;
			case NOT_AUTHORIZED:
				cause = "Authentication credentials were missing or incorrect.";
				break;
			case FORBIDDEN:
				cause = "The request is understood, but it has been refused.  An accompanying error message will explain why.";
				break;
			case NOT_FOUND:
				cause = "The URI requested is invalid or the resource requested, such as a user, does not exists.";
				break;
			case NOT_ACCEPTABLE:
				cause = "Returned by the Search API when an invalid format is specified in the request.";
				break;
			case INTERNAL_SERVER_ERROR:
				cause = "Something is broken.  Please post to the group so the Weibo team can investigate.";
				break;
			case BAD_GATEWAY:
				cause = "Weibo is down or being upgraded.";
				break;
			case SERVICE_UNAVAILABLE:
				cause = "Service Unavailable: The Weibo servers are up, but overloaded with requests. Try again later. The search and trend methods use this to indicate when you are being rate limited.";
				break;
			case NETWORK_DISABLED:
				cause = "The Network is disabled.";
				break;
			default:
				cause = "";
		}

		return statusCode + ":" + cause;
	}
}
