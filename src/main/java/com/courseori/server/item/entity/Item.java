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

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<Participants> participants = new ArrayList<>();


}
