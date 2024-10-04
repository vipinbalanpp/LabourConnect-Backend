package com.vipin.chatservice.controller;

import com.vipin.chatservice.model.entity.ChatMessage;
import com.vipin.chatservice.model.entity.Role;
import com.vipin.chatservice.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send")
    public ChatMessage sendMessage(@RequestBody ChatMessage chatMessage) {
        ChatMessage savedMessage = chatMessageService.saveMessage(chatMessage);
        String destination = "/topic/messages/" + chatMessage.getReceiverId();
        messagingTemplate.convertAndSend(destination, savedMessage);

        return savedMessage;
    }
    @GetMapping("/history")
    public List<ChatMessage> getChatHistory(
            @RequestParam String senderId,
            @RequestParam Role senderRole,
            @RequestParam String receiverId
            ){
        return chatMessageService.getChatHistory(senderId,senderRole,receiverId);
    }
}
