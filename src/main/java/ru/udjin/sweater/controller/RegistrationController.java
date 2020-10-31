package ru.udjin.sweater.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.udjin.sweater.domain.User;
import ru.udjin.sweater.service.UserService;

import java.util.Map;

@Controller
public class RegistrationController {
  @Autowired
  private UserService userService;


  @GetMapping("/registration")
  public String registration(Model model){
    model.addAttribute("message","");
    return "registration";
  }

  @PostMapping("/registration")
  public String addUser(User user, Map<String, Object> model){


    if (!userService.addUser(user)) {

      model.put("message", "User exists!");
      return "registration";
    }

    return "redirect:/login";

  }
  @GetMapping("/activate/{code}")
  public String activate(Model model, @PathVariable String code){
    // magic
    boolean isActivated = userService.activateUser(code);
    if (isActivated){
      model.addAttribute("message", "User activated successfully");
    } else {
      model.addAttribute("message", "Activeition code isn't found");
    }

    return "login";
  }
}
