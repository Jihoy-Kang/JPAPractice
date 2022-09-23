package com.courseori.server.member.entity;

import com.courseori.server.item.entity.Item;
import com.courseori.server.item.entity.Participants;
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

    private String email;

    private String name;

    private String phone;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Participants> participants = new ArrayList<>();

//    @OneToMany(mappedBy = "member", cascade = CascadeType.PERSIST)
//    private List<Item> items = new ArrayList<>();


}
