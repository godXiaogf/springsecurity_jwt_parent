package com.xiao.config;

import com.xiao.utils.RsaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.PrivateKey;
import java.security.PublicKey;

@Component
public class RsaKeyProperties {

    @Autowired
    RsaPath rsaPath;

    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct     //该注释确保publicKeyPath和privateKeyPath都有值后才执行该方法
    public void getRsaKey() throws Exception {
        System.out.println(rsaPath.getPublicKeyPath());
        publicKey = RsaUtils.getPublicKey(rsaPath.getPublicKeyPath());
        privateKey = RsaUtils.getPrivateKey(rsaPath.getPrivateKeyPath());
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}

