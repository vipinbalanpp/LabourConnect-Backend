package com.example.user.model.entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Worker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    @ManyToOne
    @JoinColumn(name = "service_id", referencedColumnName = "serviceId")
    private Services service;
    private Long experience;
    private String mobileNumber;
    private double serviceCharge;
    @Column(columnDefinition = "TEXT")
    private String about;
    private String gender;
    private LocalDate dateOfBirth;
    private Roles role;
    private String profileImageUrl;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "addressId")
    private Address address;
    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Works> works;
    private boolean isBlocked;
    private boolean isVerified;
    private LocalDateTime createdAt;
}
