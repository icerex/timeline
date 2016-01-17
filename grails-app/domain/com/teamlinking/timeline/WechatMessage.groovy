package com.teamlinking.timeline

class WechatMessage {

    Long id

    Date dateCreated

    Date lastUpdated

    Byte status = 1 as Byte
    //微信MSGID
    Long msgId

    Long uid

    String openId

    String msgType

    String body

    Long nodeId

    static constraints = {
        msgId nullable: false, blank: false
        dateCreated nullable: false, blank: false
        status inList: [1 as byte, 0 as byte]
        openId nullable: false, blank: false
        uid nullable: false, blank: false
        msgType nullable: false, blank:false
        body nullable: false, blank:false
        nodeId nullable: false, blank:false
        lastUpdated nullable: true, blank:true
    }

    static mapping = {
        table('t_wechat_message')
        version(false)
        id generator: 'identity'
        msgId unique: true
        body type: "text"
    }
}
