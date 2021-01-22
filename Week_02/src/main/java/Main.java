
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Main {

    private final static String HTTP_GET_URL = "http://localhost:8801";

    public static void main(String[] args) {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(HTTP_GET_URL);
        CloseableHttpResponse responseBody = null;
        try {
            responseBody = httpclient.execute(httpGet);

            HttpEntity entity = responseBody.getEntity();
            System.out.println(responseBody.getStatusLine());

            if (null != entity){
                System.out.println("Response content length: " + entity.getContentLength());
                System.out.println("Response content: " + EntityUtils.toString(entity));
            }else {
                System.out.println("Response content is null");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }finally {
            if (null != responseBody){
                try {
                    responseBody.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            httpGet.releaseConnection();
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
