package cn.edu.cuc.logindemo.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;

/*
 * 网络请求的返回值
 */
public class Response {

	private int statusCode;
	private InputStream is;
	private HttpURLConnection con;
	private String responseAsString=null;
	private boolean streamConsumed=false;

	public Response(){

	}

	public Response(HttpURLConnection connection) throws IOException{
		this.con=connection;
		this.statusCode=connection.getResponseCode();
		if(null==(is=connection.getErrorStream())){
			is=connection.getInputStream();
		}
		if(null!=is&&"gzip".equals(con.getContentEncoding())){
			is=new GZIPInputStream(is);
		}
	}

	public InputStream asStream(){
		if(streamConsumed){
			throw new IllegalStateException("Stream has already been consumed.");
		}

		return is;
	}

	public String asString() throws XException{

		if(null==responseAsString){
			BufferedReader bufferedReader;
			try {
				InputStream stream=asStream();
				if(null==stream){
					return null;
				}

				bufferedReader=new BufferedReader(new InputStreamReader(stream,"UTF-8"));
				StringBuffer buffer=new StringBuffer();
				String line;
				while(null!=(line=bufferedReader.readLine())){
					buffer.append(line).append("\n");
				}

				this.responseAsString=buffer.toString();
				stream.close();
				con.disconnect();
				streamConsumed=true;

			} catch (NullPointerException e) {
				throw new XException(e.getMessage(),e);
			} catch(IOException ioe){
				throw new XException(ioe.getMessage(),ioe);
			}
		}

		return responseAsString;
	}

	public int getStatusCode() {
		return statusCode;
	}


}
