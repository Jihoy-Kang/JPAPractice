package com.courseori.server.member.entity;


import com.courseori.server.item.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    private String name;

    private String phone;

    @ElementCollection(fetch = FetchType.EAGER) //@ElementCollection 애너테이션을 이용해 사용자 등록 시, 사용자의 권한을 등록하기 위한 권한 테이블을 생성합니다.
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Item> itemList = new ArrayList<>();

    public Member(String email, String name, String phone, String password) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.password = password;
    }
}
