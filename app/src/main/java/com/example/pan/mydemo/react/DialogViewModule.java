package com.example.pan.mydemo.react;



/**
 * Created by PAN on 2016/8/18.
 */
//public class DialogViewModule extends ReactContextBaseJavaModule implements DialogView.DialogItemClick {
//
//    Dialog alertDialog;
//
//    Callback callback;
//
//    public DialogViewModule(ReactApplicationContext reactContext) {
//        super(reactContext);
//    }
//
//    @Override
//    public String getName() {
//        return "PTDialog";
//    }
//
//
//    @ReactMethod
//    public void show(ReadableArray readableArray, Callback callback) {
//        this.callback = callback;
//        Log.i("readableArray","readableArray "+readableArray.size());
//        List<DialogView.carData> carDatas = new ArrayList<>();
//        for( int i=0;i<readableArray.size();i++ ){
//            ReadableMap readableMap = readableArray.getMap(i);
//            String carBrand = readableMap.getString("carBrand");
//            String carSeries = readableMap.getString("carSeries");
//            String carNumber = readableMap.getString("carNumber");
//
//            carDatas.add(new DialogView.carData(carBrand, carSeries, carNumber));
//        }
//
//
//        Activity activity = getCurrentActivity();
//        DialogView dialogView = new DialogView(activity);
//        alertDialog = dialogView.create(carDatas,this);
//    }
//
//
//    @Override
//    public void onItemclick(int index) {
//        Log.i("onitemclick ","aajljdflajslf   onitemclick "+index);
//        callback.invoke(index);
//    }
//
//}
