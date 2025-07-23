package com.vipin.chatservice.repository;

import com.vipin.chatservice.model.entity.ChatMessage;
import com.vipin.chatservice.model.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {
    List<ChatMessage> findBySenderIdAndReceiverIdAndSenderRole(String senderId, String receiverId, Role senderRole);

    List<ChatMessage> findBySenderIdAndSenderRoleAndReceiverIdOrderByTimestampAsc(String senderId, Role senderRole, String receiverId);

    List<ChatMessage> findBySenderIdAndSenderRole(String senderId, Role senderRole);

    List<ChatMessage> findByReceiverId(String receiverId);
}
