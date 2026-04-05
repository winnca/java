package ru.winnca.spring_security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.winnca.spring_security.config.MyUserDetails;
import ru.winnca.spring_security.models.MyUser;
import ru.winnca.spring_security.repository.UserRepository;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> user = repository.readByUsername(username);
        return user.map(MyUserDetails::new).orElseThrow(()->new UsernameNotFoundException(username + "not found"));
    }
}
