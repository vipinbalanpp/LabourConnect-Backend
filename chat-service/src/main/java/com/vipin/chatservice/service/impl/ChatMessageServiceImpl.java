package com.vipin.chatservice.service.impl;

import com.vipin.chatservice.model.entity.ChatMessage;
import com.vipin.chatservice.model.entity.Role;
import com.vipin.chatservice.repository.ChatMessageRepository;
import com.vipin.chatservice.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    @Override
    public ChatMessage saveMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }

    @Override
    public List<ChatMessage> getChatHistory(String senderId, Role senderRole, String receiverId) {
       return chatMessageRepository.findBySenderIdAndReceiverIdAndSenderRole(senderId,receiverId,senderRole);
    }
}
