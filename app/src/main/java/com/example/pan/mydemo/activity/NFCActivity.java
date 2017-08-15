package com.example.pan.mydemo.activity;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;
import android.widget.Toast;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NFCActivity extends BaseActivity {

    private NfcAdapter nfcAdapter;

    private String readResult;

    private Tag tagFromIntent;

    @BindView(R.id.nfc_text_view)
    TextView mNfcTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        setSupportActionBar(R.id.tool_bar);
        ButterKnife.bind(this);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            mNfcTextView.setText("设备不支持NFC！");
        }
        if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
            mNfcTextView.setText("请在系统设置中先启用NFC功能！");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            LogUtils.i("ACTION_NDEF_DISCOVERED");
            if (readFromTag(getIntent())) {
                LogUtils.i(readResult);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())) {
            LogUtils.i("ACTION_TECH_DISCOVERED");
            processIntent(this.getIntent());
        }
    }

    private boolean readFromTag(Intent intent) {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
        NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
        try {
            if (mNdefRecord != null) {
                readResult = new String(mNdefRecord.getPayload(), "UTF-8");
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Parses the NDEF Message from the intent and prints to the TextView
     */
    public void processIntent(Intent intent) {

        // 取出封装在intent中的TAG
        tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        String metaInfo = "";
        metaInfo += "卡片ID：" + bytesToHexString(tagFromIntent.getId());
        Toast.makeText(this, "找到卡片 " + metaInfo, Toast.LENGTH_SHORT).show();

        // Tech List
        String prefix = "android.nfc.tech.";
        String[] techList = tagFromIntent.getTechList();

        //分析NFC卡的类型：
        String CardType = "\n卡片类型包含：\n";
        String tagInfo = "";
        for (int i = 0; i < techList.length; i++) {
            CardType += techList[i] + "\n";
            if (techList[i].equals(NfcA.class.getName())) {
                LogUtils.i("readTag read NfcA");
                NfcA nfca = NfcA.get(tagFromIntent);
                tagInfo += readNfcaTag(nfca);

            } else if (techList[i].equals(MifareClassic.class.getName())) {
                LogUtils.i("readTag read MifareClassic");
                MifareClassic mifareClassic = MifareClassic.get(tagFromIntent);
                tagInfo += readMifareClassicTag(mifareClassic);

            } else if (techList[i].equals(NdefFormatable.class.getName())) {
                LogUtils.i("readTag read NdefFormatable");
                NdefFormatable ndefFormatable = NdefFormatable.get(tagFromIntent);


            }
        }
        metaInfo += CardType;
        metaInfo += tagInfo;
        mNfcTextView.setText(metaInfo);
    }

    private String readNfcaTag(NfcA nfca) {
        String readStr = "类型：Nfca \n";
        try {
            nfca.connect();
            if (nfca.isConnected()) {
                readStr += "Atqa:" + bytesToHexString(nfca.getAtqa()) + "\n";
                readStr += "Sak:" + nfca.getSak() + "\n";
                readStr += "MaxTransceiveLength:" + nfca.getMaxTransceiveLength() + "\n";
                readStr += "Response:";
                byte[] SELECT = {(byte) 0x30, (byte) 5 & 0x0ff,};
                byte[] response = nfca.transceive(SELECT);
                if (response != null) {
                    readStr += bytesToHexString(response) + "\n";
                } else {
                    readStr += "null\n";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            readStr += "null\n";
            return readStr;
        } finally {
            if (nfca != null) {
                try {
                    nfca.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return readStr;
    }


    // //读取数据
    public String readMifareClassicTag(MifareClassic mfc) {
        int bIndex;
        int bCount;
        boolean auth = false;
        // 读取TAG
        try {
            StringBuilder metaInfo = new StringBuilder();
            mfc.connect();
            int type = mfc.getType();// 获取TAG的类型
            int sectorCount = mfc.getSectorCount();// 获取TAG中包含的扇区数
            String typeS = "";
            switch (type) {
                case MifareClassic.TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic.TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic.TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic.TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;

            }
            metaInfo.append("类型：" + typeS + "\n共" + sectorCount + "个扇区\n共"
                    + mfc.getBlockCount() + "个块\n存储空间: " + mfc.getSize() + "B\n");
            for (int j = 0; j < sectorCount; j++) {
                // Authenticate a sector with key A.
                auth = mfc.authenticateSectorWithKeyA(j, MifareClassic.KEY_DEFAULT);// 逐个获取密码
                if (auth) {
                    metaInfo.append("Sector " + j + ":验证成功\n");
                    // 读取扇区中的块
                    bCount = mfc.getBlockCountInSector(j);
                    bIndex = mfc.sectorToBlock(j);
                    for (int i = 0; i < bCount; i++) {
                        byte[] data = mfc.readBlock(bIndex);
                        metaInfo.append("Block " + bIndex + " : " + bytesToHexString(data) + "\n");
                        bIndex++;
                    }
                } else {
                    metaInfo.append("Sector " + j + ":验证失败\n");
                }
            }
            return metaInfo.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mfc != null) {
                try {
                    mfc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }

    // 字符序列转换为16进制字符串
    private String bytesToHexString(byte[] src) {
        return bytesToHexString(src, false);
    }

    private String bytesToHexString(byte[] src, boolean isPrefix) {
        StringBuilder stringBuilder = new StringBuilder();
        if (isPrefix) {
            stringBuilder.append("0x");
        }
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }
}
