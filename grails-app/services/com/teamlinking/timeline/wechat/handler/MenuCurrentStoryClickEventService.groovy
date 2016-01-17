package com.teamlinking.timeline.wechat.handler

import com.teamlinking.timeline.Story
import com.teamlinking.timeline.common.Constans
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.common.session.WxSessionManager
import me.chanjar.weixin.mp.api.WxMpMessageHandler
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage

/**
 * 公众号菜单当前主题按钮点击事件
 */
class MenuCurrentStoryClickEventService implements WxMpMessageHandler{

    @Override
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        Story currentStory = context.get("currentStory") as Story
        String content = String.format(Constans.WECHAT_MSG_CURRENT_STORY,currentStory.title)
        return WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).build()
    }
}
