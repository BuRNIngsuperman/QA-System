package com.example.wenda.Controller;

import com.alibaba.fastjson.JSON;
import com.example.wenda.model.EntityType;
import com.example.wenda.model.HostHolder;
import com.example.wenda.service.LikeService;
import com.example.wenda.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Jin Qiuyang
 * @date 2020/3/14
 */

@Controller
public class LikeController {

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = "/like",method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId){
        if(hostHolder.getUser() == null){
            return JSONUtil.getJSONString(999);
        }

        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,commentId);
        return JSONUtil.getJSONString(0,String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return JSONUtil.getJSONString(999);
        }

        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return JSONUtil.getJSONString(0, String.valueOf(likeCount));
    }

}
