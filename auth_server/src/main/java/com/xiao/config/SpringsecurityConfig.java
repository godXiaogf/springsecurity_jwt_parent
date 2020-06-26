package com.xiao.config;

import com.xiao.filter.JwtLoginFilter;
import com.xiao.filter.JwtVerifyFilter;
import com.xiao.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringsecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SysUserService userService;
    @Autowired
    private RsaKeyProperties rsaKeyProperties;

    //配置加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //spring security 内置加密算法
    }

    //认证用户的来源
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    //Spring Security配置
    public void configure(HttpSecurity hs) throws Exception {
        hs.csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/**").hasAnyRole("NORMAL")
            .anyRequest().authenticated()

            .and()
            .addFilter(new JwtLoginFilter(super.authenticationManager(), rsaKeyProperties))
            .addFilter(new JwtVerifyFilter(super.authenticationManager(), rsaKeyProperties))
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //分布式不需要session
    }
}
