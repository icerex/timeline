package com.teamlinking

import com.teamlinking.timeline.common.Constans
import com.teamlinking.timeline.wechat.MessageRouterService
import me.chanjar.weixin.common.bean.WxMenu
import me.chanjar.weixin.mp.api.WxMpConfigStorage
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage
import org.apache.commons.lang.StringUtils

class WechatController {

    WxMpService wxMpService
    WxMpConfigStorage wxMpConfigStorage
    MessageRouterService messageRouterService

    def callback(){
        String signature = params."signature" as String
        String nonce = params."nonce" as String
        String timestamp = params."timestamp" as String

        def result = null

        if (StringUtils.isEmpty(signature) || StringUtils.isEmpty(nonce) || StringUtils.isEmpty(timestamp) ){
            result = "Parameter Error"
        }else if (wxMpService.checkSignature(timestamp, nonce, signature)) {
            String echostr = params."echostr" as String
            if (StringUtils.isEmpty(echostr) ){
                String encryptType = params."encrypt_type" as String
                if (StringUtils.isEmpty(encryptType)){
                    encryptType = "raw"
                }
                if ("raw".equals(encryptType)){
                    // 明文传输的消息
                    WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream())
                    WxMpXmlOutMessage outMessage = messageRouterService.route(inMessage)
                    result = outMessage.toXml()
                }else if ("aes".equals(encryptType)){
                    // 是aes加密的消息
                    String msgSignature = params."msg_signature" as String
                    if (StringUtils.isEmpty(msgSignature)){
                        result = "Parameter Error"
                    }else {
                        WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), wxMpConfigStorage, timestamp, nonce, msgSignature)
                        WxMpXmlOutMessage outMessage = messageRouterService.route(inMessage)
                        result = outMessage.toXml()
                    }
                }else {
                    result = "Non identifiable encryption type"
                }
            }else {
                result = echostr
            }
        }else {
            result = "Illegal Request"
        }

        withFormat {
            json {
                render text: result, contentType: 'text/xml;', encoding: "UTF-8"
            }
        }
    }

    def signature() {
        String url = params."url" as String

        def sign = wxMpService.createJsapiSignature(url)

        def result = [:]
        result.put("appId", sign.appid)
        result.put("timestamp", sign.timestamp)
        result.put("nonceStr", sign.noncestr)
        result.put("signature", sign.signature)

        withFormat {
            json {
                render text: result, contentType: 'application/json;', encoding: "UTF-8"
            }
        }
    }

    def createMenu(){
        wxMpService.menuDelete()
        String json = '{' +
                '  "menu": {' +
                '    "button": [' +
                '      {' +
                '        "type": "click",' +
                '        "name": "操作",' +
                '        "sub_button": [' +
                '          {' +
                '            "type": "click",' +
                '            "name": "'+Constans.WechatMenu.currentStory.name+'",' +
                '            "key": "'+ Constans.WechatMenu.currentStory.key + '"' +
                '          },' +
                '          {' +
                '            "type": "click",' +
                '            "name": "'+Constans.WechatMenu.updateStory.name+'",' +
                '            "key": "'+ Constans.WechatMenu.updateStory.key + '"' +
                '          },' +
                '          {' +
                '            "type": "click",' +
                '            "name": "'+Constans.WechatMenu.addStory.name+'",' +
                '            "key": "'+ Constans.WechatMenu.addStory.key + '"' +
                '          },' +
                '          {' +
                '            "type": "click",' +
                '            "name": "'+Constans.WechatMenu.addSubStory.name+'",' +
                '            "key": "'+ Constans.WechatMenu.addSubStory.key + '"' +
                '          },' +
                '          {' +
                '            "type": "click",' +
                '            "name": "'+Constans.WechatMenu.nextStory.name+'",' +
                '            "key": "'+ Constans.WechatMenu.nextStory.key + '"' +
                '          },' +
                '          {' +
                '            "type": "click",' +
                '            "name": "'+Constans.WechatMenu.backParent.name+'",' +
                '            "key": "'+ Constans.WechatMenu.backParent.key + '"' +
                '          },' +
                '          {' +
                '            "type": "click",' +
                '            "name": "'+Constans.WechatMenu.undo.name+'",' +
                '            "key": "'+ Constans.WechatMenu.undo.key + '"' +
                '          }' +
                '        ]' +
                '      },' +
                '      {' +
                '        "name": "查看",' +
                '        "sub_button": [' +
                '          {' +
                '            "type": "view",' +
                '            "name": "当前主题",' +
                '            "url": "http://chains.teamlinking.com/1/story/current"' +
                '          },' +
                '          {' +
                '            "type": "view",' +
                '            "name": "个人中心",' +
                '            "url": "http://chains.teamlinking.com/1/user"' +
                '          }' +
                '        ]' +
                '      }' +
                '    ]' +
                '  }' +
                '}'
        WxMenu wxMenu = WxMenu.fromJson(json)
        wxMpService.menuCreate(wxMenu)

        withFormat {
            json {
                render text: "Wechat menu create success!", contentType: 'text/xml;', encoding: "UTF-8"
            }
        }
    }
}
