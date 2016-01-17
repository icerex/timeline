package com.teamlinking.timeline.wechat.interceptor

import com.teamlinking.timeline.Story
import com.teamlinking.timeline.User
import com.teamlinking.timeline.UserState
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.common.session.WxSessionManager
import me.chanjar.weixin.mp.api.WxMpMessageInterceptor
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage

/**
 * 公众号命令类型菜单点击事件数据共享
 */
class DataInterceptorService implements WxMpMessageInterceptor{

    @Override
    boolean intercept(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        User user = User.findByOpenId(wxMessage.fromUserName)
        if (user && user.status == Byte.parseByte("1")){
            UserState userState = UserState.findByUid(user.id)
            Story currentStory = Story.get(userState.currentStoryId)
            context.put("user",user)
            context.put("userState",userState)
            context.put("currentStory",currentStory)
            return true
        }

        return false
    }
}
