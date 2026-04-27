package kr.co.mapspring.social.controller.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.mapspring.global.dto.ApiResponseDTO;
import kr.co.mapspring.social.dto.FriendshipDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Friendship", description = "친구 관계 API")
public interface FriendshipControllerDocs {

    @Operation(
            summary = "친구 요청 보내기",
            description = "특정 사용자에게 친구 요청을 보냅니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "친구 요청 성공"),
            @ApiResponse(responseCode = "400", description = "자기 자신에게 요청 불가"),
            @ApiResponse(responseCode = "409", description = "이미 친구 관계 존재")
    })
    ResponseEntity<ApiResponseDTO<Void>> sendFriendRequest(
            @RequestBody(
                    description = "친구 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FriendshipDto.RequestSendFriendRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "requesterId": 1,
                                              "addresseeId": 2
                                            }
                                            """
                            )
                    )
            )
            FriendshipDto.RequestSendFriendRequest request
    );


    @Operation(
            summary = "친구 요청 수락",
            description = "받은 친구 요청을 수락합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "친구 요청 수락 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "친구 요청 없음")
    })
    ResponseEntity<ApiResponseDTO<Void>> acceptFriendRequest(
            Long friendshipId,
            @RequestBody(
                    description = "요청 처리 사용자 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FriendshipDto.RequestHandleFriendRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "userId": 2
                                            }
                                            """
                            )
                    )
            )
            FriendshipDto.RequestHandleFriendRequest request
    );


    @Operation(
            summary = "친구 요청 거절",
            description = "받은 친구 요청을 거절합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "친구 요청 거절 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "친구 요청 없음")
    })
    ResponseEntity<ApiResponseDTO<Void>> rejectFriendRequest(
            Long friendshipId,
            @RequestBody(
                    description = "요청 처리 사용자 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FriendshipDto.RequestHandleFriendRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "userId": 2
                                            }
                                            """
                            )
                    )
            )
            FriendshipDto.RequestHandleFriendRequest request
    );


    @Operation(
            summary = "친구 목록 조회",
            description = "사용자의 친구 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<ApiResponseDTO<List<FriendshipDto.ResponseFriend>>> getFriends(Long userId);


    @Operation(
            summary = "친구 삭제",
            description = "친구 관계를 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "친구 삭제 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "친구 관계 없음")
    })
    ResponseEntity<ApiResponseDTO<Void>> deleteFriend(
            Long friendshipId,
            @RequestBody(
                    description = "삭제 요청 사용자 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FriendshipDto.RequestHandleFriendRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "userId": 1
                                            }
                                            """
                            )
                    )
            )
            FriendshipDto.RequestHandleFriendRequest request
    );
}