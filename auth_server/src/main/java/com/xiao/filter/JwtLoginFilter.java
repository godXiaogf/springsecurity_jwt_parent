package com.xiao.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiao.config.RsaKeyProperties;
import com.xiao.entity.SysRole;
import com.xiao.entity.SysUser;
import com.xiao.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private RsaKeyProperties rsaKeyProperties;

    public JwtLoginFilter(AuthenticationManager authenticationManager, RsaKeyProperties rsaKeyProperties){
        this.authenticationManager = authenticationManager;
        this.rsaKeyProperties = rsaKeyProperties;
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            SysUser sysUser = new ObjectMapper().readValue(request.getInputStream(), SysUser.class);
            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(sysUser.getUsername(), sysUser.getPassword());
            return authenticationManager.authenticate(authRequest);
        } catch (IOException e) {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            try {
                PrintWriter writer = response.getWriter();
                Map resultMap = new HashMap<>();
                resultMap.put("code",HttpServletResponse.SC_UNAUTHORIZED);
                resultMap.put("msg", "用户名或密码错误！");
                writer.write(new ObjectMapper().writeValueAsString(resultMap));
                writer.flush();
                writer.close();
            }catch(Exception ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException(e);
        }

    }

    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SysUser sysUser = new SysUser();
        sysUser.setUsername(authResult.getName());  // 密码是敏感信息，不放到头部
        sysUser.setRoles((List<SysRole>) authResult.getAuthorities());
        String token = JwtUtils.generateTokenExpireInMinutes(sysUser, rsaKeyProperties.getPrivateKey(), 24 * 60);
        response.addHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            PrintWriter writer = response.getWriter();
            Map resultMap = new HashMap<>();
            resultMap.put("code",HttpServletResponse.SC_OK);
            resultMap.put("msg", "认证通过！");
            writer.write(new ObjectMapper().writeValueAsString(resultMap));
            writer.flush();
            writer.close();
        }catch(Exception ex) {
            ex.printStackTrace();
        }
    }


}
