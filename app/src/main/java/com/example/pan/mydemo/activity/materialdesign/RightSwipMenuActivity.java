package com.example.pan.mydemo.activity.materialdesign;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.cus.pan.library.utils.ContactsTools;
import com.cus.pan.library.utils.LogUtils;
import com.cus.pan.library.utils.PatternUtils;
import com.example.pan.mydemo.R;
import com.example.pan.mydemo.activity.base.BaseActivity;
import com.example.pan.mydemo.adapter.DividerItemDecoration;
import com.example.pan.mydemo.view.AutoLinearLayout;
import com.example.pan.mydemo.view.AutoScrollRecyclerView;
import com.example.pan.mydemo.view.RightSwipMenuLayout;
import com.example.pan.mydemo.view.SideBar;
import com.google.gson.Gson;
import com.pan.skin.loader.load.SkinManager;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by PAN on 2017-3-28 10:27:25 .
 */
public class RightSwipMenuActivity extends BaseActivity implements RightSwipMenuLayout.OnSwipMenuListener {

    private Toolbar mToolbar;

    private RightSwipMenuLayout mRightSwipMenuLayout;

    private AutoScrollRecyclerView mRecyclerView;
    private ContactRecycleAdapter mAdapter;

    private SideBar mSideBar;
    private AutoLinearLayout mAutoLinearLayout;

    List<Contact> mContacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right_swip_menu);
        initView();
        initData();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);

        mRightSwipMenuLayout = (RightSwipMenuLayout) findViewById(R.id.right_swip_menu_layout);
        mRightSwipMenuLayout.setOnSwipMenuListener(this);

        mRecyclerView = (AutoScrollRecyclerView) findViewById(R.id.recycler_view);
        int dividerColor = SkinManager.getInstance().getColor(R.color.normal_text_color);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST, dividerColor));
        mRecyclerView.setAdapter(mAdapter = new ContactRecycleAdapter());

        mSideBar = (SideBar) findViewById(R.id.side_bar);
        mSideBar.setListView(mRecyclerView);
        mSideBar.setRightSwipMenuLayout(mRightSwipMenuLayout);
        mAutoLinearLayout = (AutoLinearLayout) findViewById(R.id.auto_line_layout);
    }


    private void initData() {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        ContactsTools contactsTools = new ContactsTools(this);
        String contactsStr = contactsTools.getPhoneContacts();
        LogUtils.i("contactStr " + contactsStr);
        if (TextUtils.isEmpty(contactsStr)) return;
        String[] contactStrList = contactsStr.split(";");
        StringBuilder stringBuilder = new StringBuilder();
        for (String contact : contactStrList) {
//            LogUtils.i("single str " + contact);
            String[] tempPerson = contact.split(",");
            Contact tempContact = new Contact();
            if (PatternUtils.isChineseAll(tempPerson[4].replace(" ", ""))) {
                try {
                    String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(tempPerson[4].charAt(0), format);
                    tempContact.index = pinyins[0].substring(0, 1).toUpperCase();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                tempContact.index = tempPerson[4].substring(0, 1).toUpperCase();
            }
            if (!TextUtils.isEmpty(tempContact.index) && PatternUtils.isPinyinFirst(tempContact.index) && stringBuilder.indexOf(tempContact.index) < 0) {
                stringBuilder.append(tempContact.index);
            }
//            tempContact.name = tempPerson[1];
//            tempContact.tel = tempPerson[0];
            tempContact.name = tempPerson[1].substring(0, 1) + "**";
            tempContact.tel = tempPerson[0].substring(0, 3) + "********";
            mContacts.add(tempContact);
        }
        LogUtils.i("contacts " + new Gson().toJson(mContacts));
        mAdapter.notifyDataSetChanged();
        char[] indexArr = stringBuilder.toString().toCharArray();
        LogUtils.i("indexStr " + indexArr.toString());
        Arrays.sort(indexArr);
        mSideBar.setListIndexData(indexArr);
        addIndexTextView(indexArr);
    }


    class ContactRecycleAdapter extends RecyclerView.Adapter<ContactRecycleAdapter.MyViewHolder> implements SectionIndexer {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(RightSwipMenuActivity.this).inflate(R.layout.contact_list_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Contact contact = mContacts.get(position);
            holder.tv.setText(contact.name + " " + contact.tel);
//            dynamicAddSkinEnableView(holder.tv, "textColor", R.color.normal_text_color);
//            holder.tv.setTextColor(Color.RED);
        }

        @Override
        public int getItemCount() {
            return mContacts.size();
        }


        @Override
        public Object[] getSections() {
            return new Object[0];
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            LogUtils.i("section Index " + sectionIndex);
            for (int i = 0; i < mContacts.size(); i++) {
                Contact c = mContacts.get(i);
                if ((c.index.charAt(0) == sectionIndex)) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public int getSectionForPosition(int position) {
            LogUtils.i("section position " + position);
            return 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv;
            LinearLayout linearLayout;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.contact_list_item_name);
                dynamicAddSkinEnableView(tv, "textColor", R.color.normal_text_color);

                linearLayout = (LinearLayout) view.findViewById(R.id.linear_layout);
                dynamicAddSkinEnableView(linearLayout, "background", R.drawable.item_selector);
            }
        }

    }

    public class Contact {
        public String index;
        public String name;
        public String tel;
    }

    @Override
    public void onShow() {
        mSideBar.setTouchEnable(false);
    }

    @Override
    public void onChanged() {
//        LogUtils.i("onChange "+mRightSwipMenuLayout.getMenuIsClosed());
        mSideBar.setTouchEnable(mRightSwipMenuLayout.getMenuIsClosed());
    }

    @Override
    public void onHide() {
        mSideBar.setTouchEnable(true);
    }

    private void addIndexTextView(char[] cArr) {
        mAutoLinearLayout.setColumns(2, 1);
        for (final char c : cArr) {
            TextView textView = new TextView(this);
            textView.setText(String.valueOf(c));
            textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.layout_none_blue_selector));
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRightSwipMenuLayout.hideMenu();
                    for (int i = 0; i < mContacts.size(); i++) {
                        Contact ci = mContacts.get(i);
                        if ((ci.index.charAt(0) == c)) {
                            mRecyclerView.scrollToPosition(i);
                        }
                    }
                }
            });
            dynamicAddSkinEnableView(textView, "textColor", R.color.normal_text_color);
            mAutoLinearLayout.addView(textView);
        }
    }

}
