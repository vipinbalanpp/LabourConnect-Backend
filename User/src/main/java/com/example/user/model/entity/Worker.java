package com.example.user.model.entity;


import com.example.user.model.dto.request.WorkerRequestDto;
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
    public Worker(WorkerRequestDto workerRequestDto) {
        this.fullName = workerRequestDto.getFullname();
        this.email = workerRequestDto.getEmail();
        this.experience = workerRequestDto.getExperience();
        this.mobileNumber = workerRequestDto.getMobileNumber();
        this.about = workerRequestDto.getAbout();
        this.serviceCharge = workerRequestDto.getServiceCharge();
        this.gender = workerRequestDto.getGender();
        this.dateOfBirth = workerRequestDto.getDateOfBirth();
        this.role = Roles.WORKER;
        this.profileImageUrl = workerRequestDto.getProfileImageUrl();
        this.isBlocked = false;
        this.isVerified = false;
        this.createdAt = LocalDateTime.now();
        this.works = null;
    }
}
