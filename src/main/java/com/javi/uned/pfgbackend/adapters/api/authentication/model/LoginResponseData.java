package com.javi.uned.pfgbackend.adapters.api.authentication.model;

import com.fasterxml.jackson.annotation.*;
import java.util.HashMap;
import java.util.Map;

public class LoginResponseData {

    private String token;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
