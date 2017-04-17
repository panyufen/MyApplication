package com.example.pan.mydemo.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.pan.mydemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PAN on 2016/8/18.
 */
public class DialogView {

    AlertDialog alertDialog;

    Context mContext;

    public DialogView(Context context) {
        this.mContext = context;
    }

    public Dialog create(List<carData> carDatas,final DialogItemClick dialogItemClick){
        List<PTButtonList.ButtonPack> packs = new ArrayList<PTButtonList.ButtonPack>();

        if( carDatas.size() == 0 ){
            carDatas.add(new carData("a "," b"," asdff"));
            carDatas.add(new carData("a "," b"," asdff"));
            carDatas.add(new carData("a "," b"," asdff"));
            carDatas.add(new carData("a "," b"," asdff"));
        }

        for (carData ccd : carDatas) {
            PTButtonList.ButtonPack buttonPack = new PTButtonList.ButtonPack(ccd.carBrand + ccd.carSeries + " " + ccd.carNumber);
            buttonPack.setSource(ccd);
            packs.add(buttonPack);
        }
        return showListDialog(packs, new PTButtonList.OnClickButtonListener() {
            @Override
            public void onClick(PTButtonList.ButtonPack buttonPack, int index) {
                onCancel();
                dialogItemClick.onItemclick(index);
            }

            @Override
            public void onCancel() {
                alertDialog.dismiss();
            }
        });

    }
    private Dialog showListDialog(List<PTButtonList.ButtonPack> packs, PTButtonList.OnClickButtonListener listener) {
        Log.i("showListDialog","activity "+mContext);
        View popupView = PTButtonList.create(mContext, packs, listener);
        return showAlertDialogBottom(popupView);
    }
    protected Dialog showAlertDialogBottom(View customView) {
        return showAlertDialog(customView, Gravity.BOTTOM, R.drawable.dialog_background);
    }

    protected Dialog showAlertDialog(View customView, int gravity, int backgroundDrawable) {
        return showAlertDialog(customView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, gravity, backgroundDrawable);
    }
    protected Dialog showAlertDialog(View customView, int width, int height, int gravity, int backgroundDrawable) {
        return showAlertDialog(customView, width, height, gravity, backgroundDrawable,true);
    }

    protected Dialog showAlertDialog(View customView, int width, int height, int gravity, int backgroundDrawable,boolean cancelAble) {
        if( alertDialog != null && alertDialog.isShowing() ) {
            alertDialog.dismiss();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(cancelAble);

        Window window = alertDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = width;
        layoutParams.height = height;
        window.setAttributes(layoutParams);
        window.setGravity(gravity);
        alertDialog.setContentView(customView);
        window.setWindowAnimations(R.style.SlideOutAlertDialogFromBottom);  //添加动画
        if (backgroundDrawable > 0) {
            window.setBackgroundDrawableResource(backgroundDrawable);
        }

//        Window window = dialog.getWindow();
//        WindowManager.LayoutParams params = window.getAttributes();
//        params.width = width;
//        params.height = height;
//        window.setAttributes(params);
//        window.setGravity(gravity);  //此处可以设置dialog显示的位置
//        dialog.setContentView(customView);
//        window.setWindowAnimations(R.style.SlideOutAlertDialogFromBottom);  //添加动画
//
//        if (backgroundDrawable > 0) {
//            window.setBackgroundDrawableResource(backgroundDrawable);
//        }
//        // 解决alertDialog中有EditText时不能输入的问题
//        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return alertDialog;
    }

    public static class carData{

        public carData(String a,String b,String c){
            this.carBrand = a;
            this.carSeries = b;
            this.carNumber = c;
        }

        public String carBrand;

        public String carSeries;

        public String carNumber;

    }

    public interface DialogItemClick{

        public void onItemclick(int index);
    }


}
