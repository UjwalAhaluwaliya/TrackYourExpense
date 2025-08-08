package com.ujwal.TrackYourExpense.Model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="UserForm")
public class userRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserId")
    private Integer id;
    @Column(name ="FullName")
    private String name;
    @Column(name="EmailId")
    private String emailId;
    @Column(name="Password")
    private String password;
    @Column(name="ConfirmPassword")
    private String confirmPassword;

}

