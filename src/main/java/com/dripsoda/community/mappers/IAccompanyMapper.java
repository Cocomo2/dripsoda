package com.dripsoda.community.mappers;

import com.dripsoda.community.entities.accompany.ContinentEntity;
import com.dripsoda.community.entities.accompany.CountryEntity;
import com.dripsoda.community.entities.accompany.RegionEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IAccompanyMapper {
    ContinentEntity[] selectContinents();

    CountryEntity[] selectCountries();

    RegionEntity[] selectRegions();
}