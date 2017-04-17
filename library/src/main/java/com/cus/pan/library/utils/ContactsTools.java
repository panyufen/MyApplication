package com.cus.pan.library.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;

import java.io.InputStream;

public class ContactsTools {
    private Context context;
    ContentResolver cr;

    public ContactsTools(Context context) {
        this.context = context;
        cr = context.getContentResolver();
    }

    public String getPhoneContacts() {
        String contacts;
        StringBuilder tempContacts = new StringBuilder();
        int nameIndex = -1;
        String[] PHONES_PROJECTION = new String[]{
                Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID, "sort_key"};

        Cursor cur = cr.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);
        if (cur == null) return "";
        while (cur.moveToNext()) {
            String number;
            //得到名字
            nameIndex = cur.getColumnIndex(Phone.DISPLAY_NAME);
            String name = cur.getString(nameIndex);

            //得到电话号码
            Long contactId = cur.getLong(cur.getColumnIndex(Phone.CONTACT_ID)); // 获取联系人的ID号，在SQLite中的数据库ID
            String sortKey = cur.getString(4);
            number = cur.getString(cur.getColumnIndex(Phone.NUMBER));
            if (number.startsWith("+86")) {
                number = number.substring(3);
            }
            //获得联系人头像ID
            int photoIndex = cur.getInt(cur.getColumnIndex(Phone.PHOTO_ID));

            tempContacts.append(number).append(",");
            tempContacts.append(name).append(",");
            tempContacts.append(contactId).append(",");
            tempContacts.append(photoIndex).append(",");
            tempContacts.append(sortKey).append(";");
        }
        contacts = tempContacts.toString();
        cur.close();
        return contacts;
    }


    public Bitmap getContactPhoto(int photoIndex, Long contactId, int ResDrawable) {
        Bitmap contactPhoto = null;
        if (photoIndex > 0) {
            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
            contactPhoto = BitmapFactory.decodeStream(input);
        } else {
            contactPhoto = BitmapFactory.decodeResource(context.getResources(), ResDrawable);
        }
        return contactPhoto;
    }


}
