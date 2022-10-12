package com.dripsoda.community.dtos.accompany;

import com.dripsoda.community.entities.accompany.ArticleEntity;

public class ArticleSearchDto extends ArticleEntity {
    private String userName;

    public String getUserName() {
        return this.userName;
    }

    public ArticleSearchDto setUserName(String userName) {
        this.userName = userName;
        return this;
    }
}