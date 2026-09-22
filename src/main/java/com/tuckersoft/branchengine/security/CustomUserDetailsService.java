package com.tuckersoft.branchengine.security;
import com.tuckersoft.branchengine.model.User;
import com.tuckersoft.branchengine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service @RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override public UserDetails loadUserByUsername(String u) throws UsernameNotFoundException {
        User x = userRepository.findByUsername(u).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + u));
        return new CustomUserDetails(x);
    }
}
