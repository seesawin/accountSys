package com.seesawin.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
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
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    @ApiOperation("User角色才能呼叫")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "校驗令牌", required = true, dataType = "string", paramType = "header"),
    })
    public String userAccess(@ApiIgnore @AuthenticationPrincipal Principal principal) {
        return "User Content." + principal.getName();
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    @ApiOperation("Moderator角色才能呼叫")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "校驗令牌", required = true, dataType = "string", paramType = "header"),
    })
    public String moderatorAccess(@ApiIgnore @AuthenticationPrincipal Principal principal) {
        return "Moderator Board." + principal.getName();
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Admin角色才能呼叫")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "校驗令牌", required = true, dataType = "string", paramType = "header"),
    })
    public String adminAccess(@ApiIgnore @AuthenticationPrincipal Principal principal) {
        return "Admin Board." + principal.getName();
    }
}
