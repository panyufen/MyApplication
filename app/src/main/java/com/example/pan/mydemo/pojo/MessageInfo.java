package com.example.pan.mydemo.pojo;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by PAN on 2017/12/13.
 */
@Entity
public class MessageInfo {
    @Id
    @Index
    String id;
    String title;
    String content;
    String is_read;
    @Generated(hash = 349174049)
    public MessageInfo(String id, String title, String content, String is_read) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.is_read = is_read;
    }
    @Generated(hash = 1292770546)
    public MessageInfo() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getIs_read() {
        return this.is_read;
    }
    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }
}
