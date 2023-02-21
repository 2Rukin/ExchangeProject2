package com.exchangeinformat.userprofile.controllers;

import com.exchangeinformat.userprofile.entity.User;
import com.exchangeinformat.userprofile.entityDTO.UserDTO;
import com.exchangeinformat.userprofile.mappers.UserMappers;
import com.exchangeinformat.userprofile.repository.UserInfoRepository;
import com.exchangeinformat.userprofile.service.UserService;
import com.exchangeinformat.userprofile.util.Data;
import com.exchangeinformat.userprofile.util.ValidationResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.cloud.stream.function.StreamBridge;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class RestUserController {
    private final UserService userService;
    private final RabbitTemplate template;
    private StreamBridge streamBridge;

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public RestUserController(UserService userService, RabbitTemplate template, StreamBridge streamBridge, UserInfoRepository userInfoRepository) {
        this.userService = userService;
        this.template = template;
        this.streamBridge = streamBridge;
        this.userInfoRepository = userInfoRepository;
    }

    @GetMapping("/findOne")
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<User> getUser(Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<HttpStatus> createUser(@RequestBody User user) {
        userService.createUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<HttpStatus> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping
    @RolesAllowed({"ADMIN"})
    public ResponseEntity<HttpStatus> deleteUser(Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/home")
    @RolesAllowed({"ADMIN", "USER"})
    public ResponseEntity<User>  getUserDetails(Principal principal) {
        Map<String, Object> cl = getExtID(principal);
        User user;
        String extId = cl.get("sub").toString();
        if (!userService.isUserPresent(extId)) {
            user = User.builder()
                .extId(extId)
                .username(cl.get("preferred_username").toString())
                .firstName(cl.get("given_name").toString())
                .lastName(cl.get("family_name").toString())
                .email(cl.get("email").toString())
                .build();
            userService.createUser(user);
        } else {
            user = userService.getUserByExtId(extId);
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/update")
    @RolesAllowed({"USER"})
    public ResponseEntity<ValidationResponse> getWord(Principal principal, @RequestBody @Valid UserDTO userDTO) {
        Map<String, Object> cl = getExtID(principal);
        String extId = cl.get("sub").toString();
        if (!userService.isUserPresent(extId) || !extId.equals(userDTO.getExtId())) { //Проверка, что пользователь существует с таким extID и проверка равенства extID principal и переданного юзера
            return ResponseEntity.status(500).body(new ValidationResponse(new Data("Не удалось обновить пользователя")));
        } else {
            userService.updateUser(UserMappers.INSTANCE.userDTOToEntity(userDTO));
            return ResponseEntity.status(200).body(new ValidationResponse(new Data("Данные успешно сохранены")));
        }
    }

    @GetMapping(value = "/getInfo")
    @RolesAllowed({"USER"})
    public String getInfo(Principal principal) {
        Map<String, Object> cl = getExtID(principal);
        String extId = cl.get("sub").toString();
        if (userInfoRepository.findById(extId).isPresent()) {
            if (ChronoUnit.HOURS.between(userInfoRepository.findById(extId).get().getLastRequest(), LocalDateTime.now()) < 1) {
                return "ИНФО ИЗ БАЗЫ USER: " + userInfoRepository.findById(extId).get().toString();
            }
        }
        streamBridge.send("producer-out-0", extId);
        return "ИНФО ИЗ quotes: " + userInfoRepository.findById(extId).get().toString();
    }

    private Map<String, Object> getExtID(Principal principal) {
        JwtAuthenticationToken kp = (JwtAuthenticationToken) principal;
        Jwt token = kp.getToken();
        return token.getClaims();
    }
}
