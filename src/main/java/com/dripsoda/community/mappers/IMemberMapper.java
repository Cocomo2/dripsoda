package com.dripsoda.community.mappers;

import com.dripsoda.community.entities.member.ContactAuthEntity;
import com.dripsoda.community.entities.member.ContactCountryEntity;
import com.dripsoda.community.entities.member.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMemberMapper {
    int insertContactAuth(ContactAuthEntity contactAuth);

    int insertUser(UserEntity user);

    ContactAuthEntity selectContactAuthByContactCodeSalt(ContactAuthEntity contactAuth);

    ContactCountryEntity[] selectContactCountries();

    UserEntity selectUserByEmail(UserEntity user);

    UserEntity selectUserByContact(UserEntity user);

    UserEntity selectUserByNameContact(UserEntity user);

    int updateContactAuth(ContactAuthEntity contactAuth);
}