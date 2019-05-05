package com.aew.users.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.aew.users.domain.User;
import com.aew.users.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    
@RequestMapping(value="/welcome", method=RequestMethod.GET)
public String welcome() {
    return "BIENVENIDO";
}

@Secured("ADMIN")
@RequestMapping(value="/users", method=RequestMethod.GET)
public String getUsers() {
    return "Estas listando todos los users";
}

@RequestMapping(value="/accessDenied", method=RequestMethod.GET)
public String permisosInsuficientes() {
    return "No Tenes los permisos";
}

@PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
@RequestMapping(value="/user/{id}", method=RequestMethod.GET)
public String getUser(@PathVariable("id") long id) {
    return "Estas viendo el usuario " + id;
}

@RequestMapping(value="/user/{id}", method=RequestMethod.DELETE)
public String deleteUser(@PathVariable("id") long id) {
    return "Estas borrando el usuario " + id;
}

@RequestMapping(value="/user", method=RequestMethod.POST)
public String addUser(@Valid @RequestBody User user) {
    user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
    userService.createUser(user);
    return "Estas creando un nuevo user";
}

@RequestMapping(value="/logout", method = RequestMethod.GET)
public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
    /*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null){    
        new SecurityContextLogoutHandler().logout(request, response, auth);
    }*/
    return "Te has deslogueado";
}

@RequestMapping(value = "/info", method = RequestMethod.GET)
public String userInfo(Authentication authentication) {
    if (authentication == null)
        {
            return "Nadie esta logueado";
    }
    else 
        return "estas logueado como " + authentication.getName();
	}

}