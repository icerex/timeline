package com.teamlinking.timeline.domain

import com.teamlinking.timeline.User
import org.apache.commons.lang.Validate

class UserService {

    User get(long id){
        Validate.isTrue(id > 0)
        User.findById(id)
    }

    User get(String openId){
        Validate.notEmpty(openId)
        User.findByOpenId(openId)
    }
}
