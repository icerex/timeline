package com.teamlinking.timeline.domain

import com.teamlinking.timeline.Story
import com.teamlinking.timeline.User
import com.teamlinking.timeline.UserState


class StoryService {

    Story get(long id){
        Story.get(id)
    }

    /**
     * 当前主题
     * @param openId
     * @return
     */
    Story getCurrentStory(String openId) {
        User user = User.findByOpenId(openId)
        UserState userState = UserState.findByUid(user.id)
        get(userState.currentStoryId)
    }

    /**
     * 下一个主题,当前是最后一个下一个为第一个,如果该父主题下只有一个则返回null
     * @param currentId
     */
    Story getNextStory(long currentId){
        Story story = get(currentId)
        Story next = null
        Story.createCriteria().list(max: 1) {
            eq("parentId",story.parentId)
            gt("dateCreated", story.dateCreated)
            gt("id", story.id)
        }.each {
            next = it
        }
        if (next == null){
            Story.findAllByParentId(story.parentId,[max: 1, sort: "dateCreated", order: "asc"]).each {
                next = it
            }
        }
        return next
    }

    /**
     * 父主题
     * @param currentId
     * @return
     */
    Story getParentStory(long currentId){
        Story story = get(currentId)
        if (story.parentId > 0){
            return get(story.parentId)
        }
        return null
    }
}
