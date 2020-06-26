package com.xiao.service.impl;

import com.xiao.entity.SysRole;
import com.xiao.entity.SysUser;
import com.xiao.mapper.SysUserMapper;
import com.xiao.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired(required = false)
    private SysUserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("123密文：" + passwordEncoder.encode("123"));
        SysUser sysUser = userMapper.findUserByName(username);
        System.out.println(sysUser.getUsername() + "====" + sysUser.getPassword());
        List<SysRole> roles= (List<SysRole>) sysUser.getAuthorities();
        for (int i = 0; i < roles.size(); i++) {
            System.out.println("数据库角色名称：" + roles.get(i).getRoleName());
        }

        /* 用下面这个返回 user 也可以，不过数据不是从数据库取的了
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("NORMAL"));
        User user = new User(username,passwordEncoder.encode("123"),authorities);

         */
        return sysUser;
    }
}
