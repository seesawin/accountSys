package com.seesawin.config.security.services;

import com.seesawin.models.Roles;
import com.seesawin.models.Users;
import com.seesawin.repository.RolesMapper;
import com.seesawin.repository.UserRolesMapper;
import com.seesawin.repository.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UsersMapper usersMapper;
    @Autowired
    UserRolesMapper userRolesMapper;
    @Autowired
    RolesMapper rolesMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersMapper.selectAll().stream()
                .filter(users -> users.getUsername().equals(username)).findAny()
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        List<Roles> roles = rolesMapper.selectAll();
        List<Integer> roleIds = roles.stream().map(r -> r.getId()).collect(Collectors.toList());
        List<Roles> rolesForUser = userRolesMapper.selectAll().stream()
                .filter(userRoles -> userRoles.getUserId().equals(user.getId()))
                .map(userRoles -> {
                    if (roleIds.contains(userRoles.getRoleId())) {
                        return roles.stream().filter(r -> r.getId().equals(userRoles.getRoleId())).findAny().get();
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return UserDetailsImpl.build(user, rolesForUser);
    }

}
