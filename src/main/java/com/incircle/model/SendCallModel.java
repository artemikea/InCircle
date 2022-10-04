package com.incircle.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SendCallModel {

    @JsonProperty(value = "public_key")
    private String publicKey;

    private String phone;

    @JsonProperty(value = "campaign_id")
    private String campaignId;

    private String text;
}
