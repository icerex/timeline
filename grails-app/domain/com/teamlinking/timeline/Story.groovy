package com.teamlinking.timeline

class Story {

    Long id

    Long uid

    Date dateCreated

    Date lastUpdated

    Byte status = 1 as Byte

    Long parentId = 0

    String title

    String pic

    static constraints = {
        uid nullable: false, blank: false
        dateCreated nullable: false, blank: false
        status inList: [1 as byte, 0 as byte]
        title nullable: false, blank: false
        parentId nullable: false, blank:false
        lastUpdated nullable: true, blank:true
        pic nullable: true, blank:true
    }

    static mapping = {
        table('t_story')
        version(false)
        id generator: 'identity'
    }
}
