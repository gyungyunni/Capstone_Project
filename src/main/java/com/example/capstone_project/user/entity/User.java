package com.example.capstone_project.user.entity;

import com.example.capstone_project.diet.entity.MyDiet;
import com.example.capstone_project.global.entity.Base;
import com.example.capstone_project.user.dto.CustomUserDetails;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String phone;
    @Column(nullable = false)
    private String gender;

    private Float height;

    private Float weight;

    @Column(nullable = false)
    private Integer age;
    @Column(name = "img_url")
    private String imgUrl;

    @OneToMany
    @JoinColumn(name = "user_id", insertable = false, updatable = false) // diet 정보는 read-only
    private List<MyDiet> myDietList = new ArrayList<>();
    //get.myDietList를 했을 떄 nullpointException이 발생하지 않도록
    //new ArrayList<>()로 기본 리스트 생성

    @Builder
    public User(String username, String password, String email, String phone, String gender, Integer age, String imgUrl) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.age = age;
        this.imgUrl = imgUrl;
    }

    public static User fromUserDetails(CustomUserDetails userDetails) {
        return User.builder()
                .username(userDetails.getUsername())
                .password(userDetails.getPassword())
                .email(userDetails.getEmail())
                .phone(userDetails.getPhone())
                .gender(userDetails.getGender())
                .age(userDetails.getAge())
                .build();
    }

    public void update(CustomUserDetails request){
        this.email = request.getEmail();
        this.phone = request.getPhone();
        this.gender = request.getGender();
        this.age = request.getAge();
    }

    public void updateProfileImage(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
