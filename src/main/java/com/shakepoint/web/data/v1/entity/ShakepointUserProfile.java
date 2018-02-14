package com.shakepoint.web.data.v1.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "Profile")
@Table(name = "user_profile")
public class ShakepointUserProfile {
    @Id
    private String id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private ShakepointUser user;

    @Column(name = "age")
    private int age;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "weight")
    private double weight;

    @Column(name = "height")
    private double height;

    public ShakepointUserProfile() {
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ShakepointUser getUser() {
        return user;
    }

    public void setUser(ShakepointUser user) {
        this.user = user;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
