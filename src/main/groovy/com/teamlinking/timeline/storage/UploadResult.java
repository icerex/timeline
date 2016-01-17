package com.teamlinking.timeline.storage;

import java.io.Serializable;

/**
 * Created by admin on 16/1/12.
 */
public class UploadResult implements Serializable{
    private static final long serialVersionUID = 2843511874972937926L;
    private String key;
    private String hash;
    private String ext;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
