import com.teamlinking.timeline.storage.QiniuUpload
import grails.util.Environment
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage
import me.chanjar.weixin.mp.api.WxMpServiceImpl

// Place your Spring DSL code here
beans = {

    switch (Environment.current) {
        case Environment.PRODUCTION:
            wxMpConfigStorage(WxMpInMemoryConfigStorage){
                appId = "wxef4f18e4e555cb98"
                secret = "a5a0562f84205e7b5aef8ab875d2810a"
                token = "098f6bcd4621d373cade4e832627b4f6"
                aesKey = "IlEG9ZQXgDms3RPei76Azfh9pAEX3Hn8xwb1JDkbiY1"
            }
            qiniuUpload(QiniuUpload){
                accessKey = "EacROcMBQ0iTbhxN4B5q5n4Qtvafx4qelGaFDpm6"
                secretKey = "ZfD33BUE3CDP9HcLSulF2hBtW_tiTPWfjOrWnEdi"
                bucket = "teamlinking"
                audioPipeline = "mp3cut"
                vedioPipeline = "mp4cut"
                persistentNotifyUrl = "http://chains.teamlinking.com/api/1.0/qiniu/callback"
            }

            break
        case Environment.DEVELOPMENT:
        case Environment.TEST:
            wxMpConfigStorage(WxMpInMemoryConfigStorage){
                appId = "wx8e20bfc04fbb49cd"
                secret = "d4624c36b6795d1d99dcf0547af5443d"
                token = "098f6bcd4621d373cade4e832627b4f6"
                aesKey = "IlEG9ZQXgDms3RPei76Azfh9pAEX3Hn8xwb1JDkbiY1"
            }
            qiniuUpload(QiniuUpload){
                accessKey = "EacROcMBQ0iTbhxN4B5q5n4Qtvafx4qelGaFDpm6"
                secretKey = "ZfD33BUE3CDP9HcLSulF2hBtW_tiTPWfjOrWnEdi"
                bucket = "teamlinking"
                audioPipeline = "mp3cut"
                vedioPipeline = "mp4cut"
                persistentNotifyUrl = "http://chains.teamlinking.com/api/1.0/qiniu/callback"
            }

            break
    }

    wxMpService(WxMpServiceImpl){
        wxMpConfigStorage = wxMpConfigStorage
    }
}
