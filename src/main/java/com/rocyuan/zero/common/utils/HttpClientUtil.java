package com.rocyuan.zero.common.utils;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


/**
 * ��װ��һЩ����HttpClient����HTTP����ķ���
 * @see ����������õ������µ�HttpComponents-Client-4.2.1
 * @history v1.0-->�½�<code>sendGetRequest(String,String)</code>��<code>sendPostRequest(String,Map<String,String>,String,String)</code>����
 * @history v1.1-->����<code>sendPostSSLRequest(String,Map<String,String>,String,String)</code>����,���ڷ���HTTPS��POST����
 * @history v1.2-->����<code>sendPostRequest(String,String,boolean,String,String)</code>����,���ڷ���HTTPЭ�鱨����Ϊ�����ַ��POST����
 * @history v1.3-->����<code>java.net.HttpURLConnection</code>ʵ�ֵ�<code>sendPostRequestByJava()</code>����
 */
public class HttpClientUtil {
	
	private static final Logger logger = java.util.logging.Logger.getLogger("HttpClientUtil");
	
	
	private HttpClientUtil(){		
		
	}
	

	
	/**
	 * ����HTTP_GET����
	 * @see �÷������Զ��ر�����,�ͷ���Դ
	 * @param requestURL    �����ַ(������)
	 * @param decodeCharset �����ַ�,������Ӧ���ʱ��֮,��ΪnullʱĬ�ϲ���UTF-8����
	 * @return Զ��������Ӧ����
	 */
	public static String sendGetRequest(String reqURL, String decodeCharset){
		long responseLength = 0;       //��Ӧ����
		String responseContent = null; //��Ӧ����
		HttpClient httpClient = new DefaultHttpClient(); //����Ĭ�ϵ�httpClientʵ��
		HttpGet httpGet = new HttpGet(reqURL);           //����org.apache.http.client.methods.HttpGet
		try{
			HttpResponse response = httpClient.execute(httpGet); //ִ��GET����
			HttpEntity entity = response.getEntity();            //��ȡ��Ӧʵ��
			if(null != entity){
				responseLength = entity.getContentLength();
				//responseContent = EntityUtils.toString(entity, decodeCharset==null ? "UTF-8" : decodeCharset);
				responseContent = EntityUtils.toString(entity);
				EntityUtils.consume(entity); //Consume response content
			}
			System.out.println("�����ַ: " + httpGet.getURI());
			System.out.println("��Ӧ״̬: " + response.getStatusLine());
			System.out.println("��Ӧ����: " + responseLength);
			System.out.println("��Ӧ����: " + responseContent);
		}catch(ClientProtocolException e){
			logger.info("���쳣ͨ����Э�������,���繹��HttpGet����ʱ�����Э�鲻��(��'http'д��'htp')���߷������˷��ص����ݲ����HTTPЭ��Ҫ���,��ջ��Ϣ����");
		}catch(ParseException e){
			logger.info(e.getMessage());
		}catch(IOException e){
			logger.info("���쳣ͨ��������ԭ�������,��HTTP������δ������,��ջ��Ϣ����");
		}finally{
			httpClient.getConnectionManager().shutdown(); //�ر�����,�ͷ���Դ
		}
		return responseContent;
	}
	
	
	/**
	 * ����HTTP_POST����
	 * @see �÷���Ϊ<code>sendPostRequest(String,String,boolean,String,String)</code>�ļ򻯷���
	 * @see �÷����ڶ�������ݵı������Ӧ��ݵĽ���ʱ,����õ��ַ��ΪUTF-8
	 * @see ��<code>isEncoder=true</code>ʱ,����Զ���<code>sendData</code>�е�[����][|][ ]�������ַ����<code>URLEncoder.encode(string,"UTF-8")</code>
	 * @param isEncoder ����ָ����������Ƿ���ҪUTF-8����,trueΪ��Ҫ
	 */
	public static String sendPostRequest(String reqURL, String sendData, boolean isEncoder){
		return sendPostRequest(reqURL, sendData, isEncoder, null, null);
	}
	
	
	/**
	 * ����HTTP_POST����
	 * @see �÷������Զ��ر�����,�ͷ���Դ
	 * @see ��<code>isEncoder=true</code>ʱ,����Զ���<code>sendData</code>�е�[����][|][ ]�������ַ����<code>URLEncoder.encode(string,encodeCharset)</code>
	 * @param reqURL        �����ַ
	 * @param sendData      �������,���ж��������Ӧƴ�ӳ�param11=value11?m22=value22?m33=value33����ʽ��,����ò�����
	 * @param isEncoder     ��������Ƿ���ҪencodeCharset����,trueΪ��Ҫ
	 * @param encodeCharset �����ַ�,�����������ʱ��֮,��ΪnullʱĬ�ϲ���UTF-8����
	 * @param decodeCharset �����ַ�,������Ӧ���ʱ��֮,��ΪnullʱĬ�ϲ���UTF-8����
	 * @return Զ��������Ӧ����
	 */
	public static String sendPostRequest(String reqURL, String sendData, boolean isEncoder, String encodeCharset, String decodeCharset){
		String responseContent = null;
		HttpClient httpClient = new DefaultHttpClient();		
		HttpPost httpPost = new HttpPost(reqURL);
		httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
		//httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded");

		try{
			if(isEncoder){
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				for(String str : sendData.split("&")){
					formParams.add(new BasicNameValuePair(str.substring(0,str.indexOf("=")), str.substring(str.indexOf("=")+1)));
				}
				httpPost.setEntity(new StringEntity(URLEncodedUtils.format(formParams, encodeCharset==null ? "UTF-8" : encodeCharset)));
			}else{
				httpPost.setEntity(new StringEntity(sendData));
			}
			
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				responseContent = EntityUtils.toString(entity, decodeCharset==null ? "UTF-8" : decodeCharset);
				System.err.println(EntityUtils.toString(entity));
				EntityUtils.consume(entity);
			}
		}catch(Exception e){
			logger.info("��[" + reqURL + "]ͨ�Ź���з����쳣,��ջ��Ϣ����");
		}finally{
			httpClient.getConnectionManager().shutdown();
		}
		return responseContent;
	}
	
	
	/**
	 * ����HTTP_POST����
	 * @see �÷������Զ��ر�����,�ͷ���Դ
	 * @see �÷������Զ���<code>params</code>�е�[����][|][ ]�������ַ����<code>URLEncoder.encode(string,encodeCharset)</code>
	 * @param reqURL        �����ַ
	 * @param params        �������
	 * @param encodeCharset �����ַ�,�����������ʱ��֮,��ΪnullʱĬ�ϲ���UTF-8����
	 * @param decodeCharset �����ַ�,������Ӧ���ʱ��֮,��ΪnullʱĬ�ϲ���UTF-8����
	 * @return Զ��������Ӧ����
	 */
	public static String sendPostRequest(String reqURL, Map<String, String> params, String encodeCharset, String decodeCharset){
		String responseContent = null;
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpPost httpPost = new HttpPost(reqURL);				
		List<NameValuePair> formParams = new ArrayList<NameValuePair>(); //�����������
		for(Map.Entry<String,String> entry : params.entrySet()){			
			formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		try{
			httpPost.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset==null ? "UTF-8" : encodeCharset));
			/**
			 * test code 
			 */
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				responseContent = EntityUtils.toString(entity, decodeCharset==null ? "UTF-8" : decodeCharset);
				EntityUtils.consume(entity);
			}
		}catch(Exception e){
			logger.info("��[" + reqURL + "]ͨ�Ź���з����쳣,��ջ��Ϣ����");
		}finally{
			httpClient.getConnectionManager().shutdown();
		}
		return responseContent;
	}
	
	
	/**
	 * ����HTTPS_POST����
	 * @see �÷���Ϊ<code>sendPostSSLRequest(String,Map<String,String>,String,String)</code>�����ļ򻯷���
	 * @see �÷����ڶ�������ݵı������Ӧ��ݵĽ���ʱ,����õ��ַ��ΪUTF-8
	 * @see �÷������Զ���<code>params</code>�е�[����][|][ ]�������ַ����<code>URLEncoder.encode(string,"UTF-8")</code>
	 */
	public static String sendPostSSLRequest(String reqURL, Map<String, String> params){
		return sendPostSSLRequest(reqURL, params, null, null);
	}
	
	
	/**
	 * ����HTTPS_POST����
	 * @see �÷������Զ��ر�����,�ͷ���Դ
	 * @see �÷������Զ���<code>params</code>�е�[����][|][ ]�������ַ����<code>URLEncoder.encode(string,encodeCharset)</code>
	 * @param reqURL        �����ַ
	 * @param params        �������
	 * @param encodeCharset �����ַ�,�����������ʱ��֮,��ΪnullʱĬ�ϲ���UTF-8����
	 * @param decodeCharset �����ַ�,������Ӧ���ʱ��֮,��ΪnullʱĬ�ϲ���UTF-8����
	 * @return Զ��������Ӧ����
	 */
	public static String sendPostSSLRequest(String reqURL, Map<String, String> params, String encodeCharset, String decodeCharset){
		String responseContent = "";
		HttpClient httpClient = new DefaultHttpClient();
		X509TrustManager xtm = new X509TrustManager(){
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
			public X509Certificate[] getAcceptedIssuers() {return null;}
		};
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[]{xtm}, null);
			org.apache.http.conn.ssl.SSLSocketFactory socketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(ctx);
			httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
			
			HttpPost httpPost = new HttpPost(reqURL);
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			for(Map.Entry<String,String> entry : params.entrySet()){
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset==null ? "UTF-8" : encodeCharset));
			
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				responseContent = EntityUtils.toString(entity, decodeCharset==null ? "UTF-8" : decodeCharset);
				EntityUtils.consume(entity);
			}
		} catch (Exception e) {
			logger.info("��[" + reqURL + "]ͨ�Ź���з����쳣,��ջ��ϢΪ");
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return responseContent;
	}
	
	
	/**
	 * ����HTTP_POST����
	 * @see �����͵�<code>params</code>�к�������,�ǵð���˫��Լ�����ַ�����<code>URLEncoder.encode(string,encodeCharset)</code>
	 * @see ������Ĭ�ϵ����ӳ�ʱʱ��Ϊ30��,Ĭ�ϵĶ�ȡ��ʱʱ��Ϊ30��
	 * @param reqURL �����ַ
	 * @param params ���͵�Զ��������������,���������Ϊ<code>java.util.Map<String, String></code>
	 * @return Զ��������Ӧ����`HTTP״̬��,��<code>"SUCCESS`200"</code><br>��ͨ�Ź���з����쳣�򷵻�"Failed`HTTP״̬��",��<code>"Failed`500"</code>
	 */
	public static String sendPostRequestByJava(String reqURL, Map<String, String> params){
		StringBuilder sendData = new StringBuilder();
		for(Map.Entry<String, String> entry : params.entrySet()){
			sendData.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		if(sendData.length() > 0){
			sendData.setLength(sendData.length() - 1); //ɾ�����һ��&���
		}
		return sendPostRequestByJava(reqURL, sendData.toString());
	}
	
	
	/**
	 * ����HTTP_POST����
	 * @see �����͵�<code>sendData</code>�к�������,�ǵð���˫��Լ�����ַ�����<code>URLEncoder.encode(string,encodeCharset)</code>
	 * @see ������Ĭ�ϵ����ӳ�ʱʱ��Ϊ30��,Ĭ�ϵĶ�ȡ��ʱʱ��Ϊ30��
	 * @param reqURL   �����ַ
	 * @param sendData ���͵�Զ��������������
	 * @return Զ��������Ӧ����`HTTP״̬��,��<code>"SUCCESS`200"</code><br>��ͨ�Ź���з����쳣�򷵻�"Failed`HTTP״̬��",��<code>"Failed`500"</code>
	 */
	public static String sendPostRequestByJava(String reqURL, String sendData){
		HttpURLConnection httpURLConnection = null;
		OutputStream out = null; //д
		InputStream in = null;   //��
		int httpStatusCode = 0;  //Զ��������Ӧ��HTTP״̬��
		try{
			URL sendUrl = new URL(reqURL);
			httpURLConnection = (HttpURLConnection)sendUrl.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setDoOutput(true);        //ָʾӦ�ó���Ҫ�����д��URL����,��ֵĬ��Ϊfalse
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setConnectTimeout(30000); //30�����ӳ�ʱ
			httpURLConnection.setReadTimeout(30000);    //30���ȡ��ʱ
			
			out = httpURLConnection.getOutputStream();
			out.write(sendData.toString().getBytes());
			
			//��ջ�����,�������
			out.flush();
			
			//��ȡHTTP״̬��
			httpStatusCode = httpURLConnection.getResponseCode();
			
			//�÷���ֻ�ܻ�ȡ��[HTTP/1.0 200 OK]�е�[OK]
			//���Է���Ӧ�����ķ����˷��ر��ĵ����һ��,��÷�����ȡ��������,��ֻ�ܻ�ȡ��[OK],�����ź�
			//respData = httpURLConnection.getResponseMessage();
			
//			//���?�ؽ��
//			BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
//			String row = null;
//			String respData = "";
//			if((row=br.readLine()) != null){ //readLine()�����ڶ�������[\n]��س�[\r]ʱ,����Ϊ��������ֹ
//				respData = row;              //HTTPЭ��POST��ʽ�����һ�����Ϊ�������
//			}
//			br.close();
			
			in = httpURLConnection.getInputStream();             
			byte[] byteDatas = new byte[in.available()];
			in.read(byteDatas);
			return new String(byteDatas) + "`" + httpStatusCode;
		}catch(Exception e){
			logger.info(e.getMessage());
			return "Failed`" + httpStatusCode;
		}finally{
			if(out != null){
				try{
					out.close();
				}catch (Exception e){
					logger.info("�ر������ʱ�����쳣,��ջ��Ϣ����");
				}
			}
			if(in != null){
				try{
					in.close();
				}catch(Exception e){
					logger.info("�ر�������ʱ�����쳣,��ջ��Ϣ����");
				}
			}
			if(httpURLConnection != null){
				httpURLConnection.disconnect();
				httpURLConnection = null;
			}
		}
	}
	
	
	
	
	/**
	 * HashMap ת��  a=1&b=2����ʽ NameParams��Ȼ������ȥ
	 * @param searchParams
	 * @param hashMap
	 */
   public static String sendPostForSearch(String url ,Map<String,String> params){
	   StringBuilder sb = new StringBuilder();
	   for(Map.Entry<String, String> entry:params.entrySet()){
			sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");			
		}
		sb=sb.deleteCharAt(sb.length()-1);		
		System.out.println(sb.toString());
	   String jsonData = HttpClientUtil.sendPostRequest(url, sb.toString(), false);	
	   return jsonData;
  }
  

   /**
    * 
    * @param url  �ϴ��ĵ�ַ 
    * @param filePath
    * @return �ϴ���Ϻ� ��õ���Ӧ
    */

   public static String  uploadFile(String url ,String filePath){
	   File file = new File(filePath);
	   if(!file.exists() || !file.isFile()){
		   System.out.println( " Ҫ�ϴ����ļ�������");
		   return null;
	   }
	   
	   try {
		URL conn = new URL(url);
		HttpURLConnection httpcon = (HttpURLConnection) conn.openConnection();
		httpcon.setRequestMethod("POST");
		httpcon.setDoInput(true);
		httpcon.setDoOutput(true);	// �����Ƿ���httpUrlConnection�������Ϊ�����post���󣬲���Ҫ���� 
															// http�����ڣ������Ҫ��Ϊtrue, Ĭ���������false; 
		
		httpcon.setUseCaches(false); // post ��ʽ��ʹ�û���
		// ��������ͷ��Ϣ
		httpcon.setRequestProperty("Connection", "Keep-Alive");
		httpcon.setRequestProperty("Charset", "UTF-8");
		
		// ���ñ߽�
		String BOUNDARY = "----------" + System.currentTimeMillis();
		httpcon.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ BOUNDARY);
		
		// ���Ĳ���
		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
				+ file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		byte[] head = sb.toString().getBytes("utf-8");
		
		OutputStream out = new DataOutputStream(httpcon.getOutputStream());
		out.write(head);
		
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte [] byttout = new byte[1024];
		while( (bytes = in.read(byttout))!=-1 ){
			out.write(byttout,0,bytes);
		}
		in.close();
		
	   //	��β
		String end = "\r\n--"+BOUNDARY+"--\r\n";
		out.write(end.getBytes("utf-8"));
		
		out.flush();
		out.close();
		
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
		
		String getstr ;
		while ( (getstr = reader.readLine())!=null ){
			buffer.append(getstr);
		}	
		reader.close();
		
		return buffer.toString();
		
		
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   return null;
   }
   
   
   
   
   public static String getIpAddr(HttpServletRequest request) {
	   	//X-Forwarded-For:���XFFͷ������ͻ��ˣ�Ҳ����HTTP���������ʵ��IP��ֻ����ͨ����HTTP ������߸��ؾ�������ʱ�Ż���Ӹ���
	 String ip = request.getHeader("x-forwarded-for");
	 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	 ip = request.getHeader("Proxy-Client-IP");
	 }
	 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	 //
	 ip = request.getHeader("WL-Proxy-Client-IP");
	 }
	 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	 ip = request.getRemoteAddr();
	 }
	 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	 ip = request.getHeader("http_client_ip");
	 }
	 if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	 ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	 }
	 // ����Ƕ༶���?��ôȡ��һ��ipΪ�ͻ�ip
	 if (ip != null && ip.indexOf(",") != -1) {
	 ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
	 }
	 return ip;
	 } 
   
// http https ������   
	public static String httpRequest(String requestUrl,
			String requestMethod, String outputStr) {
		
		StringBuffer buffer = new StringBuffer();
		try {
			// ����SSLContext���󣬲�ʹ������ָ�������ι�������ʼ��
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// ������SSLContext�����еõ�SSLSocketFactory����
			javax.net.ssl.SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			// ��������ʽ��GET/POST��
			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod))
				httpUrlConn.connect();

			// ���������Ҫ�ύʱ
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				// ע������ʽ����ֹ��������
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			// �����ص�������ת�����ַ�
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// �ͷ���Դ
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			
		} catch (ConnectException ce) {
			System.out.println("."+ce);
		} catch (Exception e) {
			System.out.println("https request error:{}"+ e);
		}
		return buffer.toString();
	}
   
   /**
    * 
    * @Description: ģ���½Post����
    * @param @return    
    * @return String   
    * @throws
    */
   public static String simulateHttpPost(String url ,Map<String,String> header,Map<String,String>formdata){
	   HttpClient httpClient = new DefaultHttpClient();
	   HttpClientParams.setCookiePolicy(httpClient.getParams(), CookiePolicy.BROWSER_COMPATIBILITY);    
    	
	   HttpPost request = new HttpPost(url);
    	if(header!=null && header.size()>0){
    		for(Map.Entry<String, String> entity : header.entrySet()){
    			request.setHeader(entity.getKey(), entity.getValue());
    		}
    	}
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	for(Map.Entry<String,String> entry : formdata.entrySet()){
    		params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
    	}
    	
    	String responseStr = null;
    	try {
    		if(url.startsWith("https")){
	    		   X509TrustManager xtm = new X509TrustManager(){
	    				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
	    				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
	    				public X509Certificate[] getAcceptedIssuers() {return null;}
	    			};
	    		SSLContext ctx = SSLContext.getInstance("TLS");
	    		ctx.init(null, new TrustManager[]{xtm}, null);
	    		org.apache.http.conn.ssl.SSLSocketFactory socketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(ctx);
	    		httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
    		}
    		
			request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			HttpResponse response = httpClient.execute(request);
			HttpEntity entity = response.getEntity();
			if ( entity !=null ){
				responseStr = EntityUtils.toString(entity,"UTF-8");
				EntityUtils.consume(entity);
			}
		} catch (Exception e) {			
			e.printStackTrace();
		}finally{
			httpClient.getConnectionManager().shutdown();
		}
    	return responseStr;
    	
    }
   
   private static boolean isLocalTest(HttpServletRequest request){
		String serviceIP = request.getLocalAddr();
		if (serviceIP.contains("10.10.121.")) {
			return true;
		}
		return false;
	}
   

	public static  String  get(String url) {
		
		URL requestURL;
		HttpURLConnection conn;
		StringBuffer res = new StringBuffer(); 
		try{
			requestURL = new URL(url);
			conn = (HttpURLConnection) requestURL.openConnection();
			conn.setRequestMethod("GET");// "POST" ,"GET"  
//	        conn.addRequestProperty("Cookie", cookie);  
//	        conn.addRequestProperty("Accept-Charset", "UTF-8;");// GB2312,  
//	        conn.addRequestProperty("User-Agent",  
//	                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");  
	        conn.connect();  
	        
//	        if (conn.getHeaderFields().get("Set-Cookie") != null) {  
//	            for (String s : conn.getHeaderFields().get("Set-Cookie")) {  
//	                cookie += s;  
//	            }  
//	        }  
	        InputStream ins = conn.getInputStream();
	        InputStreamReader inr = new InputStreamReader(ins);
	        BufferedReader bfr = new BufferedReader(inr);
	        
	        String line ="";
	        do{
	        	res.append(line);
	        	line = bfr.readLine();
	        }while(line!=null);
	        
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return res.toString();
	}
	
}

/**
 * 
 * @author liufeng
 * @date 2013-08-08
 */
class MyX509TrustManager implements X509TrustManager {

	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}
