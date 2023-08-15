package ru.maxim.cryptoalertbackend.services;


import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.maxim.cryptoalertbackend.exception.ResourceNotFoundException;
import ru.maxim.cryptoalertbackend.repository.UserRepository;

import static ru.maxim.cryptoalertbackend.utills.AppConstants.NAME;
import static ru.maxim.cryptoalertbackend.utills.AppConstants.USER;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(USER, NAME, username));
    }

}