package com.dripsoda.community.mappers;

import com.dripsoda.community.dtos.accompany.ArticleSearchDto;
import com.dripsoda.community.entities.accompany.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IAccompanyMapper {
    int insertImage(ImageEntity image);

    int insertArticle(ArticleEntity article);

    ArticleEntity selectArticleByIndex(@Param(value = "index") int index);

    ArticleSearchDto[] selectArticlesForSearch(@Param(value = "region") RegionEntity region,
                                               @Param(value = "lastArticleIndex") int lastArticleIndex);

    ContinentEntity[] selectContinents();

    CountryEntity[] selectCountries();

    RegionEntity[] selectRegions();

    ImageEntity selectImageByIndex(@Param(value = "index") int index);
}