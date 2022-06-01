package com.example.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import com.example.demo.dto.Cursor;
import com.example.demo.dto.Message;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class ChatGetMessagesResponse {

    @NotNull //todo do need this?
    @NonNull
    @JsonProperty("messages")
    private List<Message> messages;

    @JsonProperty("next")
    private Cursor next;
}
