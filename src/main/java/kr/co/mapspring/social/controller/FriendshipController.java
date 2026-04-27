package kr.co.mapspring.social.controller;

import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.social.controller.docs.FriendshipControllerDocs;
import kr.co.mapspring.social.dto.FriendshipDto;
import kr.co.mapspring.social.service.FriendshipService;
import lombok.Getter;
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
public
class FriendshipController implements FriendshipControllerDocs {

    private final FriendshipService friendshipService;

    @Override
    @PostMapping("requests")
    public ResponseEntity<ApiResponseDTO<Void>> sendFriendRequest(@RequestBody FriendshipDto.RequestSendFriendRequest request) {
        friendshipService.sendFriendRequest(
                request.getRequesterId(),
                request.getAddresseeId()
        );

        return ResponseEntity.ok(ApiResponseDTO.success("친구 요청을 보냈습니다.", null));
    }

    @Override
    @PatchMapping("/requests/{friendshipId}/accept")
    public ResponseEntity<ApiResponseDTO<Void>> acceptFriendRequest(@PathVariable("friendshipId") Long friendshipId,
                                                                    @RequestBody FriendshipDto.RequestHandleFriendRequest request) {
        friendshipService.acceptFriendRequest(
                friendshipId,
                request.getUserId()
        );

        return ResponseEntity.ok(ApiResponseDTO.success("친구 요청을 수락했습니다.", null));
    }

    @Override
    @PatchMapping("/requests/{friendshipId}/reject")
    public ResponseEntity<ApiResponseDTO<Void>> rejectFriendRequest(@PathVariable("friendshipId") Long friendshipId,
                                                                    @RequestBody FriendshipDto.RequestHandleFriendRequest request) {
        friendshipService.rejectFriendRequest(
                friendshipId,
                request.getUserId()
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
                                                             @RequestBody FriendshipDto.RequestHandleFriendRequest request) {
        friendshipService.deleteFriend(
                friendshipId,
                request.getUserId()
        );

        return ResponseEntity.ok(ApiResponseDTO.success("친구를 삭제했습니다.", null));
    }
}
