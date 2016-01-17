package com.icerex.timeline

import com.alibaba.fastjson.JSON
import com.qiniu.http.Response
import com.qiniu.storage.UploadManager
import com.qiniu.util.Auth
import com.qiniu.util.StringMap
import com.teamlinking.timeline.storage.AvItem
import com.teamlinking.timeline.storage.AvThBack
import me.chanjar.weixin.common.bean.result.WxError
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.common.util.http.InputStreamResponseHandler
import me.chanjar.weixin.common.util.http.Utf8ResponseHandler
import org.apache.commons.io.IOUtils
import org.apache.http.Header
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.entity.ContentType
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder

class QiniuController {



    def callback() {
        def jsonReq = request.JSON

        AvThBack avBack = new AvThBack()
        avBack.code = jsonReq.code //
        avBack.id = jsonReq.id
        avBack.inputBucket = jsonReq.inputBucket
        avBack.pipeline = jsonReq.pipeline
        avBack.inputKey = jsonReq.inputKey

        log.info("---> qiniu back --- id:" + avBack.id + "  |  to see op result :" + "http://api.qiniu.com/status/get/prefop?id=" + avBack.id)

        try {
            jsonReq.items.each { it ->
                AvItem item = new AvItem()
                item.cmd = it.cmd
                item.desc = it.desc
                item.hash = it.hash
                item.key = it.key
                item.returnOld = it.returnOld
                avBack.getItems().add(item)
                log.info("save qiniu call back info success......persistant id ->" + avBack.id)
            }
        } catch (Exception e) {
            log.error("---> save qiniu call back request:"+ jsonReq)
            log.error("---> save qiniu call back info failed.......excp:" + e.getMessage(), e)
        }
        def result = [:]
        /**
         * todo 处理回调
         */
        result.status = 1

        withFormat {
            json {
                render text: JSON.toJSONString(result), contentType: 'application/json;', encoding: "UTF-8"
            }
        }
        return
    }

    def index() {
        String fileName = params."fileName" as String
        if (fileName == null){
            fileName = "/Users/admin/Downloads/5a65aee7cf73a60708ca3b0c30ca4372.jpg"
        }

        def result = [:]

        File file = new File(fileName)

        if(file.exists() && file.isFile()){
            Auth auth = Auth.create("EacROcMBQ0iTbhxN4B5q5n4Qtvafx4qelGaFDpm6", "ZfD33BUE3CDP9HcLSulF2hBtW_tiTPWfjOrWnEdi")

            UploadManager uploadManager = new UploadManager()
            def token = auth.uploadToken("teamlinking", null, 3600, new StringMap().putNotEmpty("returnBody", '{"key":$(key),"hash": $(etag), "width": $(imageInfo.width), "height": $(imageInfo.height)}'))
            Response res = uploadManager.put(file,fileName,token)
            result.state = 200
            result.message = res.bodyString()
        }else {
            result.state = 500
            result.message = "Fields is error!"
        }


        withFormat {
            json {
                render text: JSON.toJSONString(result), contentType: 'application/json;', encoding: "UTF-8"
            }
        }
        return
    }

    def mp3() {
        String fileName = params."fileName" as String

        Auth auth = Auth.create("EacROcMBQ0iTbhxN4B5q5n4Qtvafx4qelGaFDpm6", "ZfD33BUE3CDP9HcLSulF2hBtW_tiTPWfjOrWnEdi")

        UploadManager uploadManager = new UploadManager()
        def token = auth.uploadToken("teamlinking", null, 3600, new StringMap().putNotEmpty("returnBody", '{"key":$(key),"hash": $(etag), "avinfo": $(avinfo)}'))

        def result = [:]

        if (fileName.startsWith("http")){
            try{
                HttpGet httpGet = new HttpGet(fileName)
                CloseableHttpClient httpclient = HttpClientBuilder.create().build()
                CloseableHttpResponse response = httpclient.execute(httpGet)
                Header[] contentTypeHeader = response.getHeaders("Content-Type");
                if (contentTypeHeader != null && contentTypeHeader.length > 0) {
                    // 下载媒体文件出错
                    if (ContentType.TEXT_PLAIN.getMimeType().equals(contentTypeHeader[0].getValue())) {
                        String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
                        throw new WxErrorException(WxError.fromJson(responseContent));
                    }
                }
                InputStream inputStream = InputStreamResponseHandler.INSTANCE.handleResponse(response);
                byte[] bytes = IOUtils.toByteArray(inputStream)
                Response res = uploadManager.put(bytes,null,token)

                result.state = 200
                result.message = res.bodyString()
            }catch (Exception e){
                result.state = 500
                result.message = "Fields is error!"
            }
        }else {
            if (fileName == null){
                fileName = "/Users/admin/Downloads/张%E7%A3%8A - %E5%8D%97山%E5%8D%97.mp3"
            }

            File file = new File(fileName)
            if(file.exists() && file.isFile()){
                Response res = uploadManager.put(file,null,token)
                result.state = 200
                result.message = res.bodyString()
            }else {
                result.state = 500
                result.message = "Fields is error!"
            }
        }

        withFormat {
            json {
                render text: JSON.toJSONString(result), contentType: 'application/json;', encoding: "UTF-8"
            }
        }
        return
    }

    def mp3cut(){
        String fileName = params."fileName" as String
        if (fileName == null){
            fileName = "/Users/admin/Downloads/张%E7%A3%8A - %E5%8D%97山%E5%8D%97.mp3"
        }

        def result = [:]

        File file = new File(fileName)

        if(file.exists() && file.isFile()){
            Auth auth = Auth.create("EacROcMBQ0iTbhxN4B5q5n4Qtvafx4qelGaFDpm6", "ZfD33BUE3CDP9HcLSulF2hBtW_tiTPWfjOrWnEdi")

            UploadManager uploadManager = new UploadManager()
            def token = auth.uploadToken("teamlinking", null, 3600, new StringMap().putNotEmpty("returnBody", '{"key":$(key),"hash": $(etag), "avinfo": $(avinfo)}').put("persistentPipeline", "mp3cut").put("persistentOps", "avthumb/m3u8/ab/192k/r/24/segtime/30/vn/1"))
            Response res = uploadManager.put(file,null,token)
            result.state = 200
            result.message = res.bodyString()
        }else {
            result.state = 500
            result.message = "Fields is error!"
        }


        withFormat {
            json {
                render text: JSON.toJSONString(result), contentType: 'application/json;', encoding: "UTF-8"
            }
        }
        return
    }

    def mp4cut(){
        String fileName = params."fileName" as String
        if (fileName == null){
            fileName = "/Users/admin/Downloads/IMG_1127.m4v"
        }

        def result = [:]

        File file = new File(fileName)

        if(file.exists() && file.isFile()){
            Auth auth = Auth.create("EacROcMBQ0iTbhxN4B5q5n4Qtvafx4qelGaFDpm6", "ZfD33BUE3CDP9HcLSulF2hBtW_tiTPWfjOrWnEdi")

            UploadManager uploadManager = new UploadManager()
            def token = auth.uploadToken("teamlinking", null, 3600, new StringMap().putNotEmpty("returnBody", '{"key":$(key),"hash": $(etag), "avinfo": $(avinfo)}').put("persistentPipeline", "mp4cut").put("persistentOps", "avthumb/flv/r/24/vcodec/libx264;avthumb/m3u8/vb/640k/segtime/5"))
            Response res = uploadManager.put(file,fileName,token)
            result.state = 200
            result.message = res.bodyString()
        }else {
            result.state = 500
            result.message = "Fields is error!"
        }


        withFormat {
            json {
                render text: JSON.toJSONString(result), contentType: 'application/json;', encoding: "UTF-8"
            }
        }
        return
    }
}
