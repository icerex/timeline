package com.teamlinking.timeline.storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 16/1/13.
 */
public class AvThBack {
    private String pipeline;
    private String code;
    private String id;
    private String inputKey;
    private String inputBucket;
    private List<AvItem> items;

    public AvThBack(){
        items = new ArrayList<AvItem>();
    }

    public String getPipeline() {
        return pipeline;
    }

    public void setPipeline(String pipeline) {
        this.pipeline = pipeline;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInputKey() {
        return inputKey;
    }

    public void setInputKey(String inputKey) {
        this.inputKey = inputKey;
    }

    public String getInputBucket() {
        return inputBucket;
    }

    public void setInputBucket(String inputBucket) {
        this.inputBucket = inputBucket;
    }

    public List<AvItem> getItems() {
        return items;
    }

    public void setItems(List<AvItem> items) {
        this.items = items;
    }
}

