package com.courseori.server.item.entity;


import com.courseori.server.location.Location;
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
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long itemId;

//    @ManyToOne
//    @JoinColumn(name = "MEMBER_ID")
//    private long memberId;

    private String title;

    private String body;

    @ManyToOne(cascade = CascadeType.ALL) // 영속성 설정 item저장시 위치정보 동시 저장
    @JoinColumn(name = "FROM_LOCATION_ID")
    private Location fromLocation;  //단방향 n:1 매핑 1

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TO_LOCATION_ID")
    private Location toLocation; //단방향 n:1 매핑 1

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Participants> participants = new ArrayList<>();


}
