package ru.udjin.sweater.controller;

import ru.udjin.sweater.domain.Message;
import ru.udjin.sweater.repos.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class MainController {
  @Autowired
  private MessageRepo messageRepo;

  @GetMapping("/")
  public String greeting(Map<String, Object> model) {
  //  model.put("name", name);
    return "greeting";
  }

  @GetMapping("/main")
  public String main(Map<String, Object> model) {
    Iterable<Message> messages = messageRepo.findAll();

    model.put("messages", messages);
    model.put("findstring","");

    return "main";
  }

  @PostMapping("/main")
  public String add(@RequestParam String text, @RequestParam String tag, Map<String, Object> model) {
    Message message = new Message(text, tag);

    messageRepo.save(message);

    Iterable<Message> messages = messageRepo.findAll();

    model.put("messages", messages);

    return "main";
  }

  @PostMapping("filter")
  public String filter(@RequestParam String filter, Map<String, Object> model) {
    Iterable<Message> messages;
    String findstring = "all";

    if (filter.equals(findstring) || filter.isEmpty()) {
      messages = messageRepo.findAll();
    } else {
      messages = messageRepo.findByTag(filter);
    }

    //    if (filter != null && !filter.isEmpty()) {
    //    messages = messageRepo.findByTag(filter);
    //  } else {
    //    messages = messageRepo.findAll();
    //  }

    model.put("findstring", filter);
    model.put("messages", messages);

    return "main";
  }
}
