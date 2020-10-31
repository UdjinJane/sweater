package ru.udjin.sweater.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.udjin.sweater.domain.Role;
import ru.udjin.sweater.domain.User;
import ru.udjin.sweater.repos.UserRepo;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
  @Autowired
  private UserRepo userRepo;

  @Autowired
  private MailSender mailSender;

//  public UserService(UserRepo userRepo) {
//    this.userRepo = userRepo;
//  }
//
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepo.findByUsername(username);
  }

  // сервис добавления пользователя
  public boolean addUser(User user) {
    User userFromDb = userRepo.findByUsername(user.getUsername());

    // если юзер есть в базе, то сообщаем новому юзеру что он есть
    if (userFromDb !=null){
      return false;
    }
    user.setActive(true);
    user.setRoles(Collections.singleton(Role.USER));
    user.setActivationCode(UUID.randomUUID().toString());

    userRepo.save(user);

    if (!StringUtils.isEmpty(user.getEmail())) {
      String message = String.format(
              "Hello, %s! \n" +
                      "Welcome to Sweater. \n" +
                      "Please click link to complete " +
                      "registration: http://localhost:8081/activate/%s ",
              user.getUsername(),
              user.getActivationCode()
      );
        mailSender.send(user.getEmail(), "Activation code", message);
    }

    return true;
  }

  public boolean activateUser(String code) {
    User user = userRepo.findByActivationCode(code);
    if (user == null){
      return false;

    }
    user.setActivationCode(null);
    userRepo.save(user);
    return true;
  }
}
