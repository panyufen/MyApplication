package com.example.pan.mydemo.http.convert.msgpack;

import com.google.gson.Gson;

import retrofit2.Converter;

/**
 * Created by PAN on 2018/5/14.
 */
public class MsgPackConverterFactory extends Converter.Factory{

    public static MsgPackConverterFactory create() {
        return create(new Gson());
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static MsgPackConverterFactory create(Gson gson) {
        return null;
    }

    private final Gson gson = null;

}
