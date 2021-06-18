package com.sevak.info.controller;

import com.sevak.info.model.User;
import com.sevak.info.repo.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@Api(value = "User contacts")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    @ApiOperation(value = "Add User", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code =400, message = "Input is incorrect"),
            @ApiResponse(code =409, message = "User already exist"),
            @ApiResponse(code =200, message = "User create")
    })
    public ResponseEntity<String> addUser(@RequestBody User user){
        if(!user.getEmail().matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")){
            return new ResponseEntity<String>("Email is incorrect",HttpStatus.BAD_REQUEST );
        }
        //name 3-20 symbol
        if(!user.getName().matches("^[A-Z][A-Z,a-z]{3,20}")){
            return new ResponseEntity<>("Name is incorrect",HttpStatus.BAD_REQUEST );
        }

        User myUser = userRepository.findByEmail(user.getEmail()).orElse(null);
        if(myUser != null){
            return new ResponseEntity<>("User already exist", HttpStatus.CONFLICT);
        }else{
            userRepository.save(new User(user.getEmail(), user.getName()));
            return new ResponseEntity<>(HttpStatus.OK);
        }

    }

    @GetMapping
    @ApiOperation(value = "Return all User", response = Iterable.class)
    @ApiResponse(code =200, message = "List of Users")
    public ResponseEntity<List<User>> getAllUser(){
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{email}")
    @ApiOperation(value = "Return User by email", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User find"),
            @ApiResponse(code = 204, message = "User not exists")

    })
    public ResponseEntity<User> getUser(@PathVariable String email){
        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PatchMapping("/{userEmail}")
    @ApiOperation(value = "Change User email", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code =400, message = "Input is incorrect"),
            @ApiResponse(code =200, message = "User email change"),
            @ApiResponse(code = 204, message = "User not exists")
    })
    public ResponseEntity<String> changeUser(@RequestBody User userForEmail, @PathVariable String userEmail){
        User user = userRepository.findByEmail(userEmail).orElse(null);
        System.out.println(userForEmail.getEmail());
        if(user != null){
            if(!userForEmail.getEmail().matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")){
                return new ResponseEntity<String>("Email is incorrect",HttpStatus.BAD_REQUEST );
            }else {
                user.setEmail(userForEmail.getEmail());
                userRepository.save(user);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @DeleteMapping("/{email}")
    @ApiOperation(value = "Delete User by email", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code =200, message = "User deleted"),
            @ApiResponse(code = 204, message = "User not exists")
    })
    public ResponseEntity<String> deleteUser(@PathVariable String email){
        User user = userRepository.findByEmail(email).orElse(null);
        if(user!=null) {
            userRepository.delete(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
