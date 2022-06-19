package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @JsonProperty("id")
    @NotBlank
    private String messageId;

    @JsonProperty("text")
    @NotBlank
    private String text;

    @JsonProperty("time")
    @NotBlank
    private String timeCreated;
}
