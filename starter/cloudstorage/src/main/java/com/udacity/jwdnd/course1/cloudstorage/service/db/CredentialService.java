package com.udacity.jwdnd.course1.cloudstorage.service.db;

import com.udacity.jwdnd.course1.cloudstorage.mapper.db.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.db.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.form.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.service.EncryptionService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
  private final CredentialMapper credentialMapper;

  public CredentialService(CredentialMapper credentialMapper) {
    this.credentialMapper = credentialMapper;
  }

  public List<Credential> getCredentialList(Integer userId) {
    return this.credentialMapper.getAllCredentials(userId);
  }

  public Integer addCredential(CredentialForm credoForm, Integer userId) {
    SecureRandom random = new SecureRandom();
    byte[] key = new byte[16];
    String encodedKey = Base64.getEncoder().encodeToString(key);
    EncryptionService encryptionService = new EncryptionService();
    String encodedPassword = encryptionService.encryptValue(credoForm.getPassword(), encodedKey);
    Credential credential = new Credential();
    credential.setUrl(credoForm.getUrl());
    credential.setUsername(credoForm.getUserName());
    credential.setKey(encodedKey);
    credential.setPassword(encodedPassword);
    credential.setUserId(userId);
    return this.credentialMapper.insert(credential);
  }

  public void updateCredential(CredentialForm credoForm) {
    SecureRandom random = new SecureRandom();
    byte[] key = new byte[16];
    String encodedKey = Base64.getEncoder().encodeToString(key);
    EncryptionService encryptionService = new EncryptionService();
    String encodedPassword = encryptionService.encryptValue(credoForm.getPassword(), encodedKey);
    this.credentialMapper.updateCredential(
        credoForm.getCredentialId(),
        credoForm.getUrl(),
        credoForm.getUserName(),
        encodedKey,
        encodedPassword);
  }

  public Integer deleteCredential(Integer credentialId, Integer userId) {
    return this.credentialMapper.deleteCredential(credentialId, userId);
  }
}
