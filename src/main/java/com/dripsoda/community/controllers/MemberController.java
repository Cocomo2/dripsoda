package com.dripsoda.community.controllers;

import com.dripsoda.community.entities.member.ContactAuthEntity;
import com.dripsoda.community.entities.member.ContactCountryEntity;
import com.dripsoda.community.entities.member.UserEntity;
import com.dripsoda.community.enums.CommonResult;
import com.dripsoda.community.exceptions.RollbackException;
import com.dripsoda.community.interfaces.IResult;
import com.dripsoda.community.services.MemberService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Controller(value = "com.dripsoda.community.controllers.MemberController")
@RequestMapping(value = "/member")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @RequestMapping(value = "userEmailCheck", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getUserEmailCheck(UserEntity user) {
        IResult result = this.memberService.checkUserEmail(user);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userRecoverEmail", method = RequestMethod.GET)
    public ModelAndView getUserRecoverEmail(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                            ModelAndView modelAndView) {
        if (user != null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        modelAndView.addObject(ContactCountryEntity.ATTRIBUTE_NAME_PLURAL, this.memberService.getContactCountries());
        modelAndView.setViewName("member/userRecoverEmail");
        return modelAndView;
    }

    @RequestMapping(value = "userRecoverEmail", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserRecoverEmail(ContactAuthEntity contactAuth) {
        contactAuth.setIndex(-1)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
        UserEntity user = UserEntity.build();
        IResult result = this.memberService.findUserEmail(contactAuth, user);
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("email", user.getEmail());
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "userRecoverEmailAuth", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getUserRecoverEmailAuth(UserEntity user) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        user.setEmail(null)
                .setPassword(null)
                .setPolicyTermsAt(null)
                .setPolicyPrivacyAt(null)
                .setPolicyMarketingAt(null)
                .setStatusValue(null)
                .setRegisteredAt(null)
                .setAdmin(false);
        IResult result;
        ContactAuthEntity contactAuth = ContactAuthEntity.build();
        try {
            result = this.memberService.recoverUserEmailAuth(user, contactAuth);
        } catch (RollbackException ex) {
            result = CommonResult.FAILURE;
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("salt", contactAuth.getSalt());
        }
        return  responseJson.toString();
    }
    @RequestMapping(value = "userRecoverEmailAuth", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserRecoverEmailAuth(ContactAuthEntity contactAuth) {
        return this.postUserRegisterAuth(contactAuth);
    }


    @RequestMapping(value = "userLogin", method = RequestMethod.GET)
    public ModelAndView getUserLogin(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                     ModelAndView modelAndView) {
        if (user != null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        modelAndView.setViewName("member/userLogin");
        return modelAndView;
    }

    @RequestMapping(value = "userRegister", method = RequestMethod.GET)
    public ModelAndView getUserRegister(@SessionAttribute(value = UserEntity.ATTRIBUTE_NAME, required = false) UserEntity user,
                                        ModelAndView modelAndView) {
        if (user != null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        modelAndView.addObject(ContactCountryEntity.ATTRIBUTE_NAME_PLURAL, this.memberService.getContactCountries());
        modelAndView.setViewName("member/userRegister");
        return modelAndView;
    }

    @RequestMapping(value = "userRegister", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserRegister(@RequestParam(value = "policyMarketing", required = true) boolean policyMarketing,
                                   ContactAuthEntity contactAuth,
                                   UserEntity user) {
        contactAuth.setIndex(-1)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
        user.setPolicyTermsAt(new Date())
                .setPolicyPrivacyAt(new Date())
                .setPolicyMarketingAt(policyMarketing ? new Date() : null)
                .setStatusValue("OKY")
                .setRegisteredAt(new Date())
                .setAdmin(false);
        IResult result;
        try {
            result = this.memberService.createUser(contactAuth, user);
        } catch (RollbackException ex) {
            result = ex.result;
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }

    @RequestMapping(value = "userRegisterDone", method = RequestMethod.GET)
    public ModelAndView getUserRegisterDone(ModelAndView modelAndView) {
        modelAndView.setViewName("member/userRegisterDone");
        return modelAndView;
    }

    @RequestMapping(value = "userRegisterAuth", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getUserRegisterAuth(ContactAuthEntity contactAuth) throws
            InvalidKeyException,
            IOException,
            NoSuchAlgorithmException {
        contactAuth.setIndex(-1)
                .setCode(null)
                .setSalt(null)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
        IResult result;
        try {
            result = this.memberService.registerAuth(contactAuth);
        } catch (RollbackException ex) {
            result = ex.result;
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        if (result == CommonResult.SUCCESS) {
            responseJson.put("salt", contactAuth.getSalt());
        }
        return responseJson.toString();
    }

    @RequestMapping(value = "userRegisterAuth", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String postUserRegisterAuth(ContactAuthEntity contactAuth) {
        contactAuth.setIndex(-1)
                .setCreatedAt(null)
                .setExpiresAt(null)
                .setExpired(false);
        IResult result;
        try {
            result = this.memberService.checkContactAuth(contactAuth);
        } catch (RollbackException ex) {
            result = ex.result;
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(IResult.ATTRIBUTE_NAME, result.name().toLowerCase());
        return responseJson.toString();
    }
}












