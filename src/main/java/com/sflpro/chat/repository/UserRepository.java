package com.sflpro.chat.repository;

import com.sflpro.chat.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role = 'USER'")
    List<User> findAllByRoleUser();

    @Transactional
    void deleteUserByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role = 'USER' and u.email != ?1")
    List<User> findAllUsersByRoleUserAndEmailNotEqual(String email);
}
