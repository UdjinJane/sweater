package ru.udjin.sweater.controller;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import ru.udjin.sweater.domain.Role;
import ru.udjin.sweater.domain.User;
import ru.udjin.sweater.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
  @Autowired
  private UserRepo userRepo;


  @GetMapping("/registration")
  public String registration(Model model){
    model.addAttribute("message","");
    return "registration";
  }

  @PostMapping("/registration")
  public String addUser(User user, Map<String, Object> model){

    User userFromDb = userRepo.findByUsername(user.getUsername());
    if (userFromDb != null) {

      model.put("message", "User exists!");
      return "registration";


    }

    user.setActive(true);
    user.setRoles(Collections.singleton(Role.USER));
    userRepo.save(user);
    return "redirect:/login";

  }
}
