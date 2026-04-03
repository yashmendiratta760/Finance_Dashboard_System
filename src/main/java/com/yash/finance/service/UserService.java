package com.yash.finance.service;

import com.yash.finance.dto.requestDTOs.CreateUserDTO;
import com.yash.finance.dto.requestDTOs.UpdateUserDTO;
import com.yash.finance.entities.UserEntity;
import com.yash.finance.repository.UserRepo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public UserEntity createUser(CreateUserDTO createUserDTO){
        UserEntity user = new UserEntity();
        user.setName(createUserDTO.getName());
        user.setEmail(createUserDTO.getEmail());
        user.setPass(createUserDTO.getPassword());
        user.setRole(createUserDTO.getRole());
        return userRepo.save(user);
    }

    public List<UserEntity> getAllUsers()
    {
        return userRepo.findAll();
    }

    public UserEntity getById(Long id){
        Optional<UserEntity> user = userRepo.findById(id);
        return user.orElseGet(UserEntity::new);
    }

    public UserEntity updateUser(UpdateUserDTO updateUserDTO){
        UserEntity user = userRepo.findById(updateUserDTO.getId())
                .orElseGet(UserEntity::new);
        if(updateUserDTO.getName()!=null) user.setName(updateUserDTO.getName());
        if(updateUserDTO.getEmail()!=null) user.setEmail(updateUserDTO.getEmail());
        if(updateUserDTO.getPass()!=null) user.setPass(updateUserDTO.getPass());
        if(updateUserDTO.getRole()!=null) user.setRole(updateUserDTO.getRole());
        if(updateUserDTO.getStatus()!=null) user.setStatus(updateUserDTO.getStatus());
        if(updateUserDTO.getRecords()!=null) user.setRecords(updateUserDTO.getRecords());
        return userRepo.save(user);
    }


    public UserEntity findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
