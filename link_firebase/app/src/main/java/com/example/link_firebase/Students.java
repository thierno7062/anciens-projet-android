package com.example.link_firebase;

public class Students {

    String name, rollno, course;

    public Students(String name, String rollno, String course) {
        this.name = name;
        this.rollno = rollno;
        this.course = course;
    }

    public String getName() {
        return name;
    }

    public String getRollno() {
        return rollno;
    }

    public String getCourse() {
        return course;
    }
}
