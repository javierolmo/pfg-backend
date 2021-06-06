package com.javi.uned.pfgbackend.adapters.api.authentication.model;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

public class LoginResponse {


    private LoginResponseData data;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();


    public LoginResponseData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(LoginResponseData data) {
        this.data = data;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
