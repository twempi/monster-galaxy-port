package crittercism.android;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import oauth.signpost.OAuth;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.json.JSONObject;
import twitter4j.internal.http.HttpResponseCode;

/* JADX INFO: loaded from: /home/edward/Documents/projects/monster-galaxy/work-exile/apk/classes.dex */
public class c {
    private final DefaultHttpClient a;
    private final String b;
    private Context c;

    public c() {
        this.b = "";
        this.a = a();
    }

    public c(String str, Context context) {
        this.b = str;
        this.a = a();
        this.c = context;
    }

    private String a(HttpRequestBase httpRequestBase) throws f, IOException {
        String string = "";
        try {
            this.a.getConnectionManager().closeExpiredConnections();
            HttpResponse httpResponseExecute = this.a.execute(httpRequestBase);
            int statusCode = httpResponseExecute.getStatusLine().getStatusCode();
            switch (statusCode) {
                case HttpResponseCode.OK /* 200 */:
                    string = EntityUtils.toString(httpResponseExecute.getEntity(), OAuth.ENCODING);
                    break;
                default:
                    c.class.getCanonicalName();
                    String str = "executeHttpRequest error=" + statusCode + " - " + httpResponseExecute.getStatusLine().toString();
                    httpResponseExecute.getEntity().consumeContent();
                    break;
            }
        } catch (ConnectTimeoutException e) {
            c.class.getClass().getCanonicalName();
            throw new f(a.a(11), f.a.CONN_TIMEOUT);
        } catch (IOException e2) {
            httpRequestBase.abort();
            for (int i = 0; i < httpRequestBase.getAllHeaders().length; i++) {
                Header header = httpRequestBase.getAllHeaders()[i];
                String str2 = header.getName() + ": " + header.getValue();
            }
            if (e2.getMessage() != null && e2.getMessage().toLowerCase().contains("no route to host")) {
                throw new f(a.a(10), f.a.NO_INTERNET);
            }
            if (!e2.getStackTrace()[0].toString().contains("java.net.InetAddress.lookupHostByName")) {
                throw e2;
            }
            c.class.getClass().getCanonicalName();
            throw new f(a.a(10), f.a.NO_INTERNET);
        } catch (Exception e3) {
            c.class.getClass().getCanonicalName();
            String str3 = "Neither ConnectTimeoutException nor IOException.  Instead: " + e3.getClass().getName();
        }
        return string;
    }

    private static DefaultHttpClient a() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        X509HostnameVerifier x509HostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier(x509HostnameVerifier);
        schemeRegistry.register(new Scheme("https", socketFactory, 443));
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(basicHttpParams, false);
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, 20000);
        HttpConnectionParams.setSoTimeout(basicHttpParams, 20000);
        HttpConnectionParams.setSocketBufferSize(basicHttpParams, Opcodes.ACC_ANNOTATION);
        HttpClientParams.setRedirecting(basicHttpParams, false);
        return new DefaultHttpClient(new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry), basicHttpParams);
    }

    public final String a(String str, JSONObject jSONObject) {
        HttpPost httpPost = new HttpPost(str);
        httpPost.addHeader("User-Agent", this.b);
        httpPost.addHeader("Content-Type", "application/json");
        try {
            httpPost.setEntity(new ByteArrayEntity(jSONObject.toString().getBytes("UTF8")));
            String str2 = "JSON Object for request: " + jSONObject.toString();
            String str3 = "httpPost Entity is: " + httpPost.getEntity().getContent().toString();
            return a(httpPost);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error encoding http params");
        }
    }

    public final String a(String str, JSONObject jSONObject, String[] strArr, String str2) {
        new String();
        byte[] bArr = new byte[30];
        new Random().nextBytes(bArr);
        String strA = g.a(bArr);
        String str3 = "boundary = " + strA;
        HttpPost httpPost = new HttpPost(str);
        httpPost.addHeader("User-Agent", this.b);
        httpPost.addHeader("Content-Type", "multipart/form-data; boundary=" + strA + "; charset=\"utf-8\"");
        httpPost.addHeader("Accept", "text/plain, application/json");
        httpPost.addHeader("Content-Disposition", "form-data");
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            StringBuilder sb = new StringBuilder();
            sb.append("--" + strA + "\n");
            sb.append("Content-Disposition: form-data; name=\"" + str2 + "json\";\n");
            sb.append("Content-type: application/json\n\n");
            sb.append(jSONObject.toString() + "\n");
            byteArrayOutputStream.write(sb.toString().getBytes(OAuth.ENCODING));
            for (int i = 0; i < strArr.length; i++) {
                File file = new File(strArr[i]);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("--" + strA + "\n");
                sb2.append("Content-Disposition: form-data; name=\"" + str2 + Integer.toString(i) + "\"; filename=\"" + file.getName() + "\";\n");
                sb2.append("Content-type: application/octet-stream\n\n");
                byteArrayOutputStream.write(sb2.toString().getBytes(OAuth.ENCODING));
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bArr2 = new byte[Opcodes.ACC_ANNOTATION];
                while (true) {
                    int i2 = fileInputStream.read(bArr2);
                    if (i2 >= 0) {
                        byteArrayOutputStream.write(bArr2, 0, i2);
                    }
                }
                byteArrayOutputStream.write(("\n").getBytes(OAuth.ENCODING));
                fileInputStream.close();
                byteArrayOutputStream.close();
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append("--" + strA + "--");
            byteArrayOutputStream.write(sb3.toString().getBytes(OAuth.ENCODING));
            httpPost.setEntity(new ByteArrayEntity(byteArrayOutputStream.toByteArray()));
        } catch (Exception e) {
        }
        return a(httpPost);
    }
}
