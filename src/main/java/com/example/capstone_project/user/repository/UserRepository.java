package com.example.capstone_project.user.repository;



import com.example.capstone_project.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}