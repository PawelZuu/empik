package pl.empik.rest.userrestservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.empik.rest.userrestservice.dto.UserResponseDto;
import pl.empik.rest.userrestservice.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/{login}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable String login) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("UserData", "/users" + "/" + login);
        return new ResponseEntity<>(userService.getUserByLogin(login).get(), httpHeaders, HttpStatus.OK);
    }

}
