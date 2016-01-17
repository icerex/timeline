package com.teamlinking.timeline.wechat

import com.teamlinking.timeline.common.Constans
import com.teamlinking.timeline.wechat.handler.*
import com.teamlinking.timeline.wechat.interceptor.DataInterceptorService
import me.chanjar.weixin.common.api.WxConsts
import me.chanjar.weixin.mp.api.WxMpMessageRouter
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage
import org.springframework.beans.factory.InitializingBean

/**
 * 消息路由
 */
class MessageRouterService implements InitializingBean {

    WxMpService wxMpService
    WxMpMessageRouter wxMpMessageRouter

    SubscribeEventService subscribeEventService
    MenuCurrentStoryClickEventService menuCurrentStoryClickEventService
    MenuCommandClickEventService menuCommandClickEventService
    UndoCommandEventService undoCommandEventService
    LocationEventService locationEventService

    MessageTextHandlerService messageTextHandlerService
    MessageImageHandlerService messageImageHandlerService
    MessageAudioHandlerService messageAudioHandlerService
    MessageVideoHandlerService messageVideoHandlerService
    MessageLocationHandlerService messageLocationHandlerService

    DataInterceptorService dataInterceptorService

    WxMpXmlOutMessage route(WxMpXmlMessage inMessage) {
        wxMpMessageRouter.route(inMessage)
    }

    public void afterPropertiesSet() throws Exception {
        wxMpMessageRouter = new WxMpMessageRouter(wxMpService)
        wxMpMessageRouter = wxMpMessageRouter.rule().msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_SUBSCRIBE).handler(subscribeEventService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.BUTTON_CLICK).eventKey(Constans.WechatMenu.undo.key).handler(undoCommandEventService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.BUTTON_CLICK).eventKey(Constans.WechatMenu.currentStory.key).handler(menuCurrentStoryClickEventService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.BUTTON_CLICK).handler(menuCommandClickEventService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_LOCATION).handler(locationEventService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().msgType(WxConsts.XML_MSG_TEXT).content(Constans.WECHAT_MSGTYPE_TEXT_COMMAND_UNDO).handler(undoCommandEventService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().msgType(WxConsts.XML_MSG_TEXT).handler(messageTextHandlerService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().msgType(WxConsts.XML_MSG_IMAGE).handler(messageImageHandlerService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().msgType(WxConsts.XML_MSG_VOICE).handler(messageAudioHandlerService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().msgType(WxConsts.XML_MSG_VIDEO).handler(messageVideoHandlerService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().msgType(WxConsts.XML_MSG_LOCATION).handler(messageLocationHandlerService).interceptor(dataInterceptorService).end()


    }
}
