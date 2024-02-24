package vn.app.tintoc.helper;


import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.entity.mime.content.StringBody;
//import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Admin on 7/28/2017.
 */

public class HttpHelper {

    //region POST
    public static String post(String url, List<Map.Entry<String, String>> params) {
        String retValue = "";
        try {
            String uri = url;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(uri);
            List nameValuePairs = new ArrayList();
            if (params != null) {
                for (Map.Entry<String, String> param : params) {
                    nameValuePairs.add(new BasicNameValuePair(param.getKey(), param.getValue()));

                }
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            retValue = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retValue;
    }
    //endregion

    //region GET
    public static String get(String url, List<Map.Entry<String, String>> params) {
        String retValue = "";
        try {
            String uri = url;
            if (params != null) {
                for (Map.Entry<String, String> param : params) {
                    if (!uri.contains("?")) {
                        uri += "?" + param.getKey() + "=" + param.getValue();
                    } else {
                        uri += "&" + param.getKey() + "=" + param.getValue();
                    }

                }
            }
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpPost = new HttpGet(uri);

            HttpResponse response = httpClient.execute(httpPost);
            retValue = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retValue;
    }

    //GET url by URLConnection
//    public static String get(String url, Map map) {
//        String retValue = "";
//        //map.put("aaa", 0);
//        try {
//            String uri = url;
//            if (map != null) {
//                for (Object key : map.keySet()) {
//                    if (!uri.contains("?")) {
//                        uri += "?" + key + "=" + map.get(key);
//                    } else {
//                        uri += "&" + key + "=" + map.get(key);
//                    }
//                    System.out.println("Key : " + key.toString() + " Value : " + map.get(key));
//                }
//
//            }
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpGet httpGet = new HttpGet(uri);
//
//            HttpResponse response = httpClient.execute(httpGet);
//            retValue = EntityUtils.toString(response.getEntity());
//
//            URL urlConnect;
//            Scanner scanner=new Scanner(System.in);
//            uri=scanner.nextLine();
//            urlConnect= new URL(uri);
//            HttpURLConnection connection= (HttpURLConnection) urlConnect.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setDoInput(true);
//            connection.connect();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return retValue;
//    }
    //endregion

    //region Post file
    public static String postFile(String url, List<Map.Entry<String, String>> params, String keyFile, File file) {
        String retValue = "";
        try {
            String uri = url;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(uri);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            multipartEntityBuilder.addBinaryBody(
                    keyFile,
                    new FileInputStream(file),
                    ContentType.APPLICATION_OCTET_STREAM,
                    file.getName()
            );
            //multipartEntityBuilder.addPart(keyFile, fileBody);
            if (params != null) {
                for (Map.Entry<String, String> param : params) {
                    multipartEntityBuilder.addTextBody(param.getKey(), param.getValue(), ContentType.TEXT_PLAIN);
                }
            }
            postRequest.setEntity(multipartEntityBuilder.build());
            HttpResponse response = httpClient.execute(postRequest);
            retValue = EntityUtils.toString(response.getEntity());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retValue;
    }

    public static String postFile(String url, List<Map.Entry<String, String>> params, List<File> files) {
        String retValue = "";
        try {
            String uri = url;
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(uri);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            for (File file : files) {
                multipartEntityBuilder.addBinaryBody(
                        "image",
                        new FileInputStream(file),
                        ContentType.APPLICATION_OCTET_STREAM,
                        file.getName()
                );
            }

            //multipartEntityBuilder.addPart(keyFile, fileBody);
            if (params != null) {
                for (Map.Entry<String, String> param : params) {
                    multipartEntityBuilder.addTextBody(param.getKey(), param.getValue(), ContentType.TEXT_PLAIN.withCharset("UTF-8"));
                }
            }
            postRequest.setEntity(multipartEntityBuilder.build());
            HttpResponse response = httpClient.execute(postRequest);
            retValue = EntityUtils.toString(response.getEntity());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retValue;
    }
    //endregion
}
