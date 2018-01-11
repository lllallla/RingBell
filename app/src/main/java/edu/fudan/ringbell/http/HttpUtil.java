package edu.fudan.ringbell.http;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 *
 */

public class HttpUtil {
    public final static String TAG = "HTTP";
    private final static int CONNECT_TIME = 10000;
    private final static int READ_TIME = 10000;

    public static String sessionid = null;

    /**
     * 发送post请求
     *
     * @param _url
     * @param map
     * @param encoding
     * @return
     * @throws IOException
     */
    private String doPost(String _url, Map<String, String> map, String encoding)
            throws IOException {

        StringBuilder data = new StringBuilder();
        // 数据拼接 key=value&key=value
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                data.append(entry.getKey()).append("=");
                data.append(URLEncoder.encode(entry.getValue(), encoding));
                data.append("&");
            }
            data.deleteCharAt(data.length() - 1);
        }

//        Log.i(TAG, data.toString());
        byte[] entity = data.toString().getBytes();// 生成实体数据
        URL url = new URL(_url);
        HttpURLConnection connection = getHttpURLConnection(_url, "POST");

        connection.setDoOutput(true);// 允许对外输出数据

        connection.setRequestProperty("Content-Length",
                String.valueOf(entity.length));

        OutputStream outStream = connection.getOutputStream();
        outStream.write(entity);
        if (connection.getResponseCode() == 200) {// 成功返回处理数据
            InputStream inStream = connection.getInputStream();

            byte[] number = read(inStream);
            String json = new String(number);
            if (sessionid == null){
                // 取得sessionid.
                String cookieval = connection.getHeaderField("set-cookie");
                if(cookieval != null) {
                    sessionid = cookieval.substring(0, cookieval.indexOf(";"));
                    System.out.println(sessionid);
                }
            }
            return json;
        }

        return null;

    }

    public String doPost(String url) throws IOException {
        return doPost(url, null, "UTF-8");
    }

    public String doPost(String url, Map<String, String> map)
            throws IOException {
        return doPost(url, map, "UTF-8");
    }

    /**
     * 发送GET请求
     *
     * @param url
     * @return
     * @throws IOException
     * @throws Exception
     */
    public String doGet(String url) throws Exception {
        HttpURLConnection connection = getHttpURLConnection(url, "GET");
        if (this.sessionid != null) {
            connection.setRequestProperty("cookie", sessionid);
        } else {
            // 取得sessionid.
            String cookieval = connection.getHeaderField("set-cookie");
            if(cookieval != null) {
                this.sessionid = cookieval.substring(0, cookieval.indexOf(";"));
            }
        }

        if (connection.getResponseCode() == 200) {
            InputStream inStream = connection.getInputStream();
            byte[] number = read(inStream);
            String json = new String(number);
            return json;
        }
        return null;
    }

    public String doFilePost(String urlstr, Map<String, String> map,
                             Map<String, File> files) throws IOException {
        String BOUNDARY = "----WebKitFormBoundaryDwvXSRMl0TBsL6kW"; // 定义数据分隔线

        URL url = new URL(urlstr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 发送POST请求必须设置如下两行
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("connection", "Keep-Alive");
        connection.setRequestProperty("user-agent", "Android WYJ");
        connection.setRequestProperty("Charsert", "UTF-8");
        connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
        if (sessionid != null) {
            connection.setRequestProperty("cookie", sessionid);
        }
        connection.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + BOUNDARY);

        OutputStream out = new DataOutputStream(connection.getOutputStream());
        byte[] end_data = ("--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线

        // 文件
        if (files != null && !files.isEmpty()) {
            for (Map.Entry<String, File> entry : files.entrySet()) {
                File file = entry.getValue();
                String fileName = entry.getKey();

                StringBuilder sb = new StringBuilder();
                sb.append("--");
                sb.append(BOUNDARY);
                sb.append("\r\n");
                sb.append("Content-Disposition: form-data;name=\"" + fileName
                        + "\";filename=\"" + file.getName() + "\"\r\n");
                sb.append("Content-Type: image/jpg\r\n\r\n");
                byte[] data = sb.toString().getBytes();
                out.write(data);

                DataInputStream in = new DataInputStream(new FileInputStream(
                        file));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
                in.close();
            }
        }
        // 数据参数
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                StringBuilder sb = new StringBuilder();
                sb.append("--");
                sb.append(BOUNDARY);
                sb.append("\r\n");
                sb.append("Content-Disposition: form-data; name=\""
                        + entry.getKey() + "\"");
                sb.append("\r\n");
                sb.append("\r\n");
                sb.append(entry.getValue());
                sb.append("\r\n");
                byte[] data = sb.toString().getBytes();
                out.write(data);
            }
        }
        out.write(end_data);
        out.flush();
        out.close();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inStream = connection.getInputStream();
            byte[] number = read(inStream);
            String json = new String(number);
            return json;
        }

        return null;
    }

    private HttpURLConnection getHttpURLConnection(String _url, String method)
            throws IOException {
        URL url = new URL(_url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(CONNECT_TIME);
        connection.setReadTimeout(READ_TIME);
        connection.setRequestMethod(method);

        // 头字段
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Accept-Charset", "UTF-8,*;q=0.5");
        connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        if (sessionid != null) {
            connection.setRequestProperty("cookie", sessionid);
            System.out.println("aaa" + sessionid);
        }

        return connection;

    }

    /**
     * 读取输入流数据 InputStream
     *
     * @param inStream
     * @return
     * @throws IOException
     */
    public static byte[] read(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
