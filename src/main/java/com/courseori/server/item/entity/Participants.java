package com.courseori.server.item.entity;

import com.courseori.server.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Participants {

    @Id
    private long participantId;

    private int type;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    private Item item;

/*    public void addMember(Member member){
        this.member = member;
        if(!this.member.getParticipants().contains(this)){
            this.member.getParticipants().add(this);
        }
    }*/


}
