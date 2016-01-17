package com.teamlinking.timeline.storage;

/**
 * Created by admin on 16/1/13.
 */
public class AvItem {

    private String returnOld;
    private String cmd;
    private String hash;
    private String key;
    private String desc;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getReturnOld() {
        return returnOld;
    }

    public void setReturnOld(String returnOld) {
        this.returnOld = returnOld;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
