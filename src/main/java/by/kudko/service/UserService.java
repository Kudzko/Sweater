package by.kudko.service;

import by.kudko.domain.Role;
import by.kudko.domain.User;
import by.kudko.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Value("${mail.host}")
    private String host;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailSender mailSender;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        userRepository.save(user);
        sendMessage(user);
        return true;
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Sweater. Please, visit next link: " +
                            host + "/activate/%s",
                    user.getUsername(),
                    user.getActivationCode());
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null){
            return false;
        }

        user.setActivationCode(null);
        userRepository.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void saveUser(User user, String usermane, Map<String, String> form) {
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
    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();
       boolean isEmailChanged =  (email != null && !email.equals(userEmail))
                || (userEmail != null && !userEmail.equals(email));

       if (isEmailChanged){
           user.setEmail(email);

           if (!StringUtils.isEmpty(email)){
               user.setActivationCode(UUID.randomUUID().toString());
           }
       }

       if (!StringUtils.isEmpty(password)){
           user.setPassword(password);
       }
       userRepository.save(user);

       if (isEmailChanged){
           sendMessage(user);
       }
    }
}
