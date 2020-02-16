package com.sflpro.chat.service;

import com.sflpro.chat.model.User;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {
    User findByEmail(String email);

    void save(User user);

    List<User> findAllByUserRole();

    void removeByEmail(String email);

    boolean isUpdatedForgottenPassword(String email) throws MessagingException;

    List<User> findAllUsersByRoleUserAndEmailNotEqual(String email);
}
