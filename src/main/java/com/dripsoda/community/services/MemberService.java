package com.dripsoda.community.services;

import com.dripsoda.community.components.SmsComponent;
import com.dripsoda.community.entities.member.ContactAuthEntity;
import com.dripsoda.community.enums.CommonResult;
import com.dripsoda.community.enums.ContactVerificationCodeResult;
import com.dripsoda.community.exceptions.RollbackException;
import com.dripsoda.community.interfaces.IResult;
import com.dripsoda.community.mappers.IMemberMapper;
import com.dripsoda.community.regex.MemberRegex;
import com.dripsoda.community.utils.CryptoUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service(value = " com.dripsoda.community.services.MemberService")
public class MemberService {
    private final IMemberMapper memberMapper;
    private final SmsComponent smsComponent;

    @Autowired
    public MemberService(IMemberMapper memberMapper, SmsComponent smsComponent) {
        this.memberMapper = memberMapper;
        this.smsComponent = smsComponent;
    }

    public IResult checkContactVerificationCode(ContactAuthEntity contactAuth) {
        ContactAuthEntity p = this.memberMapper.selectContactAuth(contactAuth);
        if (p == null){
            return CommonResult.FAILURE;
        } else if (p.getExpiresAt().compareTo(new Date()) < 0){
            return ContactVerificationCodeResult.FAILURE_EXPIRED;
        }
        p.setExpiredFlag(true);
        this.memberMapper.updateContactAuth(p);
        return CommonResult.SUCCESS;
    }


    public IResult createContactAuth(ContactAuthEntity contactAuth) throws RollbackException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        if (contactAuth.getContact() == null || !contactAuth.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
        }
        Date createdAt = new Date();
        Date expiresAt = DateUtils.addMinutes(createdAt, 5);
        String code = RandomStringUtils.randomNumeric(6);
        String salt = CryptoUtils.hashSha512(String.format("%s%s%s%f%f",
                contactAuth.getContact(),
                code,
                createdAt.getTime(),
                Math.random(),
                Math.random()));
        contactAuth.setCode(code)
                .setSalt(salt)
                .setCreatedAt(createdAt)
                .setExpiresAt(expiresAt)
                .setExpiredFlag(false);

        if (this.memberMapper.insertContactAuth(contactAuth) == 0) {
            throw new RollbackException();
        }
        String smsContent = String.format("[드립소다] 회원가입 인증번호 [%s]를 입력주세요.", contactAuth.getCode());
        if (this.smsComponent.send(contactAuth.getContact(), smsContent) != 202) {
            throw new RollbackException();
        }

        return CommonResult.SUCCESS;
    }
}
