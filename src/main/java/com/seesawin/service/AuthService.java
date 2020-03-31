package com.seesawin.service;

import com.seesawin.models.Roles;
import com.seesawin.models.UserRoles;
import com.seesawin.models.Users;
import com.seesawin.repository.UserRolesMapper;
import com.seesawin.repository.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AuthService {
    @Autowired
    UsersMapper usersMapper;
    @Autowired
    UserRolesMapper userRolesMapper;

    public void saveUserAndRoles(Users user, Set<Roles> roles) throws Exception {
        log.info("AuthService.saveUserAndRoles user:{}, roles:{}", user, roles);
        usersMapper.insert(user);
        roles.forEach(r -> {
            UserRoles userRoles = new UserRoles();
            userRoles.setUserId(user.getId());
            userRoles.setRoleId(r.getId());
            userRolesMapper.insert(userRoles);
        });
    }
}
