package ru.udjin.sweater.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.udjin.sweater.domain.Role;
import ru.udjin.sweater.domain.User;
import ru.udjin.sweater.repos.UserRepo;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
// Если прописать реквест на уровне класса, то нет необходимости прописывать меппинг на
// уровне методов класса
@RequestMapping("/user")
// чтобы юзера без ролей админа не могли попасть в форму админскую
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
  @Autowired
  private UserRepo userRepo;

  @GetMapping
  public String userList(Model model){
    //вернет файл, см в ресурсах
    model.addAttribute("users",userRepo.findAll());

    return "userList";
  }

  @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model){
    model.addAttribute("user", user);
    model.addAttribute("roles", Role.values());
    return "userEdit";

  }
  @PostMapping
  public String userSave(
          @RequestParam String username,
          @RequestParam Map<String, String> form,
          @RequestParam("userId") User user
  ){
    user.setUsername(username);
    // берем список ролей и превращаем в стринг
    Set<String> roles =Arrays.stream(Role.values())
            .map(Role::name)
            .collect(Collectors.toSet());
    // проверяем что форма содержит список ролей
    // добавление роли сработает , если роль юзера пустая
    // поэтому ее сначала очищаем

    user.getRoles().clear();

    for (String key : form.keySet()){
      if (roles.contains(key)){
        user.getRoles().add(Role.valueOf(key));
      }
    }

    userRepo.save(user);

    return "redirect:/user";
  }



}
