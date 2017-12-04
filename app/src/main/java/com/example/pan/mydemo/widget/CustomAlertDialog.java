package com.example.pan.mydemo.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pan.mydemo.R;

/**
 *
 *单选列表
 *CustomAlertDialog customAlertDialog = new CustomAlertDialog(context);
 *String[] StrLists = {"修改","删除","确定","删除","确定","删除","确定","删除","确定","删除"};  //最多10项
 *customAlertDialog.setItemSingleClick(StrLists, new CustomAlertDialog.CustomAlertDialogItemClickListener() {
 *   @Override
 *   public boolean itemclick(View view, int position, long id) {
 *       // TODO Auto-generated method stub
 *       return false;//实现的接口: 返回true 则不关闭dialog
 *   }
 *});
 *
 * Created by Pan on 2016/5/26.
 */
public class CustomAlertDialog {

    private Context mContext;
    private AlertDialog alertDialog;
    private Window window;
    private TextView title;
    private TextView message;

    private LinearLayout dialog_container;

    private ListView listView;
    private LayoutInflater flater;

    private CheckBox checkBox;

    private LinearLayout multiBtnLayout;
    private Button positiveBtn,negativeBtn;

    private Button comfirmBtn;

    private LinearLayout comfirmBtnMark;

    private ImageView login_wait_image;



    public CustomAlertDialog(Context context, boolean cancelAble ){
        mContext = context;
        flater = LayoutInflater.from(context);
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCanceledOnTouchOutside(cancelAble);
        alertDialog.setCancelable(cancelAble);
        if( !alertDialog.isShowing() ) {
            alertDialog.show();
        }
        initDefaultWedget();
    }


    public CustomAlertDialog(Context context) {
        mContext = context;
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);
        if( !alertDialog.isShowing() ) {
            alertDialog.show();
        }
        initDefaultWedget();
    }

    private void initDefaultWedget(){
        window = alertDialog.getWindow();
        window.setContentView(R.layout.custom_alertdialog_default);
        title = (TextView)window.findViewById(R.id.custom_alert_dialog_title);
        message = (TextView)window.findViewById(R.id.custom_alert_dialog_message);
        dialog_container= (LinearLayout) window.findViewById(R.id.custom_alert_dialog_content_container);

        checkBox = (CheckBox)window.findViewById(R.id.custom_alert_dialog_checkBox);

        multiBtnLayout = (LinearLayout)window.findViewById(R.id.custom_alert_dialog_multi_btn_layout);

        positiveBtn = (Button)window.findViewById(R.id.custom_alert_dialog_positive_btn);
        negativeBtn = (Button)window.findViewById(R.id.custom_alert_dialog_negative_btn);


        comfirmBtn = (Button) window.findViewById(R.id.custom_alert_dialog_comfirm_btn);
        comfirmBtnMark = (LinearLayout)window.findViewById(R.id.custom_alert_dialog_comfirm_btn_mark);
        login_wait_image = (ImageView)window.findViewById(R.id.custom_alert_dialog_login_wait_image);


    }

    public void setTitle(String titleText ){
        if(!TextUtils.isEmpty(titleText) ) {
            this.title.setVisibility(View.VISIBLE);
            this.title.setText(titleText);
        }else{
            this.title.setVisibility(View.GONE);
        }
    }



    public void setMessage(String text ){

        if(!TextUtils.isEmpty(text) ) {
            this.message.setVisibility(View.VISIBLE);
            this.message.setText(text);
        }else{
            this.message.setVisibility(View.GONE);
        }
    }

    public void addContentView( View content ){
        dialog_container.setVisibility(View.VISIBLE);
        dialog_container.addView(content);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    /**
     * 单选列表
     * @param textArr
     * @param alertDialogItemClickListener
     */
    public void setItemSingleClick( String[] textArr,final CustomAlertDialogItemClickListener alertDialogItemClickListener){
        if( listView == null ){
            initSingleWegget();
        }
        if( textArr != null && textArr.length > 0 ){
            SingleAdapter singleAdapter = new SingleAdapter(mContext, R.layout.custom_alertdialog_singleclick_item,textArr);
            if( listView != null ){
                listView.setAdapter(singleAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        if(	!alertDialogItemClickListener.itemclick(view, position, id) ){
                            dismiss();
                        }
                    }
                });
            }
        }
    }

    class SingleAdapter extends ArrayAdapter<String> {

        private int resourceId;

        public SingleAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            this.resourceId = resource;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String item = getItem(position);
            convertView = flater.inflate(resourceId, null);
            TextView itemText = (TextView)convertView.findViewById(R.id.itemText);
            itemText.setText(item);
            return convertView;
        }
    }

    private void initSingleWegget(){
        window.setContentView(R.layout.custom_alertdialog_singleclick);
        listView = (ListView)window.findViewById(R.id.singleListView);
    }


    public void setCheckBox( boolean state ){
        if( state ){
            checkBox.setVisibility(View.VISIBLE);
        }else{
            checkBox.setChecked(false);
            checkBox.setVisibility(View.GONE);
        }
    }



    public void setButtonLoading(){
        if( alertDialog.isShowing() ) {
            comfirmBtn.setVisibility(View.GONE);
            comfirmBtnMark.setVisibility(View.VISIBLE);
            AnimationDrawable ad = (AnimationDrawable) login_wait_image.getDrawable();
            ad.start();
        }
    }

    public void setButtonLoadFinished(){
        if( alertDialog.isShowing() ) {
            comfirmBtn.setVisibility(View.VISIBLE);
            comfirmBtnMark.setVisibility(View.GONE);
        }
    }


    public CustomAlertDialog setButton( String text,final CustomAlertDialogClickListener alertDialogInterface ){
        comfirmBtn.setVisibility(View.VISIBLE);
        if( text != null ){
            comfirmBtn.setText(text);
        }
        /**
         * 实现的接口 返回true 则不关闭dialog
         */
        comfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialogInterface == null || !alertDialogInterface.onclick(v,checkBox.isChecked())) {
                    dismiss();
                }
            }
        });
        return this;
    }

    public CustomAlertDialog setButtonLeft(String text, final CustomAlertDialogClickListener alertDialogInterface ){
        multiBtnLayout.setVisibility(View.VISIBLE);
        if( text != null ){
            positiveBtn.setText(text);
        }
        /**
         * 实现的接口 返回true 则不关闭dialog
         */
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( alertDialogInterface == null || !alertDialogInterface.onclick(v,checkBox.isChecked()) ){
                    dismiss();
                }
            }
        });
        return this;
    }

    public CustomAlertDialog setButtonRight(String text, final CustomAlertDialogClickListener alertDialogInterface ){
        multiBtnLayout.setVisibility(View.VISIBLE);
        if( text != null ){
            negativeBtn.setText(text);
        }
        /**
         * 实现的接口 返回true 则不关闭dialog
         */
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( alertDialogInterface == null || !alertDialogInterface.onclick(v,checkBox.isChecked()) ){
                    dismiss();
                }
            }
        });
        return this;
    }

    public void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        alertDialog.setOnKeyListener(onKeyListener);
    }

    public void dismiss(){
        if( alertDialog.isShowing() ){
            alertDialog.dismiss();
        }
    }


    public interface CustomAlertDialogClickListener{
        public boolean onclick(View v, boolean checkState);
    }


    public interface CustomAlertDialogItemClickListener{
        public boolean itemclick(View view,int position, long id);
    }
}
