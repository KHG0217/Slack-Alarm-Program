package work.khg.slack_alarm_program;

import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.Gson;

/**
 * 
 * @author hyukg
 * @Date 2024-07-23
 * @see 슬렉 api를 구현하여 원하는 메시지를 슬렉 알람으로 전송한다.
 * 메시지를 전송할때 webHookUrl이 필요하다.
 * 	-> slack에 incoming WebHooks app을 사용한다.
 *
 */
public class SlackApi {
	/**
	 * 
	 * @param webHookUrl
	 * @param text
	 */
	public void sendSlackText(String webHookUrl, String text){
		try {
			certifyTLS();
			URL url = new URL(webHookUrl);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type", "application/json");
	        connection.setDoOutput(true);
	        connection.setSSLSocketFactory(certifyTLS());
	        Map<String, String> body = new HashMap<>();
	        body.put("text", text);
	        
            Gson gson = new Gson();
            String jsonString = gson.toJson(body);
            
            OutputStream os = connection.getOutputStream();
            os.write(jsonString.getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();

            connection.getResponseCode();
            connection.disconnect();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	

	public SSLSocketFactory certifyTLS() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };
        	SSLSocketFactory getSSlFactory = null;
            try {
                SSLContext sslContext = SSLContext.getInstance("TLS");
				sslContext.init(null, trustAllCerts, new SecureRandom());
	            getSSlFactory = sslContext.getSocketFactory();
	            
			} catch (KeyManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
            return getSSlFactory;
	}
}
