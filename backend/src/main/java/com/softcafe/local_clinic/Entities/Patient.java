package com.softcafe.local_clinic.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patients")
public class Patient extends User {

    @Column(name = "blood_type")
    private String bloodType;

    @Column(name = "allergies")
    private String allergies;

    @Column(name = "insurance_provider")
    private String insuranceProvider;

    @Column(name = "insurance_number")
    private String insuranceNumber;

    @Column(name = "next_of_kin_name")
    private String kinName;

    @Column(name = "kin_contact", length = 20)
    private String kinContact;

}
