package com.sflpro.chat.service.impl;

import com.sflpro.chat.model.User;
import com.sflpro.chat.repository.UserRepository;
import com.sflpro.chat.service.UserService;
import com.sflpro.chat.util.SecurePasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;

import static com.sflpro.chat.util.Constants.FORGET_PASSWORD_EMAIL_TEMPLATE_NAME;
import static sun.security.x509.X509CertImpl.INFO;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private EmailServiceImpl emailService;

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public void save(User user) {
        repository.save(user);
    }

    @Override
    public List<User> findAllByUserRole() {
        return repository.findAllByRoleUser();
    }

    @Override
    public void removeByEmail(String email) {
        repository.deleteUserByEmail(email);
    }

    @Override
    public boolean isUpdatedForgottenPassword(String email) throws MessagingException {
        User user = repository.findByEmail(email);
        if (user != null) {
            String password = SecurePasswordGenerator.generatePassword();
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", user.getFirstName());
            map.put(INFO, "This is your new password");
            map.put("password", password);
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            repository.save(user);
            emailService.sendEmail(emailService.buildEmailMessageModel(user, "Password Updating", map), FORGET_PASSWORD_EMAIL_TEMPLATE_NAME);
            return true;
        }
        return false;
    }

    @Override
    public List<User> findAllUsersByRoleUserAndEmailNotEqual(String email) {
        return repository.findAllUsersByRoleUserAndEmailNotEqual(email);
    }
}
