package com.seesawin.controller;

import com.seesawin.payload.response.CommonResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public ResponseEntity<?> allAccess() {
        CommonResponse response = new CommonResponse();
        response.setCode("00");
        response.setMsg("Public login sucess");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("User角色才能呼叫")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "校驗令牌", required = true, dataType = "string", paramType = "header"),
    })
    public ResponseEntity<?> userAccess(@ApiIgnore @AuthenticationPrincipal Principal principal) {
        CommonResponse response = new CommonResponse();
        response.setCode("00");
        response.setMsg("user login sucess");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    @ApiOperation("Moderator角色才能呼叫")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "校驗令牌", required = true, dataType = "string", paramType = "header"),
    })
    public ResponseEntity<?> moderatorAccess(@ApiIgnore @AuthenticationPrincipal Principal principal) {
        CommonResponse response = new CommonResponse();
        response.setCode("00");
        response.setMsg("mod login sucess");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Admin角色才能呼叫")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "校驗令牌", required = true, dataType = "string", paramType = "header"),
    })
    public ResponseEntity<?> adminAccess(@ApiIgnore @AuthenticationPrincipal Principal principal) {
        CommonResponse response = new CommonResponse();
        response.setCode("00");
        response.setMsg("admin login sucess");
        return ResponseEntity.ok(response);
    }
}
