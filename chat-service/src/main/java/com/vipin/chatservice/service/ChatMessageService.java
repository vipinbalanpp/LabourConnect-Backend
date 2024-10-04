package com.vipin.chatservice.service;

import com.vipin.chatservice.model.entity.ChatMessage;
import com.vipin.chatservice.model.entity.Role;

import java.util.List;

public interface ChatMessageService {
    ChatMessage saveMessage(ChatMessage chatMessage);

    List<ChatMessage> getChatHistory(String senderId, Role senderRole, String receiverId);
}
