package com.teamlinking.timeline

class User {

    Long id

    Byte status = 1 as Byte

    String openId

    String unionId

    String nickname

    Integer sexId = 0

    String city

    String province

    String country

    String headImgUrl

    String language

    Date dateCreated

    Date lastUpdated

    static constraints = {
        status inList: [1 as byte, 0 as byte]
        dateCreated nullable: false, blank: false
        nickname nullable: false, blank: false
        sexId inList: [0,1,2]
        openId nullable: true, blank: true
        unionId nullable: true, blank: true
        lastUpdated nullable: true, blank:true
        city nullable: true, blank: true
        province nullable: true, blank:true
        country nullable: true, blank:true
        headImgUrl nullable: true, blank:true
        language nullable: true, blank:true
    }

    static mapping = {
        table('t_user')
        version(false)
        id generator: 'identity'
    }
}
