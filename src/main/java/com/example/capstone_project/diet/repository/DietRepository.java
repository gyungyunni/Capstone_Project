package com.example.capstone_project.diet.repository;

import com.example.capstone_project.diet.entity.MyDiet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietRepository extends JpaRepository<MyDiet, Long> {
}
