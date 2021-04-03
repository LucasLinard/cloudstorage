package com.udacity.jwdnd.course1.cloudstorage.controller.db;

import com.udacity.jwdnd.course1.cloudstorage.model.form.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.model.db.Credential;
import com.udacity.jwdnd.course1.cloudstorage.service.db.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.service.db.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CredentialController {
    private UserService userService;
    private CredentialService credentialService;

    public CredentialController(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @GetMapping("/credential")
    public String getCredentialList(@ModelAttribute("credentialForm") CredentialForm credentialForm, Model model) {
        model.addAttribute("SavedCredentials", credentialService.getCredentialList(credentialForm.getUserId()));
        return "home";
    }

    @PostMapping("/credential")
    public ModelAndView addCredential(@ModelAttribute("credentialForm") CredentialForm credentialForm, Authentication auth, ModelMap modelMap) {
        String saveError = null;
        User user = this.userService.getUserByUsername(auth.getName());
        if (credentialForm.getCredentialId() == null) {
            if (this.credentialService.addCredential(credentialForm, user.getUserId()) == 1) {
                modelMap.addAttribute("credoSuccessBool", true);
                modelMap.addAttribute("credoSuccess", "Your credential has been saved successfully! ");
            } else {
                modelMap.addAttribute("credoErrorBool", true);
                modelMap.addAttribute("credoError", "Something went wrong, please try again! ");
            }
        } else {
            this.credentialService.updateCredential(credentialForm);
            modelMap.addAttribute("credoSuccessBool", true);
            modelMap.addAttribute("credoSuccess", "Your credential has been saved successfully! ");
        }
        modelMap.addAttribute("SavedCredentials", this.credentialService.getCredentialList(user.getUserId()));
        return new ModelAndView("forward:/result", modelMap);
    }

    @GetMapping("/credential-delete")
    public ModelAndView deleteCredential(@ModelAttribute("credentialForm") CredentialForm credentialForm, Authentication auth, ModelMap attributes) {
        User user = this.userService.getUserByUsername(auth.getName());
        for (Credential credential : this.credentialService.getCredentialList(user.getUserId())) {
            if (credential.getUrl().equals(credentialForm.getUrl())) {
                if (this.credentialService.deleteCredential(credential.getCredentialId(), user.getUserId()) == 1) {
                    attributes.addAttribute("credoSuccessBool", true);
                    attributes.addAttribute("CredoSuccess","Your credential has been saved successfully");
                } else {
                    attributes.addAttribute("credoErrorBool", true);
                    attributes.addAttribute("CredoError","Something went wrong, please try again");
                }
            }
        }
        attributes.addAttribute("SavedCredentials", credentialService.getCredentialList(user.getUserId()));
        return new ModelAndView("forward:/result", attributes);
    }
}
