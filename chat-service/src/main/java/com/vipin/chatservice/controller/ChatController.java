package com.vipin.chatservice.controller;

import com.vipin.chatservice.model.entity.ChatMessage;
import com.vipin.chatservice.model.entity.PersonInfo;
import com.vipin.chatservice.model.entity.Role;
import com.vipin.chatservice.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat/api/v1")
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send")
    public ChatMessage sendMessage(@RequestBody ChatMessage chatMessage) {
//        ChatMessage savedMessage = chatMessageService.saveMessage(chatMessage);
        System.out.println("From send message");
        String destination = "/topic/messages/" + chatMessage.getReceiverId();
        messagingTemplate.convertAndSend(destination, chatMessage);
        System.out.println(chatMessage.getContent()+" {}chatmessage");

        return chatMessage;
    }
    @MessageMapping("/send")
    public void handleSendMessage(ChatMessage chatMessage) {
        String destination = "/topic/messages/" + chatMessage.getReceiverId();
        messagingTemplate.convertAndSend(destination, chatMessage);
        chatMessageService.saveMessage(chatMessage);

    }


    @GetMapping("/history")
    public List<ChatMessage> getChatHistory(
            @RequestParam String senderId,
            @RequestParam Role senderRole,
            @RequestParam String receiverId
            ){
        System.out.println(senderId+" "+receiverId);
        System.out.println(chatMessageService.getChatHistory(senderId,senderRole,receiverId).size());
        return chatMessageService.getChatHistory(senderId,senderRole,receiverId);
    }
    @GetMapping("/previousChatUsersList")
    public List<PersonInfo> getPreviousChatUsersList(
            @RequestParam String senderId,
            @RequestParam Role senderRole
    ){
        System.out.println("Sender id and role");
        System.out.println(senderId +"  " + senderRole);
        return chatMessageService.getPreviousChatUsersList(senderId,senderRole);
    }
}
