package com.tracec.data;

public class Patient {
    private int id;
    private String patientid;
    private String surname;
    private String givenname;
    private String password;
    private String birth;
    private String clinicianid;
    private String clinicianname;
    private String guardianname;
    private String guardianphone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenname() {
        return givenname;
    }

    public void setGivenname(String givenname) {
        this.givenname = givenname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getClinicianid() {
        return clinicianid;
    }

    public void setClinicianid(String clinicianid) {
        this.clinicianid = clinicianid;
    }

    public String getClinicianname() {
        return clinicianname;
    }

    public void setClinicianname(String clinicianname) {
        this.clinicianname = clinicianname;
    }

    public String getGuardianname() {
        return guardianname;
    }

    public void setGuardianname(String guardianname) {
        this.guardianname = guardianname;
    }

    public String getGuardianphone() {
        return guardianphone;
    }

    public void setGuardianphone(String guardianphone) {
        this.guardianphone = guardianphone;
    }
}
