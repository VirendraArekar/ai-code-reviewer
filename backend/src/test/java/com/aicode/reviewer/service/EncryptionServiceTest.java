package com.aicode.reviewer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        encryptionService = new EncryptionService("test-secret-key-for-aes-256-gcm-encryption!");
    }

    @Test
    void shouldEncryptAndDecryptPlainTextCorrectly() {
        String originalKey = "sk-proj-live-google-gemini-secret-api-key-1234567890";
        String cipherText = encryptionService.encrypt(originalKey);

        assertNotNull(cipherText);
        assertNotEquals(originalKey, cipherText);

        String decrypted = encryptionService.decrypt(cipherText);
        assertEquals(originalKey, decrypted);
    }

    @Test
    void shouldProduceDifferentCipherTextsForSameInputDueToRandomIv() {
        String input = "same-input-data";
        String cipher1 = encryptionService.encrypt(input);
        String cipher2 = encryptionService.encrypt(input);

        assertNotEquals(cipher1, cipher2);
        assertEquals(input, encryptionService.decrypt(cipher1));
        assertEquals(input, encryptionService.decrypt(cipher2));
    }
}
