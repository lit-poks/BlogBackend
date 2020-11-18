package org.scribbler.service.entity;


import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * UserEntity class contains all the attributes to be mapped to all the fields in USERS table in the database.
 * All the annotations which are used to specify all the constraints to the columns in the database must be correctly implemented.
 */

@Entity
@Table(name="users")
@NamedQueries({
        @NamedQuery(name="userByUsername", query="select u from UserEntity u where u.userName = :username"),
        @NamedQuery(name="userByEmail", query="select u from UserEntity u where u.email = :email"),
        @NamedQuery(name="userByUuid", query= "select u from UserEntity u where u.uuid= : uuid")
})

public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 64)
    @NotNull
    private String uuid;


    @Column(name = "FIRST_NAME")
    @NotNull
    private String firstName;

    @Column(name = "LAST_NAME")
    @NotNull
    private String lastName;

    @Column(name = "USERNAME", unique = true)
    @NotNull
    private String userName;

    @Column(name = "EMAIL", unique = true)
    @NotNull
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    @ToStringExclude
    private String password;

    @Column(name = "SALT")
    @NotNull
    @Size(max = 200)
    private String salt;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "CONTACT_NUMBER")
    private String contactNumber;

    public Integer getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getCountry() {
        return country;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
