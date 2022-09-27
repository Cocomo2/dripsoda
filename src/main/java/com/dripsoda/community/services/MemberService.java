package com.dripsoda.community.services;

import com.dripsoda.community.components.SmsComponent;
import com.dripsoda.community.entities.member.ContactAuthEntity;
import com.dripsoda.community.entities.member.ContactCountryEntity;
import com.dripsoda.community.entities.member.UserEntity;
import com.dripsoda.community.enums.CommonResult;
import com.dripsoda.community.exceptions.RollbackException;
import com.dripsoda.community.interfaces.IResult;
import com.dripsoda.community.mappers.IMemberMapper;
import com.dripsoda.community.regex.MemberRegex;
import com.dripsoda.community.utils.CryptoUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service(value = "com.dripsoda.community.services.MemberService")
public class MemberService {
    private final IMemberMapper memberMapper;
    private final SmsComponent smsComponent;

    @Autowired
    public MemberService(IMemberMapper memberMapper, SmsComponent smsComponent) {
        this.memberMapper = memberMapper;
        this.smsComponent = smsComponent;
    }

    @Transactional
    protected IResult createContactAuth(ContactAuthEntity contactAuth) throws
            InvalidKeyException,
            IOException,
            NoSuchAlgorithmException,
            RollbackException {
        if (contactAuth.getContact() == null || !contactAuth.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
        }

        Date createdAt = new Date();
        Date expiresAt = DateUtils.addMinutes(createdAt, 5);
        String code = RandomStringUtils.randomNumeric(6);
        String salt = CryptoUtils.hashSha512(String.format("%s%s%d%f%f",
                contactAuth.getContact(),
                code,
                createdAt.getTime(),
                Math.random(),
                Math.random()));
        contactAuth.setCode(code)
                .setSalt(salt)
                .setCreatedAt(createdAt)
                .setExpiresAt(expiresAt)
                .setExpired(false);
        if (this.memberMapper.insertContactAuth(contactAuth) == 0) {
            // TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            // return CommonResult.FAILURE;
            throw new RollbackException();
        }
        String smsContent = String.format("[드립소다] 인증번호 [%s]를 입력해 주세요.", contactAuth.getCode());
        if (this.smsComponent.send(contactAuth.getContact(), smsContent) != 202) {
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }

    @Transactional
    public IResult checkContactAuth(ContactAuthEntity contactAuth) throws
            RollbackException {
        if (contactAuth.getContact() == null ||
                contactAuth.getCode() == null ||
                contactAuth.getSalt() == null ||
                !contactAuth.getContact().matches(MemberRegex.USER_CONTACT) ||
                !contactAuth.getCode().matches(MemberRegex.CONTACT_AUTH_CODE) ||
                !contactAuth.getSalt().matches(MemberRegex.CONTACT_AUTH_SALT)) {
            return CommonResult.FAILURE;
        }
        contactAuth = this.memberMapper.selectContactAuthByContactCodeSalt(contactAuth);
        if (contactAuth == null) {
            return CommonResult.FAILURE;
        }
        if (contactAuth.isExpired() || new Date().compareTo(contactAuth.getExpiresAt()) > 0) {
            return CommonResult.EXPIRED;
        }
        contactAuth.setExpired(true);
        if (this.memberMapper.updateContactAuth(contactAuth) == 0) {
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }

    @Transactional
    public CommonResult createUser(ContactAuthEntity contactAuth, UserEntity user) throws
            RollbackException {
        if (contactAuth.getContact() == null ||
                contactAuth.getCode() == null ||
                contactAuth.getSalt() == null ||
                !contactAuth.getContact().matches(MemberRegex.USER_CONTACT) ||
                !contactAuth.getCode().matches(MemberRegex.CONTACT_AUTH_CODE) ||
                !contactAuth.getSalt().matches(MemberRegex.CONTACT_AUTH_SALT)) {
            return CommonResult.FAILURE;
        }
        contactAuth = this.memberMapper.selectContactAuthByContactCodeSalt(contactAuth);
        if (contactAuth == null || !contactAuth.isExpired()) {
            return CommonResult.FAILURE;
        }
        if (user.getEmail() == null ||
                user.getPassword() == null ||
                user.getName() == null ||
                user.getContact() == null ||
                !user.getEmail().matches(MemberRegex.USER_EMAIL) ||
                !user.getPassword().matches(MemberRegex.USER_PASSWORD) ||
                !user.getName().matches(MemberRegex.USER_NAME) ||
                !user.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
        }
        user.setPassword(CryptoUtils.hashSha512(user.getPassword()));
        if (this.memberMapper.insertUser(user) == 0) {
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }

    public IResult checkUserEmail(UserEntity user) {
        if (user.getEmail() == null || !user.getEmail().matches(MemberRegex.USER_EMAIL)) {
            return CommonResult.FAILURE;
        }
        user = this.memberMapper.selectUserByEmail(user);
        return user == null
                ? CommonResult.SUCCESS
                : CommonResult.DUPLICATE;
    }

    public ContactCountryEntity[] getContactCountries() {
        return this.memberMapper.selectContactCountries();

    }

    @Transactional
    public IResult recoverUserEmailAuth(UserEntity user, ContactAuthEntity contactAuth) throws
            IOException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            RollbackException {
        if (user.getName() == null ||
                !user.getName().matches(MemberRegex.USER_NAME) ||
                user.getContact() == null ||
                !user.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
        }
        user = this.memberMapper.selectUserByNameContact(user);
        if (user == null) {
            return CommonResult.FAILURE;
        }
        contactAuth.setContact(user.getContact());
        if(this.createContactAuth(contactAuth) != CommonResult.SUCCESS) {
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }

    @Transactional
    public IResult registerAuth(ContactAuthEntity contactAuth) throws
            IOException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            RollbackException {
        if (contactAuth.getContact() == null || !contactAuth.getContact().matches(MemberRegex.USER_CONTACT)) {
            return CommonResult.FAILURE;
        }
        if (this.memberMapper.selectUserByContact(UserEntity.build().setContact(contactAuth.getContact())) != null) {
            return CommonResult.DUPLICATE;
        }
        if(this.createContactAuth(contactAuth) != CommonResult.SUCCESS) {
            throw new RollbackException();
        }
        return CommonResult.SUCCESS;
    }

    @Transactional
    public IResult findUserEmail(ContactAuthEntity contactAuth , UserEntity user) {
        if (contactAuth.getContact() == null ||
                contactAuth.getCode() == null ||
                contactAuth.getSalt() == null ||
                !contactAuth.getContact().matches(MemberRegex.USER_CONTACT) ||
                !contactAuth.getCode().matches(MemberRegex.CONTACT_AUTH_CODE) ||
                !contactAuth.getSalt().matches(MemberRegex.CONTACT_AUTH_SALT)) {
            return CommonResult.FAILURE;
        }
        contactAuth = this.memberMapper.selectContactAuthByContactCodeSalt(contactAuth);
        if (contactAuth == null || !contactAuth.isExpired()) {
            return CommonResult.FAILURE;
        }
        UserEntity foundUser = this.memberMapper.selectUserByContact(user.setContact(contactAuth.getContact()));
        if (foundUser == null) {
            return CommonResult.FAILURE;
        }
        user.setEmail(foundUser.getEmail());
        return CommonResult.SUCCESS;
    }
}




















