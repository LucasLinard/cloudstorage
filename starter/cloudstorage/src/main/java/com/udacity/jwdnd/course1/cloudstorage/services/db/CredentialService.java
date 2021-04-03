package com.udacity.jwdnd.course1.cloudstorage.services.db;

import com.udacity.jwdnd.course1.cloudstorage.Mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.Model.db.Credential;
import com.udacity.jwdnd.course1.cloudstorage.Model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    public List<Credential> getCredentialList(Integer userId) {
        return this.credentialMapper.getAllCredentials(userId);
    }

    public Integer addCredential(CredentialForm credoForm, Integer userId) {
        SecureRandom random=new SecureRandom();
        byte [] key= new byte[16];
        String encodedKey= Base64.getEncoder().encodeToString(key);
        EncryptionService encryptionService= new EncryptionService();
        String encodedPassword=encryptionService.encryptValue(credoForm.getPassword(),encodedKey);
        Credential credential = new Credential();
        credential.setUrl(credoForm.getUrl());
        credential.setUserName(credoForm.getUserName());
        credential.setKey(encodedKey);
        credential.setPassword(encodedPassword);
        credential.setUserId(userId);
        return this.credentialMapper.insert(credential);
    }

    public void updateCredential(CredentialForm credoForm) {
        SecureRandom random=new SecureRandom();
        byte [] key= new byte[16];
        String encodedKey= Base64.getEncoder().encodeToString(key);
        EncryptionService encryptionService= new EncryptionService();
        String encodedPassword=encryptionService.encryptValue(credoForm.getPassword(),encodedKey);
        this.credentialMapper.updateCredential(credoForm.getCredentialId(), credoForm.getUrl(), credoForm.getUserName(), encodedKey, encodedPassword);
    }

    public Integer deleteCredential(Integer credentialId, Integer userId) {
     return this.credentialMapper.deleteCredential(credentialId, userId);
    }
}
