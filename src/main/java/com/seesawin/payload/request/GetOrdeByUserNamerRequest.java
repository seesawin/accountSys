package com.seesawin.payload.request;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class GetOrdeByUserNamerRequest {
    @NotBlank
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "GetOrdeByUserNamerRequest{" +
                "username='" + username + '\'' +
                '}';
    }
}
