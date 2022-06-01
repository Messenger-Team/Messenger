package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.example.demo.dto.Cursor;
import com.example.demo.dto.HistoryMessage;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class HistoryGetMessagesResponse {

    @JsonProperty("messages")
    @NotNull
    private List<HistoryMessage> taskId;

    @JsonProperty("next")
    private Cursor next;
}