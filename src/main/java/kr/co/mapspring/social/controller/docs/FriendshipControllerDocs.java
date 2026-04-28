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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Friendship", description = "친구 관계 API")
public interface FriendshipControllerDocs {

    @Operation(
            summary = "친구 요청 보내기",
            description = "특정 사용자에게 친구 요청을 보냅니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "친구 요청 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "친구 요청을 보냈습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "자기 자신에게 요청 불가",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 400,
                                      "message": "자기 자신에게 친구 요청을 보낼 수 없습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이미 친구 관계 존재",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 409,
                                      "message": "이미 친구 관계가 존재합니다.",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> sendFriendRequest(
            @RequestBody(
                    description = "친구 요청 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FriendshipDto.RequestSendFriendRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "requesterId": 1,
                                      "addresseeId": 2
                                    }
                                    """)
                    )
            )
            FriendshipDto.RequestSendFriendRequest request
    );

    @Operation(
            summary = "친구 요청 수락",
            description = "받은 친구 요청을 수락합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "친구 요청 수락 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "친구 요청을 수락했습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 403,
                                      "message": "해당 친구 요청을 처리할 권한이 없습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "친구 요청 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 404,
                                      "message": "친구 관계를 찾을 수 없습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> acceptFriendRequest(
            @PathVariable("friendshipId") Long friendshipId,
            @RequestBody(
                    description = "요청 처리 사용자 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FriendshipDto.RequestHandleFriendRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "userId": 2
                                    }
                                    """)
                    )
            )
            FriendshipDto.RequestHandleFriendRequest request
    );

    @Operation(
            summary = "친구 요청 거절",
            description = "받은 친구 요청을 거절합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "친구 요청 거절 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "친구 요청을 거절했습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 403,
                                      "message": "해당 친구 요청을 처리할 권한이 없습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "친구 요청 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 404,
                                      "message": "친구 관계를 찾을 수 없습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> rejectFriendRequest(
            @PathVariable("friendshipId") Long friendshipId,
            @RequestBody(
                    description = "요청 처리 사용자 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FriendshipDto.RequestHandleFriendRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "userId": 2
                                    }
                                    """)
                    )
            )
            FriendshipDto.RequestHandleFriendRequest request
    );

    @Operation(
            summary = "친구 목록 조회",
            description = "사용자의 친구 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "친구 목록 조회 성공",
                                      "data": [
                                        {
                                          "friendshipId": 1,
                                          "requesterId": 1,
                                          "addresseeId": 2,
                                          "status": "ACCEPTED"
                                        }
                                      ]
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<FriendshipDto.ResponseFriend>>> getFriends(@RequestParam("userId") Long userId);

    @Operation(
            summary = "친구 삭제",
            description = "친구 관계를 삭제합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "친구 삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": true,
                                      "status": 200,
                                      "message": "친구를 삭제했습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 403,
                                      "message": "해당 친구 요청을 처리할 권한이 없습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "친구 관계 없음",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                    {
                                      "success": false,
                                      "status": 404,
                                      "message": "친구 관계를 찾을 수 없습니다.",
                                      "data": null
                                    }
                                    """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> deleteFriend(
            @PathVariable("friendshipId") Long friendshipId,
            @RequestBody(
                    description = "삭제 요청 사용자 정보",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = FriendshipDto.RequestHandleFriendRequest.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "userId": 1
                                    }
                                    """)
                    )
            )
            FriendshipDto.RequestHandleFriendRequest request
    );

    @Operation(
            summary = "받은 친구 요청 목록 조회",
            description = "사용자가 받은 친구 요청(PENDING 상태)을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "success": true,
                                  "status": 200,
                                  "message": "받은 친구 요청 조회 성공",
                                  "data": [
                                    {
                                      "friendshipId": 1,
                                      "requesterId": 2,
                                      "addresseeId": 1,
                                      "status": "PENDING"
                                    }
                                  ]
                                }
                                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "success": false,
                                  "status": 400,
                                  "message": "userId는 필수입니다.",
                                  "data": null
                                }
                                """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<FriendshipDto.ResponseFriend>>> getReceivedRequests(@RequestParam("userId") Long userId);

    @Operation(
            summary = "보낸 친구 요청 목록 조회",
            description = "사용자가 보낸 친구 요청(PENDING 상태)을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "success": true,
                                  "status": 200,
                                  "message": "보낸 친구 요청 조회 성공",
                                  "data": [
                                    {
                                      "friendshipId": 1,
                                      "requesterId": 1,
                                      "addresseeId": 2,
                                      "status": "PENDING"
                                    }
                                  ]
                                }
                                """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                  "success": false,
                                  "status": 400,
                                  "message": "userId는 필수입니다.",
                                  "data": null
                                }
                                """)
                    )
            )
    })
    ResponseEntity<ApiResponseDTO<List<FriendshipDto.ResponseFriend>>> getSentRequests(@RequestParam("userId") Long userId);

    @Operation(
            summary = "친구 차단",
            description = "특정 친구 관계를 차단 상태로 변경합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "차단 성공"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "권한 없음"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "친구 관계 없음"
            )
    })
    ResponseEntity<ApiResponseDTO<Void>> blockFriend(
            @PathVariable("friendshipId") Long friendshipId,
            @RequestBody FriendshipDto.RequestHandleFriendRequest request
    );
}