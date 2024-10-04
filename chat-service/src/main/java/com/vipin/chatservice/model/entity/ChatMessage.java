package com.vipin.chatservice.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    @Id
    private String id;
    private String senderId;
    private Role senderRole;
    private String receiverId;
    private String content;
    private LocalDateTime timestamp;
    private boolean isRead;
}
