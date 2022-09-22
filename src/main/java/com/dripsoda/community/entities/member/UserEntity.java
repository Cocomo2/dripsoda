package com.dripsoda.community.entities.member;

import java.util.Date;
import java.util.Objects;

public class UserEntity {
    public static final String ATTRIBUTE_NAME = "MemberUser";
    public static final String ATTRIBUTE_NAME_PLURAL = "MemberUsers";


    public static UserEntity build() {
        return new UserEntity();
    }

    private String email;
    private String password;
    private String name;
    private String contactCountryValue;
    private String contact;
    private Date policyTermsAt;
    private Date policyPrivacyAt;
    private Date policyMarketingAt;
    private String statusValue;
    private Date registeredAt;
    private Boolean adminFlag;

    public UserEntity() {
    }

    public UserEntity(String email, String password, String name, String contactCountryValue, String contact, Date policyTermsAt, Date policyPrivacyAt, Date policyMarketingAt, String statusValue, Date registeredAt, Boolean adminFlag) {

        this.email = email;
        this.password = password;
        this.name = name;
        this.contactCountryValue = contactCountryValue;
        this.contact = contact;
        this.policyTermsAt = policyTermsAt;
        this.policyPrivacyAt = policyPrivacyAt;
        this.policyMarketingAt = policyMarketingAt;
        this.statusValue = statusValue;
        this.registeredAt = registeredAt;
        this.adminFlag = adminFlag;
    }

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getContactCountryValue() {
        return contactCountryValue;
    }

    public UserEntity setContactCountryValue(String contactCountryValue) {
        this.contactCountryValue = contactCountryValue;
        return this;
    }

    public String getContact() {
        return contact;
    }

    public UserEntity setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public Date getPolicyTermsAt() {
        return policyTermsAt;
    }

    public UserEntity setPolicyTermsAt(Date policyTermsAt) {
        this.policyTermsAt = policyTermsAt;
        return this;
    }

    public Date getPolicyPrivacyAt() {
        return policyPrivacyAt;
    }

    public UserEntity setPolicyPrivacyAt(Date policyPrivacyAt) {
        this.policyPrivacyAt = policyPrivacyAt;
        return this;
    }

    public Date getPolicyMarketingAt() {
        return policyMarketingAt;
    }

    public UserEntity setPolicyMarketingAt(Date policyMarketingAt) {
        this.policyMarketingAt = policyMarketingAt;
        return this;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public UserEntity setStatusValue(String statusValue) {
        this.statusValue = statusValue;
        return this;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public UserEntity setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
        return this;
    }

    public Boolean getAdminFlag() {
        return adminFlag;
    }

    public UserEntity setAdminFlag(Boolean adminFlag) {
        this.adminFlag = adminFlag;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
