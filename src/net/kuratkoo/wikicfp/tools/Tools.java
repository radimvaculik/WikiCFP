package net.kuratkoo.wikicfp.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Tools
 *
 * @author Radim -kuratkoo- Vaculik <kuratkoo@gmail.com>
 */
public class Tools {

    public static StringBuilder getContent(String url) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(new HttpGet(url));
        return Tools.inputStreamToString(response.getEntity().getContent());
    }

    private static StringBuilder inputStreamToString(InputStream is) throws IOException {
        String line;
        StringBuilder total = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        while ((line = rd.readLine()) != null) {
            total.append(line);
        }
        return total;
    }
}
