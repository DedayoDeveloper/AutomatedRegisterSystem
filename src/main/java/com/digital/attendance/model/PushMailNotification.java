package com.digital.attendance.model;


import javax.persistence.*;

@Entity
@Table(name = "tbl_mailnotification")
public class PushMailNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "medicalcenter", length = 50)
    private String medicalcenter;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content", length = 500)
    private String content;

    @Column(name = "datesent")
    private String datesent;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMedicalcenter() {
        return medicalcenter;
    }

    public void setMedicalcenter(String medicalcenter) {
        this.medicalcenter = medicalcenter;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatesent() {
        return datesent;
    }

    public void setDatesent(String datesent) {
        this.datesent = datesent;
    }


}
