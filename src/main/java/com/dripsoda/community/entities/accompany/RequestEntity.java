package com.dripsoda.community.entities.accompany;

import java.util.Date;
import java.util.Objects;

public class RequestEntity {
    public static final String ATTRIBUTE_NAME = "accompanyRequest";
    public static final String ATTRIBUTE_NAME_PLURAL = "accompanyRequests";

    public static RequestEntity build() {
        return new RequestEntity();
    }

    private int index;
    private String requesterUserEmail;
    private String requesteeUserEmail;
    private int articleIndex;
    private Date createdAt;
    private boolean isGranted;
    private boolean isDeclined;

    public int getIndex() {
        return index;
    }

    public RequestEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getRequesterUserEmail() {
        return requesterUserEmail;
    }

    public RequestEntity setRequesterUserEmail(String requesterUserEmail) {
        this.requesterUserEmail = requesterUserEmail;
        return this;
    }

    public String getRequesteeUserEmail() {
        return requesteeUserEmail;
    }

    public RequestEntity setRequesteeUserEmail(String requesteeUserEmail) {
        this.requesteeUserEmail = requesteeUserEmail;
        return this;
    }

    public int getArticleIndex() {
        return articleIndex;
    }

    public RequestEntity setArticleIndex(int articleIndex) {
        this.articleIndex = articleIndex;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public RequestEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public boolean isGranted() {
        return isGranted;
    }

    public RequestEntity setGranted(boolean granted) {
        this.isGranted = granted;
        return this;
    }

    public boolean isDeclined() {
        return isDeclined;
    }

    public RequestEntity setDeclined(boolean declined) {
        this.isDeclined = declined;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestEntity that = (RequestEntity) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}
