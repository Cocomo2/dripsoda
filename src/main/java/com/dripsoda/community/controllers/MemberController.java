package com.dripsoda.community.controllers;


import com.dripsoda.community.entities.member.UserEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "com.dripsoda.community.controllers.MemberController")
@RequestMapping(value = "/member")
public class MemberController {
    @RequestMapping(value = "userLogin",method = RequestMethod.GET)
    public ModelAndView getUserLogin(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                     ModelAndView modelAndView) {
        if (user == null) {
            modelAndView.setViewName("member/userLogin");
        } else {
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }
}
