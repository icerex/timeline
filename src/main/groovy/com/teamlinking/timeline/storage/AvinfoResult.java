package com.teamlinking.timeline.storage;

/**
 * Created by admin on 16/1/12.
 */
public class AvinfoResult extends UploadResult{

    private Double duration;
    private Long bitRate;
    private String persistentId;

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Long getBitRate() {
        return bitRate;
    }

    public void setBitRate(Long bitRate) {
        this.bitRate = bitRate;
    }

    public String getPersistentId() {
        return persistentId;
    }

    public void setPersistentId(String persistentId) {
        this.persistentId = persistentId;
    }
}
