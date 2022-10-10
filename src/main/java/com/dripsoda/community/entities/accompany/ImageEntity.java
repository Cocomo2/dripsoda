package com.dripsoda.community.entities.accompany;

import java.util.Date;
import java.util.Objects;

public class ImageEntity {
    public static final String ATTRIBUTE_NAME = "image";
    public static final String ATTRIBUTE_NAME_PLURAL = "images";

    public static ImageEntity build() {
        return new ImageEntity();
    }

    private int index;
    private String userEmail;
    private Date createdAt;
    private String name;
    private String mime;
    private byte[] data;

    public ImageEntity() {
    }

    public ImageEntity(int index, String userEmail, Date createdAt, String name, String mime, byte[] data) {
        this.index = index;
        this.userEmail = userEmail;
        this.createdAt = createdAt;
        this.name = name;
        this.mime = mime;
        this.data = data;
    }

    public int getIndex() {
        return this.index;
    }

    public ImageEntity setIndex(int index) {
        this.index = index;
        return this;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public ImageEntity setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public ImageEntity setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public ImageEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getMime() {
        return this.mime;
    }

    public ImageEntity setMime(String mime) {
        this.mime = mime;
        return this;
    }

    public byte[] getData() {
        return this.data;
    }

    public ImageEntity setData(byte[] data) {
        this.data = data;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        ImageEntity that = (ImageEntity) o;
        return this.index == that.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.index);
    }
}