package by.kudko.controller;

import by.kudko.domain.Role;
import by.kudko.domain.User;
import by.kudko.repository.UserRepository;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "userList";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PostMapping()
    public String userSave(
            @RequestParam String usermane,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ){
        user.setUsername(usermane);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());
        //before edit roles, clear user's roles
        user.getRoles().clear();
        // в форме много полей. Фильтруем только роли
        for (String key : form.keySet()) {
            // If some of roles present , add to user
            if (roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }

        }
        userRepository.save(user);
        return "redirect:/user";
    }

}
