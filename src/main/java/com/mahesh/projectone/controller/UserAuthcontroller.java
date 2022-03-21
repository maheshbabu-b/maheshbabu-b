package com.mahesh.projectone.controller;


import javax.servlet.ServletException;

import com.mahesh.projectone.model.User;
import com.mahesh.projectone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("/pro")
public class UserAuthcontroller {

    // Defining expiry time of the JWTtoken in milliseconds
    long expireTime = 1200000;

    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user){
        boolean bool=userService.addUser(user);
        if(bool){
            return new ResponseEntity<String>("ok", HttpStatus.CREATED);
        }
        else
            return new ResponseEntity<String>("no",HttpStatus.CONFLICT);

    }

    @DeleteMapping("/user")
    public ResponseEntity<String> delUser(@RequestParam String username){
        if(userService.deleteUser(username)) {
            return new ResponseEntity<String>("ok", HttpStatus.OK);
        }
        else return new ResponseEntity<String>("no", HttpStatus.NOT_FOUND);

    }
    @PutMapping("/user")
    public ResponseEntity<String> update(@RequestBody Map<String,String>json){
        if(userService.updateUser(json.get("username"),json.get("oldpassword"),json.get("newpassword"))){
            return new ResponseEntity<String>("ok",HttpStatus.OK);
        }
        else
            return new ResponseEntity<String>("no",HttpStatus.NOT_FOUND);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {

        String jwtToken = "";
        Map<String,String> map = new HashMap<>();

        try {

            // Calling the getToken method as written below
            jwtToken = getToken(user.getUsername(),user.getPassword());
            map.clear();
            map.put("message", "User logged in successfully");
            map.put("Token",jwtToken);

        }catch(Exception e) {

            // In case of exception returning the error message and the null in place of token
            map.clear();
            map.put("message",e.getMessage());
            map.put("Token",null);
        }
        return new ResponseEntity<>(map,HttpStatus.OK);
    }



    public String getToken(String username,String password) throws Exception {

        // If either of user name or password field is empty it will throw exception
        if(username == null || password == null) {
            throw new ServletException("Please fill the Username and Password");
        }

        // Calling the validate(String username, String password) of return type boolean
        boolean status = userService.validate(username,password);

        // Throwing exception if the user is not a valid user
        if(!status) {
            throw new ServletException("Invalid Credentials");
        }

        // Generating token only when the user is validated
        String jwtToken = Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ expireTime))
                .signWith(SignatureAlgorithm.HS256, "MusixAppkey").compact();
        return jwtToken;
    }


}
