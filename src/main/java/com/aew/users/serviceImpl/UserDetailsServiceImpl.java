package com.aew.users.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import com.aew.users.domain.Authority;
import com.aew.users.domain.User;
import com.aew.users.repository.UserRepository;

/**
*
* @author Adrian
*/


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
                    
        User user = userRepository.findByLogin(login);
        if (user == null) {
            System.out.println("User not found! " + login);
            throw new UsernameNotFoundException("User " + login + " was not found in the database");
        } else {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            for (Authority role : user.getAuthorities()){
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), grantedAuthorities);
        }
        
    }
}