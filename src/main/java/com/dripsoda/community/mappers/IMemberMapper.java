package com.dripsoda.community.mappers;

import com.dripsoda.community.entities.member.ContactAuthEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IMemberMapper {
    int insertContactAuth(ContactAuthEntity contactAuth);

    ContactAuthEntity selectContactAuth(ContactAuthEntity contactAuth);

    int updateContactAuth(ContactAuthEntity contactAuth);
}
