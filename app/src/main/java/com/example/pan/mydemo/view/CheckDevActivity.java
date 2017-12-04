package com.example.pan.mydemo.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.view.base.BaseActivity;
import com.example.pan.mydemo.pojo.CpuInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CheckDevActivity extends BaseActivity {

    private String TAG = "CheckDevActivity";

    private TextView textView;

    private List<CpuInfo> cpuInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_dev);
        init();
    }

    private void init() {
        textView = (TextView) findViewById(R.id.text_view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    cpuInfoList = getNumCores();
                    String str = "";
                    str += "CPU:" + cpuInfoList.size() + "个 使用率：" + getProcessCpuRate() + "% \n";
                    for (int i = 0; i < cpuInfoList.size(); i++) {
                        CpuInfo tempInfo = cpuInfoList.get(i);
                        str += "CPU" + i + ":\n" +
                                "    Min~Max:" + tempInfo.dealUnit(tempInfo.minFreq) + "~" + tempInfo.getMaxFreqStr() +
                                "   Cur:" + tempInfo.getCurFreqStr() + " \n";
                    }
                    final String result = str;
                    CheckDevActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(result);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //CPU个数
    private List<CpuInfo> getNumCores() {
        List<CpuInfo> tempCpuInfos = new ArrayList<>();
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            for (File f : files) {
                CpuInfo cpuInfo = new CpuInfo();
                LogUtils.i(f.getPath());
                cpuInfo.minFreq = getMinCpuFreq(f.getPath());
                cpuInfo.maxFreq = getMaxCpuFreq(f.getPath());
                cpuInfo.curFreq = getCurCpuFreq(f.getPath());
                tempCpuInfos.add(cpuInfo);
            }

            return tempCpuInfos;
        } catch (Exception e) {
            //Print exception
            Log.d(TAG, "CPU Count: Failed.");
            e.printStackTrace();
            //Default to return 1 core
            return tempCpuInfos;
        }
    }

    //获取最大频率
    public static long getMaxCpuFreq(String path) {
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat", path + "/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);

            Process process = cmd.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String line = reader.readLine();
            return TextUtils.isEmpty(line) ? 0 : Long.valueOf(line);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static long getMinCpuFreq(String path) {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat", path + "/cpufreq/cpuinfo_min_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "0";
        }
        return Long.valueOf(result.trim());
    }

    public static long getCurCpuFreq(String path) {
        String result = "0";
        try {
            FileReader fr = new FileReader(path + "/cpufreq/scaling_cur_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            result = text.trim();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Long.valueOf(result);
    }


    private int getProcessCpuRate() {
        StringBuilder tv = new StringBuilder();
        int rate = 0;
        try {
            String Result;
            Process p;
            p = Runtime.getRuntime().exec("top -n 1");

            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((Result = br.readLine()) != null) {
                if (Result.trim().length() < 1) {
                    continue;
                } else {
                    String[] CPUusr = Result.split("%");
                    tv.append("USER:" + CPUusr[0] + "\n");
                    String[] CPUusage = CPUusr[0].split("User");
                    String[] SYSusage = CPUusr[1].split("System");
                    tv.append("CPU:" + CPUusage[1].trim() + " length:" + CPUusage[1].trim().length() + "\n");
                    tv.append("SYS:" + SYSusage[1].trim() + " length:" + SYSusage[1].trim().length() + "\n");

                    rate = Integer.parseInt(CPUusage[1].trim()) + Integer.parseInt(SYSusage[1].trim());
                    break;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(rate + "");
        return rate;
    }
}
