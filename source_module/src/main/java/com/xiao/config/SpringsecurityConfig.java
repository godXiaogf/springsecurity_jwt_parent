package com.xiao.config;

import com.xiao.filter.JwtVerifyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringsecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private RsaKeyProperties rsaKeyProperties;

    //Spring Security配置
    public void configure(HttpSecurity hs) throws Exception {
        hs.csrf()
            .disable()
            .authorizeRequests()
            .antMatchers("/**").hasAnyRole("NORMAL")
            .anyRequest().authenticated()

            .and()
            .addFilter(new JwtVerifyFilter(super.authenticationManager(), rsaKeyProperties))
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); //分布式不需要session
    }
}
