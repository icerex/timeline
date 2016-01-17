package com.teamlinking.timeline.wechat.handler

import com.teamlinking.timeline.UserState
import grails.transaction.Transactional
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.common.session.WxSessionManager
import me.chanjar.weixin.mp.api.WxMpMessageHandler
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage

/**
 * 上报地理位置事件
 */
class LocationEventService implements WxMpMessageHandler{

    @Override
    @Transactional
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        UserState userState = context.get("userState") as UserState
        if (wxMessage.latitude){
            userState.latitude = wxMessage.latitude
        }
        if (wxMessage.longitude){
            userState.longitude = wxMessage.longitude
        }
        userState.lastUpdated = new Date()
        userState.save(flush: true, failOnError: true)

        return WxMpXmlOutMessage.TEXT().content("Location update success").fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).build()
    }

}
