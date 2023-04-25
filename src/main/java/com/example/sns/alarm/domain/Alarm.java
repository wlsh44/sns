package com.example.sns.alarm.domain;

import com.example.sns.common.entity.BaseTimeEntity;
import com.example.sns.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Alarm extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member target;

    private boolean read;

    protected Alarm(Member target) {
        this.target = target;
        this.read = false;
    }
}
