package com.xiao.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RsaUtilsTest {
    private String privateFilePath = "G:\\temp\\authRsa\\rsa_id_key";
    private String publicFilePath = "G:\\temp\\authRsa\\rsa_id_key.pub";

    @Test
    void getPublicKey() throws Exception {
        System.out.println(RsaUtils.getPublicKey(publicFilePath));
    }

    @Test
    void getPrivateKey() throws Exception {
        System.out.println(RsaUtils.getPrivateKey(privateFilePath));
    }

    @Test
    void generateKey() throws Exception {
        RsaUtils.generateKey(publicFilePath, privateFilePath, "godxiao", 2048);
    }
}