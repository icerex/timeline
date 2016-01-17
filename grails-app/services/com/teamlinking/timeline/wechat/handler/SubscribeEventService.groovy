package com.teamlinking.timeline.wechat.handler

import com.teamlinking.timeline.Story
import com.teamlinking.timeline.User
import com.teamlinking.timeline.UserState
import com.teamlinking.timeline.common.Constans
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.common.session.WxSessionManager
import me.chanjar.weixin.mp.api.WxMpMessageHandler
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage
import me.chanjar.weixin.mp.bean.result.WxMpUser
import org.apache.commons.lang.Validate
import org.springframework.beans.BeanUtils

/**
 * 订阅公众号事件
 */
class SubscribeEventService implements WxMpMessageHandler{

    @Override
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        WxMpUser wxMpUser = wxMpService.userInfo(wxMessage.fromUserName,null)
        User user = initUser(wxMpUser)
        Story story = initMasterStory(user.id)
        initState(user.id,story.id)
        return WxMpXmlOutMessage.TEXT().content(Constans.WECHAT_MSG_SUBSCRIBE).fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).build()
    }

    /**
     * 初始化用户数据
     */
    User initUser(WxMpUser wxMpUser){
        Validate.notNull(wxMpUser)
        User user = User.findByOpenId(wxMpUser.openId)
        if (user == null){
            if (wxMpUser.unionId) {
                user = User.findByUnionId(wxMpUser.unionId)
            }
            if (user){
                user.openId = wxMpUser.openId
            }else {
                user = new User(
                        dateCreated: new Date()
                )
            }
        }
        BeanUtils.copyProperties(wxMpUser,user)
        user.lastUpdated = new Date()
        user.save(flush: true,failOnError: true)
    }

    /**
     * 初始化主题数据
     */
    Story initMasterStory(long uid){
        List<Story> list =  Story.findAllByUidAndParentId(uid,0,[max: 1])
        if (list.size() == 0){
            Story story = new Story(
                    uid: uid,
                    dateCreated: new Date(),
                    title: "人生就是一场戏",
                    lastUpdated: new Date()
            )
            return story.save(flush: true,failOnError: true)
        }
        return list.get(0)
    }

    /**
     * 初始化用户状态
     */
    def initState(long uid,long storyId){
        UserState state = UserState.findByUid(uid)
        if (state == null){
            state = new UserState(
                    uid: uid,
                    dateCreated: new Date(),
                    lastUpdated: new Date(),
                    currentStoryId: storyId

            )
            state.save(flush: true,failOnError: true)
        }
    }
}
