package com.samsamoo.zzalu.domain.chat.api;

import com.samsamoo.zzalu.domain.gifs.entity.Gifs;
import com.samsamoo.zzalu.domain.gifs.service.GifsService;
import com.samsamoo.zzalu.global.auth.sevice.JwtTokenProvider;
import com.samsamoo.zzalu.domain.chat.dto.ChatMessageDto;
import com.samsamoo.zzalu.domain.chat.dto.ChatRoomEnroll;
import com.samsamoo.zzalu.domain.chat.dto.ChatRoomListDto;
import com.samsamoo.zzalu.domain.chat.entity.ChatRoom;
import com.samsamoo.zzalu.domain.chat.repository.ChatRoomRedisRepository;
import com.samsamoo.zzalu.domain.chat.repository.ChatRoomRepository;
import com.samsamoo.zzalu.domain.chat.service.ChatRoomService;
import com.samsamoo.zzalu.domain.member.entity.Member;
import com.samsamoo.zzalu.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChatRoomController {

    private final ChatRoomRedisRepository chatRoomRedisRepository;
    private final ChatRoomService chatRoomService;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final GifsService gifsService;

    // =============== redis ======================
//    @GetMapping("/rooms")
//    @ResponseBody
//    public List<ChatRoomDto> rooms() {
//        return chatRoomRedisRepository.findAllRoom();
//    }
//
//    @GetMapping("/rooms-top10")
//    @ResponseBody
//    public List<ChatRoomDto> roomsLikeTop10() {
//        return chatRoomRedisRepository.findTop10LikeCountRoom();
//    }
//
//    @PostMapping("/room")
//    @ResponseBody
//    public ChatRoomDto createRoom(@RequestBody ChatRoomEnroll chatRoomEnroll) {
//        System.out.println("chatroomenroll");
//        System.out.println(chatRoomEnroll.toString());
//        return chatRoomRedisRepository.createChatRoom(chatRoomEnroll);
//    }
//
////    @GetMapping("/room/enter/{roomId}")
////    public String roomDetail(Model model, @PathVariable String roomId) {
////        model.addAttribute("roomId", roomId);
////        return "chat/roomdetail";
////    }
//
//    @GetMapping("/room/{roomId}")
//    @ResponseBody
//    public ChatRoomDto roomInfo(@PathVariable String roomId) {
//        return chatRoomRedisRepository.findRoomById(roomId);
//    }

    @GetMapping("/rooms")
    @ResponseBody
    public List<ChatRoomListDto> findAll() {
        List<ChatRoom> chatRoomList = chatRoomService.findAll();
        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList){
            ChatRoomListDto chatRoomListDto = new ChatRoomListDto(chatRoom);
            chatRoomListDtos.add(chatRoomListDto);
        }
        return chatRoomListDtos;
    }

    @GetMapping("/rooms-top10")
    @ResponseBody
    public List<ChatRoomListDto> findTop10ByOrderByLikeCountDesc() {
        List<ChatRoom> chatRoomList = chatRoomService.findTop10ByOrderByLikeCountDesc();
        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList) {
            ChatRoomListDto chatRoomListDto = new ChatRoomListDto(chatRoom);
            chatRoomListDtos.add(chatRoomListDto);
        }
        return chatRoomListDtos;
    }

    @PostMapping("/room")
    @ResponseBody
    public ChatRoomListDto createRoom(@RequestBody ChatRoomEnroll chatRoomEnroll) {
        ChatRoom chatRoom = chatRoomRedisRepository.createChatRoom(chatRoomEnroll).toEntity();
        return new ChatRoomListDto(chatRoom);
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoomListDto roomInfo(@PathVariable String roomId) {
        ChatRoom chatRoom = chatRoomRedisRepository.findRoomById(roomId).toEntity();
        return new ChatRoomListDto(chatRoom);
    }

    @GetMapping("/search")
    @ResponseBody
    public List<ChatRoomListDto> findByTagsContainsOrRoomNameContains(@RequestParam(name = "keyword") String keyword){
        List<ChatRoom> chatRoomList = chatRoomService.findByTagsContainsOrRoomNameContains(keyword);
        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList) {
            ChatRoomListDto chatRoomListDto = new ChatRoomListDto(chatRoom);
            chatRoomListDtos.add(chatRoomListDto);
        }
        return chatRoomListDtos;
    }

    @GetMapping("/search-tags")
    @ResponseBody
    public List<ChatRoomListDto> findByTagsContains(@RequestParam(name = "keyword") String keyword){
        List<ChatRoom> chatRoomList = chatRoomService.findByTagsContains(keyword);
        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList) {
            ChatRoomListDto chatRoomListDto = new ChatRoomListDto(chatRoom);
            chatRoomListDtos.add(chatRoomListDto);
        }
        return chatRoomListDtos;
    }

    @GetMapping("/search-room-name")
    @ResponseBody
    public List<ChatRoomListDto> findByRoomNameContains(@RequestParam(name = "keyword") String keyword){
        List<ChatRoom> chatRoomList = chatRoomService.findByRoomNameContains(keyword);
        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList) {
            ChatRoomListDto chatRoomListDto = new ChatRoomListDto(chatRoom);
            chatRoomListDtos.add(chatRoomListDto);
        }
        return chatRoomListDtos;
    }

    @GetMapping("/search-order-lastactivation")
    @ResponseBody
    public List<ChatRoomListDto> findAllByTagsContainsOrRoomNameContainsOrderByLastActivationDesc(@RequestParam(name = "keyword") String keyword){
        List<ChatRoom> chatRoomList = chatRoomService.findAllByTagsContainsOrRoomNameContainsOrderByLastActivationDesc(keyword, keyword);
        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList) {
            ChatRoomListDto chatRoomListDto = new ChatRoomListDto(chatRoom);
            chatRoomListDtos.add(chatRoomListDto);
        }
        return chatRoomListDtos;
    }
//
    @GetMapping("/search-order-likecount")
    @ResponseBody
    public List<ChatRoomListDto> findAllByTagsContainsOrRoomNameContainsOrderByLikeCount(@RequestParam(name = "keyword") String keyword){
        List<ChatRoom> chatRoomList = chatRoomService.findAllByTagsContainsOrRoomNameContainsOrderByLikeCountDesc(keyword, keyword);
        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList) {
            ChatRoomListDto chatRoomListDto = new ChatRoomListDto(chatRoom);
            chatRoomListDtos.add(chatRoomListDto);
        }
        return chatRoomListDtos;
    }

    @GetMapping("/member")
    @ResponseBody
    public List<ChatRoomListDto> findAllByMemberId(@RequestHeader(value = "Authorization")String bearerToken){
        String token = bearerToken.substring(7);
        Member requestMember = jwtTokenProvider.getMember(token);
        List<ChatRoom> chatRoomList = chatRoomService.findAllByMemberId(requestMember.getId());
        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList) {
            ChatRoomListDto chatRoomListDto = new ChatRoomListDto(chatRoom);
            chatRoomListDtos.add(chatRoomListDto);
        }
        return chatRoomListDtos;
    }

    @GetMapping("/member-order-lastactivation")
    @ResponseBody
    public List<ChatRoomListDto> findAllByMemberIdOrderByLastActivationDesc(@RequestHeader(value = "Authorization")String bearerToken){
        String token = bearerToken.substring(7);
        Member requestMember = jwtTokenProvider.getMember(token);
        List<ChatRoom> chatRoomList = chatRoomService.findAllByMemberIdOrderByLastActivationDesc(requestMember.getId());
        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList) {
            ChatRoomListDto chatRoomListDto = new ChatRoomListDto(chatRoom);
            chatRoomListDtos.add(chatRoomListDto);
        }
        return chatRoomListDtos;
    }

    @GetMapping("/member-order-likecount")
    @ResponseBody
    public List<ChatRoomListDto> findAllByMemberIdOrderByLikeCountDesc(@RequestHeader(value = "Authorization")String bearerToken){
        String token = bearerToken.substring(7);
        Member requestMember = jwtTokenProvider.getMember(token);
        List<ChatRoom> chatRoomList = chatRoomService.findAllByMemberIdOrderByLikeCountDesc(requestMember.getId());
        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList) {
            ChatRoomListDto chatRoomListDto = new ChatRoomListDto(chatRoom);
            chatRoomListDtos.add(chatRoomListDto);
        }
        return chatRoomListDtos;
    }

    @GetMapping("/search-member-order-lastactivation")
    @ResponseBody
    public List<ChatRoomListDto> findAllByMemberIdAndTagsContainsOrRoomNameContainsOrderByLastActivationDesc(@RequestParam(name = "keyword") String keyword,@RequestHeader(value = "Authorization")String bearerToken){
        String token = bearerToken.substring(7);
        Member requestMember = jwtTokenProvider.getMember(token);
        List<ChatRoom> chatRoomList = chatRoomService.findAllByMemberIdAndTagsContainsOrRoomNameContainsOrderByLastActivationDesc(requestMember.getId(), keyword, keyword);
        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList) {
            ChatRoomListDto chatRoomListDto = new ChatRoomListDto(chatRoom);
            chatRoomListDtos.add(chatRoomListDto);
        }
        return chatRoomListDtos;
    }

    @GetMapping("/search-member-order-likecount")
    @ResponseBody
    public List<ChatRoomListDto> findAllByMemberIdAndTagsContainsOrRoomNameContainsOrderByLikeCountDesc(@RequestParam(name = "keyword") String keyword, @RequestHeader(value = "Authorization")String bearerToken){
        String token = bearerToken.substring(7);
        Member requestMember = jwtTokenProvider.getMember(token);
        List<ChatRoom> chatRoomList = chatRoomService.findAllByMemberIdAndTagsContainsOrRoomNameContainsOrderByLikeCountDesc(requestMember.getId(), keyword, keyword);
        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList) {
            ChatRoomListDto chatRoomListDto = new ChatRoomListDto(chatRoom);
            chatRoomListDtos.add(chatRoomListDto);
        }
        return chatRoomListDtos;
    }

    @GetMapping("/search-like-order-lastactivation")
    @ResponseBody
    public List<ChatRoomListDto> findAllByLikeMembersInAndTagsContainsOrRoomNameContainsOrderByLastActivationDesc(@RequestParam(name = "keyword") String keyword, @RequestHeader(value = "Authorization")String bearerToken){
        String token = bearerToken.substring(7);
        Member requestMember = jwtTokenProvider.getMember(token);
        List<Member> member = new ArrayList<>();
        member.add(requestMember);
        List<ChatRoom> chatRoomList = chatRoomService.findAllByTagsContainsOrRoomNameContainsOrderByLastActivationDesc(keyword, keyword);
        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList) {
            if(chatRoom.getLikeMembers().contains(requestMember))
                chatRoomListDtos.add(new ChatRoomListDto(chatRoom));
        }
        return chatRoomListDtos;
    }

    @GetMapping("/search-like-order-likecount")
    @ResponseBody
    public List<ChatRoomListDto> findAllByLikeMembersInAndTagsContainsOrRoomNameContainsOrderByLikeCountDesc(@RequestParam(name = "keyword") String keyword, @RequestHeader(value = "Authorization")String bearerToken){
        String token = bearerToken.substring(7);
        Member requestMember = jwtTokenProvider.getMember(token);
        List<Member> member = new ArrayList<>();
        member.add(requestMember);
        List<ChatRoom> chatRoomList = chatRoomService.findAllByTagsContainsOrRoomNameContainsOrderByLikeCountDesc(keyword, keyword);
        List<ChatRoomListDto> chatRoomListDtos = new ArrayList<>();
        for(ChatRoom chatRoom : chatRoomList) {
            if(chatRoom.getLikeMembers().contains(requestMember))
                chatRoomListDtos.add(new ChatRoomListDto(chatRoom));
        }
        return chatRoomListDtos;
    }

    // 사용자 인증 정보 식별을 위한 Token 필요
    // 좋아요를 누른 방 아이디 필요
    // Dto 구성해서 수행
    @PostMapping("/like")
    @ResponseBody
    public boolean updateLikeCount(@RequestHeader(value = "Authorization")String bearerToken, @RequestBody Map<String, String> map){

        // AccessToken 검증 필요
        String token = bearerToken.substring(7);
        Member requestMember = jwtTokenProvider.getMember(token);

        String roomId = map.get("roomId");
        System.out.println("RoomId : " + roomId);
        Optional<ChatRoom> optionalChatRoom = chatRoomService.findByRoomId(roomId);
        if(optionalChatRoom.isPresent()) {
            ChatRoom chatRoom = optionalChatRoom.get();
            if(!chatRoom.getLikeMembers().contains(requestMember)) {
                requestMember.addLikeChatRoom(chatRoom);
                chatRoom.setLikeCount(chatRoom.getLikeCount() + 1);
                memberRepository.save(requestMember);
                chatRoomService.save(chatRoom);
                return true;
            } else {
                chatRoom.deleteLikeMember(requestMember);
                chatRoom.setLikeCount(chatRoom.getLikeCount() - 1);
                chatRoomService.save(chatRoom);
                memberRepository.save(requestMember);
//                System.out.println("이미 클릭한 사용자 입니다. Error Exception 필요");
                return false;
            }
        }
        else {
            System.out.println("Can't find chatRoom Error");
            return false;
        }
    }

    @GetMapping("/messages")
    @ResponseBody
    public List<ChatMessageDto> findAllChatMessage(@RequestHeader(value = "Authorization")String bearerToken, @RequestParam String roomId) {
        String token = bearerToken.substring(7);
        Member requestMember = jwtTokenProvider.getMember(token);

        List<ChatMessageDto> chatMessageDtos = chatRoomRedisRepository.findAllChatMessage(roomId);
        return chatMessageDtos;
    }

    @GetMapping("/gif/search")
    public ResponseEntity<List<Gifs>> findByTags(@RequestParam("searchKeyword") String searchKeyword) {
        return ResponseEntity.ok().body(gifsService.findByTags(searchKeyword));
    }
}