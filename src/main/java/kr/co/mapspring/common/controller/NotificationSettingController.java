package kr.co.mapspring.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.mapspring.common.dto.NotificationSettingDto;
import kr.co.mapspring.common.service.CommonService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notification-settings")
@RequiredArgsConstructor
public class NotificationSettingController {

    private final CommonService commonService;
    
    // 알림 설정 조회
    @GetMapping
    public ResponseEntity<NotificationSettingDto.Response> getNotificationSetting(
            @AuthenticationPrincipal UserDetails userDetails) {
 
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(commonService.getNotificationSetting(userId));
    }
 
    // 알림 설정 변경
    @PatchMapping
    public ResponseEntity<NotificationSettingDto.Response> updateNotificationSetting(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody NotificationSettingDto.Request request) {
 
        Long userId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(
                commonService.updateNotificationSetting(userId, request));
    }
}
