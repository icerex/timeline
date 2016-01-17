package com.teamlinking.timeline.wechat.handler

import com.teamlinking.timeline.Story
import com.teamlinking.timeline.UserState
import com.teamlinking.timeline.WechatMessage
import com.teamlinking.timeline.common.Constans
import com.teamlinking.timeline.common.Constans.NodeType
import grails.transaction.Transactional
import me.chanjar.weixin.common.api.WxConsts
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.common.session.WxSessionManager
import me.chanjar.weixin.mp.api.WxMpMessageHandler
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage

/**
 * 撤销|取消命令事件
 */
class UndoCommandEventService implements WxMpMessageHandler{

    @Override
    @Transactional
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        Story currentStory = context.get("currentStory") as Story
        UserState userState = context.get("userState") as UserState
        String content = null
        if (userState.command){
            //撤销命令
            switch (userState.command){
                case Constans.WechatCommand.story_image_add.key:
                    currentStory.status = 0 as Byte
                    currentStory.lastUpdated = new Date()
                    currentStory.save()
                    cleanState(userState)
                    content = String.format(Constans.WECHAT_MSG_UNDO_STORY_SUCCESS, currentStory.title)
                    break
                case Constans.WechatCommand.story_add.key:
                case Constans.WechatCommand.story_sub_add.key:
                case Constans.WechatCommand.story_upate.key:
                    cleanState(userState)
                    content = Constans.WECHAT_MSG_UNDO_SUCCESS
            }
        }else {
            content = Constans.WECHAT_MSG_UNDO_FAILE
            //撤销消息
            WechatMessage.findAllByUid(userState.uid,[max: 1, sort: "dateCreated", order: "desc"]).each {
                Node node = Node.get(it.nodeId)
                if (node && node.storyId == userState.currentStoryId){
                    it.status = 0 as Byte
                    it.lastUpdated = new Date()
                    it.save()

                    Constans.NodeType st = Constans.NodeType.pase(node.nodeType)
                    switch (it.msgType){
                        case WxConsts.XML_MSG_TEXT:
                            Constans.NodeType nt = st.pop(Constans.NodeType.text)
                            if (nt){
                                node.content = null
                                popNode(node,nt)
                            }
                            break
                        case WxConsts.XML_MSG_IMAGE:
                            Constans.NodeType nt = st.pop(Constans.NodeType.pic)
                            if (nt){
                                node.picUrl = null
                                popNode(node,nt)
                            }
                            break
                        case WxConsts.XML_MSG_VOICE:
                            Constans.NodeType nt = st.pop(Constans.NodeType.audio)
                            if (nt){
                                node.audioUrl = null
                                popNode(node,nt)
                            }
                            break
                        case WxConsts.XML_MSG_VIDEO:
                            Constans.NodeType nt = st.pop(Constans.NodeType.video)
                            if (nt){
                                node.videoUrl = null
                                popNode(node,nt)
                            }
                            break
                        case WxConsts.XML_MSG_LOCATION:
                            node.latitude = null
                            node.longitude = null
                            node.locationLab = null
                            popNode(node,st)
                            break
                    }
                    content = Constans.WECHAT_MSG_UNDO_SUCCESS
                }
            }
        }

        return WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).build()
    }

    void cleanState(UserState userState){
        userState.command = null
        userState.lastUpdated = new Date()
        userState.save(flush: true, failOnError: true)
    }

    void popNode(Node node, NodeType nodeType){
        node.nodeType = nodeType.key
        node.lastUpdated = new Date()
        node.save(flush: true, failOnError: true)
    }
}
