package com.dripsoda.community.services;

import com.dripsoda.community.entities.accompany.ContinentEntity;
import com.dripsoda.community.entities.accompany.CountryEntity;
import com.dripsoda.community.entities.accompany.RegionEntity;
import com.dripsoda.community.mappers.IAccompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "com.dripsoda.community.services.AccompanyService")
public class AccompanyService {
    private final IAccompanyMapper accompanyMapper;

    @Autowired
    public AccompanyService(IAccompanyMapper accompanyMapper) {
        this.accompanyMapper = accompanyMapper;
    }

    public ContinentEntity[] getContinents() {
        return this.accompanyMapper.selectContinents();
    }

    public CountryEntity[] getCountries() {
        return this.accompanyMapper.selectCountries();
    }

    public RegionEntity[] getRegions() {
        return this.accompanyMapper.selectRegions();
    }
}