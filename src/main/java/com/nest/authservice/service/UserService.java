package com.nest.authservice.service;

import com.nest.authservice.exception.EmailAlreadyExistsException;
import com.nest.authservice.exception.NoChangesDetectedException;
import com.nest.authservice.exception.RoleNotFoundException;
import com.nest.authservice.exception.UserNotFoundException;
import com.nest.authservice.model.*;
import com.nest.authservice.repository.RolesRepository;
import com.nest.authservice.repository.UsersRepository;
import com.nest.authservice.util.UsersUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.nest.authservice.util.Base64Util.getBase64DecodedPassword;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final RolesRepository rolesRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Value("${default.role.id}")
    private Long DefaultRoleId;

    public Optional<ResponseUserDTO> signUp(RequestSignupUser requestSignupUser) {
        if(usersRepository.findByEmail(requestSignupUser.getEmail()).isPresent()){
            log.warn("User with email: {} already exists", requestSignupUser.getEmail());
            throw new EmailAlreadyExistsException("Email '"+ requestSignupUser.getEmail()+"' already in use.");
        }
        requestSignupUser.setRoles(new HashSet<>(getDefaultRoleIds()));
        requestSignupUser.setPassword(passwordEncoder.encode(getBase64DecodedPassword(requestSignupUser.getPassword())));
        Users savedUser = usersRepository.save(convertToUser(requestSignupUser));
        ResponseUserDTO responseUserDTO = convertToResponseUserDTO(savedUser);
        return Optional.of(responseUserDTO);
    }


    private Set<Long> getDefaultRoleIds() {
        return Set.of(DefaultRoleId);
    }

    private Roles getDefaultRole() {
        return rolesRepository.findById(DefaultRoleId).orElseThrow(()->new RoleNotFoundException("Default role not found"));
    }

    private Users convertToUser(RequestSignupUser requestSignupUser){
        Users users = modelMapper.map(requestSignupUser, Users.class);
        users.setRoles(new HashSet<>(rolesRepository.findAllById(requestSignupUser.getRoles())));
        return users;
    }
    private ResponseUserDTO convertToResponseUserDTO(Users users){
        return modelMapper.map(users, ResponseUserDTO.class);
    }

    public ResponseUserDTO getUser() {
        Optional<Users> user = UsersUtil.getUserFromAuthentication();
        return convertToResponseUserDTO(user.orElseThrow(()->new UserNotFoundException("User not found. Authentication required.")));
    }

    public List<ResponseUserDTO> getAllUsers() {
        return usersRepository.findAll().stream()
                .map(this::convertToResponseUserDTO).toList();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usersRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: "+email));
    }

    public Users getUserByEmail(String email) {
        return usersRepository.findByEmail(email).orElseThrow(()->new UserNotFoundException("User not found with email: "+email));
    }

    public Optional<ResponseUserDTO> updateUser(RequestUpdateUser requestUpdateUser) {
        Users user = usersRepository.findByEmail(requestUpdateUser.getEmail()).orElseThrow(()-> new UserNotFoundException("User with email:"+requestUpdateUser.getEmail()+" not found"));
        user.setUpdatedAt(LocalDateTime.now());
        boolean isUserChanged = false;
        if(requestUpdateUser.getFirstName() != null && !requestUpdateUser.getFirstName().isBlank() && !requestUpdateUser.getFirstName().equals(user.getFirstName())){
            isUserChanged = true;
            user.setFirstName(requestUpdateUser.getFirstName());
        }
        if(requestUpdateUser.getLastName() != null && !requestUpdateUser.getLastName().isBlank() && !requestUpdateUser.getLastName().equals(user.getLastName())){
            isUserChanged = true;
            user.setLastName(requestUpdateUser.getLastName());
        }
        if (requestUpdateUser.getRoles() != null && !requestUpdateUser.getRoles().isEmpty()) {
            Set<Roles> newRoles = new HashSet<>(rolesRepository.findAllById(requestUpdateUser.getRoles()));
            Set<Roles> currentRoles = user.getRoles();

            // Check if roles changed
            if (!newRoles.equals(currentRoles)) {
                isUserChanged = true;
                user.setRoles(newRoles);
            }
        }
        if(!isUserChanged){
            throw new NoChangesDetectedException("User has no changes");
        }
        return Optional.of(convertToResponseUserDTO(usersRepository.save(user)));
    }

    public void deleteById(Long id) {
        usersRepository.deleteById(id);
    }
}
