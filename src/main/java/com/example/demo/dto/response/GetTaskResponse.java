package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.example.demo.status.SearchTaskStatus;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GetTaskResponse {

    @JsonProperty("status")
    @NotBlank
    private SearchTaskStatus status;
}