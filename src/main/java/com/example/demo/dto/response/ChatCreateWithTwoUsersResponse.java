package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ChatCreateWithTwoUsersResponse {
    @JsonProperty("chat_id")
    @NotBlank
    private String chatId;

    @JsonProperty("user_first_id")
    @NotBlank
    private String userFirstId;

    @JsonProperty("user_second_id")
    @NotBlank
    private String userSecondId;
}
