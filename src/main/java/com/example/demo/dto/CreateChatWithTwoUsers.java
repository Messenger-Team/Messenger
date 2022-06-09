package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class CreateChatWithTwoUsers {
    @JsonProperty("chat_name")
    @Size(max=255)
    @NotNull
    @NotEmpty
    private String chatName;

    @JsonProperty("user_name_first")
    @Size(max=255)
    @NotBlank
    private String userNameFirst;

    @JsonProperty("user_name_second")
    @Size(max=255)
    @NotBlank
    private String userNameSecond;
}
