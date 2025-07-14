package com.softcafe.clinic_system.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Staff staff;

    @Column
    private String action;

    @Column
    private LocalDateTime time;
}