package cn.meyoung.wlfz.webapi.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

public final class RequestUtils {

    //post超时时间
    private static final int TIMEOUT_IN_MILLIONS = 10 * 1000;

    /**
     * 获取客户端ip
     *
     * @param request
     * @return
     */
    public static String getUserIPAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;

    }


    /**
     * 发送POST请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws Exception
     */
    public static URLConnection sendPostRequest(String url,
                                                Map<String, String> params, Map<String, String> headers)
            throws Exception {
        StringBuilder buf = new StringBuilder();
        Set<Entry<String, String>> entrys = null;
        // 如果存在参数，则放在HTTP请求体，形如name=aaa&age=10
        if (params != null && !params.isEmpty()) {
            entrys = params.entrySet();
            for (Map.Entry<String, String> entry : entrys) {
                buf.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"))
                        .append("&");
            }
            buf.deleteCharAt(buf.length() - 1);
        }
        URL url1 = new URL(url);

        //byte[] entitydata = buf.toString().getBytes();// 得到实体的二进制数据

        HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
        //post需要设置
        conn.setDoOutput(true);
        //  conn.setDoInput(true);

        // 设置默认的http请求消息头。。。
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        // conn.setRequestProperty("Content-Length",
        // String.valueOf(buf.toString().getBytes("UTF-8").length));
        if (headers != null && !headers.isEmpty()) {
            entrys = headers.entrySet();
            for (Map.Entry<String, String> entry : entrys) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        // 流的形式输出。。发送请求的参数
        OutputStream out = conn.getOutputStream();
        out.write(buf.toString().getBytes("UTF-8"));

        // 施放流。。。。
        out.flush();
        out.close();
        //请求成功
        if (conn.getResponseCode() == 200) {
            return conn;
        } else {
            return null;
        }
    }

    /**
     * 发送POST请求方法2
     *
     * @param url
     * @param params//--key=value& 连接形式
     * @return
     * @throws Exception
     */
    public String sendPost(String url, String params) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            /*conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");*/
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            out.write(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * httpClient PostMethod 传参方法 :post 请求方法3
     */
    public static String sendPostMethod(String url, Map<String, String> params) throws IOException {
        HttpClient client = new HttpClient();

        PostMethod postMethod = new PostMethod(url);
        postMethod.getParams().setParameter(
                HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");//消除乱码问题


        Set<Entry<String, String>> entrys = null;
        // 如果存在参数，则放在HTTP请求体，形如name=aaa&age=10
        if (params != null && !params.isEmpty()) {
            entrys = params.entrySet();
            for (Map.Entry<String, String> entry : entrys) {
                //循环加入到参数
                postMethod.addParameter(entry.getKey(), URLEncoder.encode(entry.getValue()));
            }
        }
       /* //或者
        //填入各个表单域的值
        NameValuePair[] data = {
                new NameValuePair("ID", "11"),
                new NameValuePair("mtg", "0"),
                new NameValuePair("haveCookie", "0"),
                new NameValuePair("backID", "30"),
                new NameValuePair("psw", "password")
        };
        //   将表单的值放入postMethod中
        postMethod.setRequestBody(data);*/


        //执行返回state状态
        int statCode = client.executeMethod(postMethod);
        if (statCode == HttpStatus.SC_OK) {

            String str = "";
            try {
                str = postMethod.getResponseBodyAsString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return str;
           /* 或者
           StringBuffer contentBuffer = new StringBuffer();
            InputStream in = postMethod.getResponseBodyAsStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in,postMethod.getResponseCharSet()));
            String inputLine = null;
            while((inputLine = reader.readLine()) != null)
            {
                contentBuffer.append(inputLine);
                System.out.println("input line:"+ inputLine);
                contentBuffer.append("/n");
            }
            //关闭流
            in.close();*/

        } else {

        }
        //释放连接
        postMethod.releaseConnection();

        return "";
    }


    /**
     * 解析json返回
     *
     * @return
     * @throws Exception
     */
    public static boolean parseJSONCargoEvaluation(InputStream inStream)
            throws Exception {

        byte[] data = read(inStream);
        String json = new String(data);
        // L.i(TAG, "parseJSONCargoEvaluation"+json);
        String messageString = null;
        //jackson
        JSONObject jsonObject = JSONObject.parseObject(json);
        boolean flag = jsonObject.getBoolean("success");

        if (flag) {
            return flag;
        } else {
            JSONObject resultJSON = jsonObject.getJSONObject("error");
            messageString = resultJSON.getString("message");
            //   L.i(TAG, "parseJSONCargoEvaluation解析失败=="+messageString);
            // L.i(TAG, "parseJSONCargoEvaluation解析失败=="+"解析失败");
            return flag;
        }
    }

    /**
     * 读取输入流数据
     *
     * @param inStream
     * @return
     */
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }


    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

}
