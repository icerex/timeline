package com.teamlinking.timeline

class UserState {

    Long id

    Long uid

    Date dateCreated

    Date lastUpdated

    Long currentStoryId

    String command
    //纬度
    Double latitude
    //精度
    Double longitude

    static constraints = {
        uid nullable: false, blank: false
        dateCreated nullable: false, blank: false
        currentStoryId nullable: false, blank: false
        lastUpdated nullable: true, blank:true
        command nullable: true, blank:true
        latitude nullable: true, blank:true
        longitude nullable: true, blank:true
    }

    static mapping = {
        table('t_user_state')
        version(false)
        id generator: 'identity'
        uid unique: true
    }
}
