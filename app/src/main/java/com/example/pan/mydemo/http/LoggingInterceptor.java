package com.example.pan.mydemo.http;

import com.cus.pan.library.utils.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by PAN on 2017/11/30.
 */

public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("{");
        FormBody formBody = null;
        if (request.body() instanceof FormBody) {
            formBody = (FormBody) request.body();
        }
        if (formBody != null) {
            for (int i = 0; i < formBody.size(); i++) {
                stringBuffer.append(formBody.name(i)).append(":").append(formBody.value(i));
                if (i + 1 < formBody.size()) {
                    stringBuffer.append(",");
                }
            }
        }
        stringBuffer.append("}");

        long t1 = System.nanoTime();
        LogUtils.i(String.format("Sending request %s%nbody:%s", request.url(), stringBuffer.toString()));

        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        ResponseBody responseBody = response.body();
        long contentLen = responseBody.contentLength();
        String result = "";
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        Charset charset = Util.bomAwareCharset(buffer, response.body().contentType().charset());

        if (contentLen != 0 && charset != null) {
            Buffer buffer1 = buffer.clone();
            GZIPInputStream gzipInputStream = new GZIPInputStream(buffer1.inputStream());
            result = readByInputStreamReader(gzipInputStream);

//            result = buffer1.readString(charset);
            result = decodeUnicode(result);
            buffer1.close();
            buffer1.clear();
            buffer = null;
        }
        String responseStr = String.format("Received response from %s in %.1fms %s%nbody:%s", response.request().url(), (t2 - t1) / 1e6d, response.code(), result + "\nlen:" + result.length());
        LogUtils.i(responseStr);
        return response;
    }

    private String readByInputStreamReader(InputStream is) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int len = 0;
        char[] tempLine = new char[1024];
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "utf-8"), 1024 * 1024);
        while ((len = bufferedReader.read(tempLine)) != -1) {
            stringBuilder.append(tempLine, 0, len);
        }
        return stringBuilder.toString();
    }


    private String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed\\u xxxxencoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }
}
