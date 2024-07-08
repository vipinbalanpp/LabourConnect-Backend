package com.example.user.repository;

import com.example.user.model.entity.Roles;
import com.example.user.model.entity.User;
import feign.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Page<User> findByRole(Roles role, Pageable pageable);
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isBlocked = true AND LOWER(u.fullName) LIKE LOWER(CONCAT(:searchInput, '%'))")
    Page<User> findByRoleAndIsBlockedBySearch(@Param("role")Roles role,   @Param("searchInput")String searchInput,Pageable pageable);
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isBlocked = false AND LOWER(u.fullName) LIKE LOWER(CONCAT(:searchInput, '%'))")
    Page<User> findByRoleAndNotBlockedBySearch(@Param("role") Roles role, @Param("searchInput") String searchInput, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isBlocked = false")
    Page<User> findByRoleAndNotBlocked(@Param("role")Roles role, Pageable pageable);
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.isBlocked = true")
    Page<User> findByRoleAndIsBlocked(@Param("role")Roles role, Pageable pageable);
    @Query("SELECT u FROM User u WHERE u.role = :role AND LOWER(u.fullName) LIKE CONCAT(:searchInput, '%')")
    Page<User> findByRoleAndSearchInput(@Param("role") Roles role,@Param("searchInput") String searchInput, Pageable pageable);
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.isBlocked = true AND LOWER(u.fullName) LIKE LOWER(CONCAT(:searchInput, '%'))")
    long countByRoleAndIsBlockedBySearch(@Param("role") Roles role, @Param("searchInput") String searchInput);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.isBlocked = false AND LOWER(u.fullName) LIKE LOWER(CONCAT(:searchInput, '%'))")
    long countByRoleAndNotBlockedBySearch(@Param("role") Roles role, @Param("searchInput") String searchInput);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.isBlocked = true")
    long countByRoleAndIsBlocked(@Param("role") Roles role);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.isBlocked = false")
    long countByRoleAndNotBlocked(@Param("role") Roles role);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND LOWER(u.fullName) LIKE LOWER(CONCAT(:searchInput, '%'))")
    long countByRoleAndSearchInput(@Param("role") Roles role, @Param("searchInput") String searchInput);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countByRole(@Param("role") Roles role);
}