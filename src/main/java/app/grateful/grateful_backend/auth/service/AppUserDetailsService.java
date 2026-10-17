package app.grateful.grateful_backend.auth.service;

import app.grateful.grateful_backend.auth.domain.AppUserDetails;
import app.grateful.grateful_backend.common.ApiException;
import app.grateful.grateful_backend.user.domain.User;
import app.grateful.grateful_backend.user.infra.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new ApiException(
                HttpStatus.NOT_FOUND, "INVALID_EMAIL", "user does not exist: "+username
        ));
        if(user.getPasswordHash() == null) throw new ApiException(HttpStatus.UNAUTHORIZED, "INVALID_PASSWORD", "password does not exist: "+username);
        return new AppUserDetails(user.getId(), user.getEmail(), user.getPasswordHash(), user.isEmailVerified(), user.getDisplayName());
    }
}
