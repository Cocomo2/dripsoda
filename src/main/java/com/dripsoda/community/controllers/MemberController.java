package com.dripsoda.community.controllers;


import com.dripsoda.community.entities.member.ContactAuthEntity;
import com.dripsoda.community.entities.member.UserEntity;
import com.dripsoda.community.enums.CommonResult;
import com.dripsoda.community.exceptions.RollbackException;
import com.dripsoda.community.interfaces.IResult;
import com.dripsoda.community.services.MemberService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

@Controller(value = "com.dripsoda.community.controllers.MemberController")
@RequestMapping(value = "/member")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @RequestMapping(value = "userLogin",method = RequestMethod.GET)
    public ModelAndView getUserLogin(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                     ModelAndView modelAndView) {
        if (user == null) {
            modelAndView.setViewName("member/userLogin");
            return modelAndView;
        } else {
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }
    @RequestMapping(value = "userRegister",method = RequestMethod.GET)
    public ModelAndView getUserRegister(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                     ModelAndView modelAndView) {
        if (user == null) {
            modelAndView.setViewName("member/userRegister");
            return modelAndView;
        } else {
            modelAndView.setViewName("redirect:/");
        }
        return modelAndView;
    }

    @RequestMapping(value = "contactAuthCode", method = RequestMethod.GET ,produces = "application/json")
    @ResponseBody
    public String getContactAuthCode(ContactAuthEntity contactAuth) throws NoSuchAlgorithmException, IOException, InvalidKeyException {
        contactAuth.setIndex(-1)
            .setCode(null)
            .setSalt(null)
            .setCreatedAt(null)
            .setExpiresAt(null)
            .setExpiredFlag(false);
        IResult result;
        try {
            result = this.memberService.createContactAuth(contactAuth);
        } catch (RollbackException ex){
            result = ex.result;
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("salt", contactAuth.getSalt());
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "contactAuthCode", method = RequestMethod.POST)
    @ResponseBody
    public String postContactVerificationCode(ContactAuthEntity contactAuth) {
        JSONObject responseJson = new JSONObject();
        IResult result = this.memberService.checkContactVerificationCode(contactAuth);
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }
}
