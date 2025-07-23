package com.vipin.chatservice.service.impl;
import com.vipin.chatservice.client.UserServiceClient;
import com.vipin.chatservice.model.dto.UsersListForChatRequestDto;
import com.vipin.chatservice.model.entity.ChatMessage;
import com.vipin.chatservice.model.entity.PersonInfo;
import com.vipin.chatservice.model.entity.Role;
import com.vipin.chatservice.repository.ChatMessageRepository;
import com.vipin.chatservice.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserServiceClient userServiceClient;
    @Override
    public ChatMessage saveMessage(ChatMessage chatMessage) {
        return chatMessageRepository.save(chatMessage);
    }
    @Override
    public List<ChatMessage> getChatHistory(String senderId, Role senderRole, String receiverId) {
        List<ChatMessage> messages = new ArrayList<>();
        System.out.println("Sender id: " +senderId);
        System.out.println("Sender role: "+senderRole);
        System.out.println("Receiver id: " + receiverId);
        log.info("Message history: {}",chatMessageRepository.findBySenderIdAndSenderRoleAndReceiverIdOrderByTimestampAsc(
                senderId, senderRole, receiverId));
        messages.addAll(chatMessageRepository.findBySenderIdAndSenderRoleAndReceiverIdOrderByTimestampAsc(
                senderId, senderRole, receiverId));
        if(senderRole.equals(Role.USER)){
            senderRole = Role.WORKER;
        }else {
            senderRole = Role.USER;
        }
        messages.addAll(chatMessageRepository.findBySenderIdAndSenderRoleAndReceiverIdOrderByTimestampAsc(
                receiverId, senderRole, senderId));
        return messages;
    }

    @Override
    public List<PersonInfo> getPreviousChatUsersList(String senderId, Role senderRole) {
       List<ChatMessage> sendMessages = chatMessageRepository.findBySenderIdAndSenderRole(senderId,senderRole);
       List<ChatMessage> receivedMessages = chatMessageRepository.findByReceiverId(senderId);
       Set<String> previousChatUsersIds = new HashSet<>();
       sendMessages.stream().map(ChatMessage :: getReceiverId).forEach(previousChatUsersIds::add);
       receivedMessages.stream().map(ChatMessage::getSenderId).forEach(previousChatUsersIds::add);
        log.info("users for chat from user service : {}" , previousChatUsersIds);
       List<PersonInfo> users = userServiceClient.getUsersList(new UsersListForChatRequestDto(previousChatUsersIds,senderRole.toString()));
       log.info("users for chat from user service : {}" , users);
       return users;
    }

}
