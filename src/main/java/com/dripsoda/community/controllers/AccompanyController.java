package com.dripsoda.community.controllers;

import com.dripsoda.community.entities.accompany.ContinentEntity;
import com.dripsoda.community.entities.accompany.CountryEntity;
import com.dripsoda.community.entities.accompany.RegionEntity;
import com.dripsoda.community.services.AccompanyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "com.dripsoda.community.controllers.AccompanyController")
@RequestMapping(value = "/accompany")
public class AccompanyController {
    private final AccompanyService accompanyService;

    @Autowired
    public AccompanyController(AccompanyService accompanyService) {
        this.accompanyService = accompanyService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getIndex(ModelAndView modelAndView) {
        modelAndView.setViewName("accompany/index");
        return modelAndView;
    }

    @RequestMapping(value = "/", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchIndex() {
        // ObjectMapper mapper = new ObjectMapper();
        // JSONArray continentsJson = new JSONArray(mapper.writeValueAsString(this.accompanyService.getContinents()));
        // JSONArray countriesJson = new JSONArray(mapper.writeValueAsString(this.accompanyService.getCountries()));
        // JSONArray regionsJson = new JSONArray(mapper.writeValueAsString(this.accompanyService.getRegions()));
        // JSONObject responseJson = new JSONObject();
        // responseJson.put(ContinentEntity.ATTRIBUTE_NAME_PLURAL, continentsJson);
        // responseJson.put(CountryEntity.ATTRIBUTE_NAME_PLURAL, countriesJson);
        // responseJson.put(RegionEntity.ATTRIBUTE_NAME_PLURAL, regionsJson);
        // return responseJson.toString();

        JSONArray continentsJson = new JSONArray();
        for (ContinentEntity continent : this.accompanyService.getContinents()) {
            JSONObject continentJson = new JSONObject();
            continentJson.put("value", continent.getValue());
            continentJson.put("text", continent.getText());
            continentsJson.put(continentJson);
        }
        JSONArray countriesJson = new JSONArray();
        for (CountryEntity country : this.accompanyService.getCountries()) {
            JSONObject countryJson = new JSONObject();
            countryJson.put("continentValue", country.getContinentValue());
            countryJson.put("value", country.getValue());
            countryJson.put("text", country.getText());
            countriesJson.put(countryJson);
        }
        JSONArray regionsJson = new JSONArray();
        for (RegionEntity region : this.accompanyService.getRegions()) {
            JSONObject regionJson = new JSONObject();
            regionJson.put("continentValue", region.getContinentValue());
            regionJson.put("countryValue", region.getCountryValue());
            regionJson.put("value", region.getValue());
            regionJson.put("text", region.getText());
            regionsJson.put(regionJson);
        }
        JSONObject responseJson = new JSONObject();
        responseJson.put(ContinentEntity.ATTRIBUTE_NAME_PLURAL, continentsJson);
        responseJson.put(CountryEntity.ATTRIBUTE_NAME_PLURAL, countriesJson);
        responseJson.put(RegionEntity.ATTRIBUTE_NAME_PLURAL, regionsJson);
        return responseJson.toString();
    }
}