package in.virajraut.foodiesapi.service;

import in.virajraut.foodiesapi.entity.UserEntity;
import in.virajraut.foodiesapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

//Created for login API
@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    public final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity existingUser = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found."));
        return new User(existingUser.getEmail(), existingUser.getPassword(), Collections.emptyList()); //'User' is the implemented class of 'UserDetails' interface. Also, passing empty list will use for roles later.
    }
}
