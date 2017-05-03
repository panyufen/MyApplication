package com.example.pan.mydemo.pojo;

/**
 * Created by PAN on 2017/5/3.
 */

public class CpuInfo {
    public long minFreq;
    public long maxFreq;
    public long curFreq;

    public String getMinFreqStr() {
        if (minFreq > 0) {
            return dealUnit(minFreq);
        }
        return "N/A";
    }

    public String getMaxFreqStr() {
        if (maxFreq > 0) {
            return dealUnit(maxFreq);
        }
        return "N/A";
    }

    public String getCurFreqStr() {
        if (curFreq > 0) {
            return dealUnit(curFreq);
        }
        return "offline";
    }

    private String dealUnit(long v) {
        return v / 1000 + "MHZ";
    }
}
