package com.example.pan.mydemo.view.database;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.cus.pan.library.utils.LogUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.db.greendao.GreenHelper;
import com.example.pan.mydemo.db.natives.NUserInfoDao;
import com.example.pan.mydemo.pojo.UserInfo;
import com.example.pan.mydemo.view.base.BaseLayoutActivity;
import com.google.gson.Gson;
import com.qiufeng.greendao.greendao.gen.UserInfoDao;

import java.util.List;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class DataBaseActivity extends BaseLayoutActivity {

    @BindView(R.id.database_type)
    TextView databaseType;
    @BindView(R.id.batabase_switch)
    SwitchCompat batabaseSwitch;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_age)
    EditText etAge;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.button_insert)
    Button buttonInsert;
    @BindView(R.id.button_query)
    Button buttonQuery;
    @BindView(R.id.button_edit)
    Button buttonEdit;
    @BindView(R.id.button_delete)
    Button buttonDelete;

    //    private DBHelper dbHelper;
    private NUserInfoDao nUserInfoDao;
    private UserInfoDao userInfoDao;

    private boolean isGreen = false;

    private UserInfo currUserinfo;

    private List<UserInfo> userInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);
        nUserInfoDao = new NUserInfoDao();
        userInfoDao = GreenHelper.getInstance().getDaoSession().getUserInfoDao();
    }


    @OnCheckedChanged({R.id.batabase_switch})
    public void onViewCheckChanged(CompoundButton v, boolean checked) {
        if (checked) { // greendao
            isGreen = true;
        } else {      //native
            isGreen = false;
        }
    }

    @OnClick({R.id.button_insert, R.id.button_query, R.id.button_edit, R.id.button_delete})
    public void onViewClicked(View view) {
        UserInfo userInfo = new UserInfo();
        switch (view.getId()) {
            case R.id.button_insert:
                userInfo.setName(etName.getText().toString());
                userInfo.setAge(etAge.getText().toString());
                userInfo.setMobile(etMobile.getText().toString());
                userInfo.setAddress(etAddress.getText().toString());
                if (isGreen) {
                    userInfoDao.insert(userInfo);
                } else {
                    nUserInfoDao.insert(userInfo);
                }
                break;
            case R.id.button_query:
                if (isGreen) {
                    userInfos = userInfoDao.loadAll();

                } else {
                    userInfos = nUserInfoDao.query(userInfo, null);
                }
                LogUtils.i("query " + new Gson().toJson(userInfos));
                break;
            case R.id.button_edit:

//                userInfo.setName(etName.getText().toString());
//                userInfo.setAge(etAge.getText().toString());
//                userInfo.setMobile(etMobile.getText().toString());
//                userInfo.setAddress(etAddress.getText().toString());
//                if (isGreen) {
//                    userInfoDao.insert(userInfo);
//                } else {
//                    databaseDao.insert(userInfo);
//                }
                break;
            case R.id.button_delete:

                break;
        }
    }
}
