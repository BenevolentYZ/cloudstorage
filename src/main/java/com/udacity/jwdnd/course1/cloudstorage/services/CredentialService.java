package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentials(Integer userId) {
        return this.credentialMapper.getCredentials(userId);
    }

    public int createCredential(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setPassword(encryptedPassword);
        credential.setKey(encodedKey);
        return credentialMapper.addCredential(credential);
    }

    public int deleteCredential(Integer credentialId, Integer userId) {
        return credentialMapper.deleteCredential(credentialId, userId);
    }

    public int updateCredential(Integer credentialId, String url, String username, String password, Integer userId) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
        return this.credentialMapper.updateCredential(credentialId, url, username, encodedKey, encryptedPassword, userId);
    }

    //used in home.html to show decrypted password in modal
    public Credential getCredentialByCredentialId(Integer credentialId, Integer userId) {
        return credentialMapper.getCredential(credentialId, userId);
    }

    public String getDecryptedPassword(Credential credential) {
        return encryptionService.decryptValue(credential.getPassword(), credential.getKey());
    }
}
