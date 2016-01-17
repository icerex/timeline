package com.teamlinking.timeline.wechat.handler

import com.teamlinking.timeline.Story
import com.teamlinking.timeline.UserState
import com.teamlinking.timeline.common.Constans
import com.teamlinking.timeline.domain.StoryService
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.common.session.WxSessionManager
import me.chanjar.weixin.mp.api.WxMpMessageHandler
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage

/**
 * 公众号命令类型菜单点击事件
 */
class MenuCommandClickEventService implements WxMpMessageHandler{

    StoryService storyService

    @Override
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        Story currentStory = context.get("currentStory") as Story
        UserState userState = context.get("userState") as UserState
        String content = null

        if (userState.command){
            //如果在流程命令中,不能菜单执行命令
            switch (userState.command){
                case Constans.WechatCommand.story_image_add.key:
                    content = String.format(Constans.WECHAT_MSG_ADD_STORY_IMAGE_FAILE, currentStory.title)
                    break
            }
        }
        if (content == null) {
            switch (wxMessage.eventKey) {
                case Constans.WechatMenu.updateStory.key:
                    userState.command = Constans.WechatCommand.story_upate.key
                    userState.lastUpdated = new Date()
                    userState.save(flush: true, failOnError: true)
                    content = String.format(Constans.WECHAT_MSG_UPDATE_STORY_BEFORE, currentStory.title)
                    break
                case Constans.WechatMenu.addStory.key:
                    userState.command = Constans.WechatCommand.story_add.key
                    userState.lastUpdated = new Date()
                    userState.save(flush: true, failOnError: true)
                    content = Constans.WECHAT_MSG_ADD_STORY_BEFORE
                    break
                case Constans.WechatMenu.addSubStory.key:
                    userState.command = Constans.WechatCommand.story_sub_add.key
                    userState.lastUpdated = new Date()
                    userState.save(flush: true, failOnError: true)
                    content = String.format(Constans.WECHAT_MSG_ADD_SUB_STORY_BEFORE, currentStory.title)
                    break
                case Constans.WechatMenu.nextStory.key:
                    Story next = storyService.getNextStory(userState.currentStoryId)
                    if (next) {
                        userState.command = null
                        userState.lastUpdated = new Date()
                        userState.currentStoryId = next.id
                        userState.save(flush: true, failOnError: true)
                        content = String.format(Constans.WECHAT_MSG_NEXT_STORY, next.title)
                    } else {
                        content = String.format(Constans.WECHAT_MSG_NEXT_STORY_FAILE, currentStory.title)
                    }
                    break
                case Constans.WechatMenu.backParent.key:
                    content = String.format(Constans.WECHAT_MSG_BACK_PARENT_FAILE, currentStory.title)
                    if (currentStory.parentId > 0) {
                        Story parent = storyService.get(currentStory.parentId)
                        if (parent) {
                            userState.command = null
                            userState.lastUpdated = new Date()
                            userState.currentStoryId = parent.id
                            userState.save(flush: true, failOnError: true)
                            content = String.format(Constans.WECHAT_MSG_BACK_PARENT, parent.title)
                        }
                    }
                    break

            }
        }
        return WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).build()
    }
}
