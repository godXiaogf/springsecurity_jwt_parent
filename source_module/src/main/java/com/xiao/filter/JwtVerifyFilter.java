package com.xiao.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiao.config.RsaKeyProperties;
import com.xiao.entity.Payload;
import com.xiao.entity.SysUser;
import com.xiao.utils.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class JwtVerifyFilter extends BasicAuthenticationFilter {
    private RsaKeyProperties rsaKeyProperties;

    public JwtVerifyFilter(AuthenticationManager authenticationManager, RsaKeyProperties rsaKeyProperties) {
        super(authenticationManager);
        this.rsaKeyProperties = rsaKeyProperties;
    }

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Bearer")){
            chain.doFilter(request, response);
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            PrintWriter writer = response.getWriter();
            Map resultMap = new HashMap<>();
            resultMap.put("code",HttpServletResponse.SC_FORBIDDEN);
            resultMap.put("msg", "请登录！");
            writer.write(new ObjectMapper().writeValueAsString(resultMap));
            writer.flush();
            writer.close();
        }else {
            //携带了正确格式的token
            String token = header.replace("Bearer", "");
            //验证token是否正确
            Payload<SysUser> payload = JwtUtils.getInfoFromToken(token, rsaKeyProperties.getPublicKey(), SysUser.class);
            //获取到当前登录用户的信息
            SysUser userInfo = payload.getUserInfo();
            if(userInfo != null) {
                UsernamePasswordAuthenticationToken authResult = new UsernamePasswordAuthenticationToken(userInfo.getUsername(), "", userInfo.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authResult);
                chain.doFilter(request, response);
            }
        }

    }

}
