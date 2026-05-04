package kr.co.mapspring.social.controller;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.social.controller.docs.FriendshipControllerDocs;
import kr.co.mapspring.social.dto.FriendshipDto;
import kr.co.mapspring.social.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friends")
public class FriendshipController implements FriendshipControllerDocs {

    private final FriendshipService friendshipService;

    @Override
    @PostMapping("/requests")
    public ResponseEntity<ApiResponseDTO<Void>> sendFriendRequest(@RequestParam("loginUserId") Long loginUserId,
                                                                  @RequestBody FriendshipDto.RequestSendFriendRequest request) {
        friendshipService.sendFriendRequest(
                loginUserId,
                request.getAddresseeId()
        );

        return ResponseEntity.ok(ApiResponseDTO.success("친구 요청을 보냈습니다.", null));
    }

    @Override
    @PostMapping("/requests/email")
    public ResponseEntity<ApiResponseDTO<Void>> sendFriendRequestByEmail(@RequestParam("loginUserId") Long loginUserId,
                                                                         @RequestBody FriendshipDto.RequestSendFriendRequestByEmail request) {

        friendshipService.sendFriendRequestByEmail(
                loginUserId,
                request.getEmail()
        );

        return ResponseEntity.ok(ApiResponseDTO.success("이메일로 친구 요청을 보냈습니다.", null));
    }

    @Override
    @PatchMapping("/requests/{friendshipId}/accept")
    public ResponseEntity<ApiResponseDTO<Void>> acceptFriendRequest(@PathVariable("friendshipId") Long friendshipId,
                                                                    @RequestParam("loginUserId") Long loginUserId) {
        friendshipService.acceptFriendRequest(
                friendshipId,
                loginUserId
        );

        return ResponseEntity.ok(ApiResponseDTO.success("친구 요청을 수락했습니다.", null));
    }

    @Override
    @PatchMapping("/requests/{friendshipId}/reject")
    public ResponseEntity<ApiResponseDTO<Void>> rejectFriendRequest(@PathVariable("friendshipId") Long friendshipId,
                                                                    @RequestParam("loginUserId") Long loginUserId) {
        friendshipService.rejectFriendRequest(
                friendshipId,
                loginUserId
        );

        return ResponseEntity.ok(ApiResponseDTO.success("친구 요청을 거절했습니다.", null));
    }

    @Override
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<FriendshipDto.ResponseFriend>>> getFriends(@RequestParam("userId") Long userId) {
        List<FriendshipDto.ResponseFriend> result = friendshipService.getFriends(userId).stream()
                .map(FriendshipDto.ResponseFriend::from)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.success("친구 목록 조회 성공", result));
    }

    @Override
    @DeleteMapping("/{friendshipId}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteFriend(@PathVariable("friendshipId") Long friendshipId,
                                                             @RequestParam("loginUserId") Long loginUserId) {
        friendshipService.deleteFriend(
                friendshipId,
                loginUserId
        );

        return ResponseEntity.ok(ApiResponseDTO.success("친구를 삭제했습니다.", null));
    }

    @Override
    @GetMapping("/requests/received")
    public ResponseEntity<ApiResponseDTO<List<FriendshipDto.ResponseFriend>>> getReceivedRequests(@RequestParam("userId") Long userId) {
        List<FriendshipDto.ResponseFriend> result = friendshipService.getReceivedRequests(userId).stream()
                .map(FriendshipDto.ResponseFriend::from)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.success("받은 친구 요청 조회 성공", result));
    }

    @Override
    @GetMapping("/requests/sent")
    public ResponseEntity<ApiResponseDTO<List<FriendshipDto.ResponseFriend>>> getSentRequests(@RequestParam("userId") Long userId) {
        List<FriendshipDto.ResponseFriend> result = friendshipService.getSentRequests(userId).stream()
                .map(FriendshipDto.ResponseFriend::from)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.success("보낸 친구 요청 조회 성공", result));
    }

    @Override
    @PatchMapping("/{friendshipId}/block")
    public ResponseEntity<ApiResponseDTO<Void>> blockFriend(@PathVariable("friendshipId") Long friendshipId,
                                                            @RequestParam("loginUserId") Long loginUserId) {
        friendshipService.blockFriend(
                friendshipId,
                loginUserId);

        return ResponseEntity.ok(ApiResponseDTO.success("친구를 차단했습니다.", null));
    }

    @Override
    @GetMapping("/history")
    public ResponseEntity<ApiResponseDTO<List<FriendshipDto.ResponseFriend>>> getFriendshipHistory(@RequestParam("userId") Long userId) {
        List<FriendshipDto.ResponseFriend> result = friendshipService.getFriendshipHistory(userId).stream()
                .map(FriendshipDto.ResponseFriend::from)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.success("차단 및 거절 이력 조회 성공", result));
    }

    @Override
    @GetMapping("/recommend")
    public ResponseEntity<ApiResponseDTO<List<FriendshipDto.ResponseRecommendedFriend>>> getRecommendedFriends(
            @RequestParam("userId") Long userId
    ) {
        List<FriendshipDto.ResponseRecommendedFriend> result = friendshipService.getRecommendedFriends(userId).stream()
                .map(FriendshipDto.ResponseRecommendedFriend::from)
                .toList();

        return ResponseEntity.ok(ApiResponseDTO.success("추천 친구 조회 성공", result));
    }
}
