package com.teamlinking.timeline.storage;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.persistent.FileRecorder;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by admin on 16/1/12.
 */
public class QiniuUpload implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(QiniuUpload.class);

    private String accessKey;
    private String secretKey;
    //目标空间
    private String bucket;
    //音视频处理回调地址
    private String persistentNotifyUrl;
    private String audioPipeline;
    private String vedioPipeline;
    //设置断点文件保存的位置
    private String pathFile;

    private Auth auth;
    private UploadManager uploadManager;

    public void afterPropertiesSet() throws Exception {
        auth = Auth.create(accessKey,secretKey);
        if (pathFile != null && pathFile.length() > 0){
            uploadManager = new UploadManager(new FileRecorder(pathFile));
        }else {
            uploadManager = new UploadManager();
        }
    }

    //上传图片
    public ImageResult uploadImage(byte[] data,String key){
        String returnBody = "{\"key\":$(key),\"hash\": $(hash), \"width\": $(imageInfo.width), \"height\": $(imageInfo.height)}";
        StringMap policy = new StringMap().putNotEmpty("returnBody", returnBody);
        String token = auth.uploadToken(bucket, null, 60, policy);
        return upload(data,key,token,ImageResult.class);
    }

    //上传音频
    public AvinfoResult uploadAudio(byte[] data, String key){
        String returnBody = "{\"key\":$(key),\"hash\": $(hash), \"duration\": $(avinfo.format.duration), \"bitRate\": $(avinfo.format.bit_rate), \"persistentId\": $(persistentId)}";
        StringMap policy = new StringMap().putNotEmpty("returnBody",returnBody )
                .put("persistentPipeline", audioPipeline)
                .put("persistentOps", "avthumb/m3u8/ab/192k/r/24/segtime/30/vn/1")
                .put("persistentNotifyUrl", persistentNotifyUrl);
        String token = auth.uploadToken(bucket, null, 60, policy);
        return upload(data,key,token,AvinfoResult.class);
    }

    //上传视频
    public AvinfoResult uploadvedio(byte[] data, String key){
        String returnBody = "{\"key\":$(key),\"hash\": $(hash), \"duration\": $(avinfo.format.duration), \"bitRate\": $(avinfo.format.bit_rate), \"persistentId\": $(persistentId)}";
        StringMap policy = new StringMap().putNotEmpty("returnBody",returnBody )
                .put("persistentPipeline", vedioPipeline)
                .put("persistentOps", "avthumb/m3u8/vb/640k/segtime/5")
                .put("persistentNotifyUrl", persistentNotifyUrl);
        String token = auth.uploadToken(bucket, null, 60, policy);
        return upload(data,key,token,AvinfoResult.class);
    }

    //从内存中上传
    public <T> T upload(byte[] data, String key, String upToken,Class<T> classOfT){
        try {
            Response res = uploadManager.put(data, key, upToken);
            logger.info(res.toString());
            if(res.isOK()){
                return res.jsonToObject(classOfT);
            }
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时简单状态信息
            logger.error(r.toString());
            try {
                // 响应的文本信息
                logger.error(r.bodyString());
            } catch (QiniuException e1) {
            }

        }
        return null;
    }

    public Auth getAuth(){
        return auth;
    }

    public UploadManager getUploadManager() {
        return uploadManager;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPersistentNotifyUrl() {
        return persistentNotifyUrl;
    }

    public void setPersistentNotifyUrl(String persistentNotifyUrl) {
        this.persistentNotifyUrl = persistentNotifyUrl;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    public String getAudioPipeline() {
        return audioPipeline;
    }

    public void setAudioPipeline(String audioPipeline) {
        this.audioPipeline = audioPipeline;
    }

    public String getVedioPipeline() {
        return vedioPipeline;
    }

    public void setVedioPipeline(String vedioPipeline) {
        this.vedioPipeline = vedioPipeline;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
