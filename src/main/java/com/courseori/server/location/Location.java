package com.courseori.server.location;

import com.courseori.server.item.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long locationId;

    private String name;

    private String eng;

//    @OneToMany(mappedBy = "location")
//    private List<Item> items = new ArrayList<>();

}
