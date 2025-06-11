package com.softcafe.clinic_system.services;

import com.softcafe.clinic_system.dto.billing.BillList;
import com.softcafe.clinic_system.dto.billing.BillingData;
import com.softcafe.clinic_system.dto.billing.NewBill;
import com.softcafe.clinic_system.entities.Billing;
import com.softcafe.clinic_system.entities.Patient;
import com.softcafe.clinic_system.entities.PaymentStatus;
import com.softcafe.clinic_system.repositories.BillingRepository;
import com.softcafe.clinic_system.repositories.PatientRepository;
import com.softcafe.clinic_system.utils.BillingUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository billingRepository;
    private final PatientRepository patientRepository;
    private final int PAGE_SIZE = 10;

    /**
     * Adds a new billing to the system
     *
     * @param newBill The new billing data
     * @return Newly saved billing data
     */
    @Transactional
    public BillingData add(NewBill newBill) {
        // Fetch patient's details
        Patient patient = getPatient(newBill.patientId());

        BillingData billingData = BillingUtil.toDto(billingRepository.save(BillingUtil.toObject(newBill, patient)));

        billingRepository.flush();

        log.info("A new billing was created with ID:{}", billingData.id());

        return billingData;
    }

    /**
     * Retrieves bills made by a patient
     *
     * @param id   Patient's primary key
     * @param page Page number
     * @return An object containing the list of bills and total expected pages
     */
    public BillList getByPatient(Long id, int page) {
        // Fetch patient's details
        Patient patient = getPatient(id);

        // Fetch the page of bills
        Page<Billing> pageOfBills = billingRepository.findByPatient(
                patient, PageRequest.of(page - 1, PAGE_SIZE, Sort.by(
                        Sort.Direction.DESC, "created_At"
                ))
        );

        List<BillingData> listOfBills = new ArrayList<>();

        for (Billing billing : pageOfBills) {
            listOfBills.add(BillingUtil.toDto(billing));
        }

        return new BillList(pageOfBills.getTotalPages(), listOfBills);
    }

    /**
     * Retrieves bills created between two dates
     *
     * @param start Starting data
     * @param end   Ending date
     * @param page  Page number
     * @return An object containing the list of bills and total expected pages
     */
    public BillList getByDateRange(LocalDateTime start, LocalDateTime end, int page) {
        // Ensure the end date isn't before starting date
        if (end.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ensure the ending date is after starting date!");
        }

        // Fetch the page of Billings
        Page<Billing> billingPage = billingRepository.findByCreatedAtBetween(
                start, end, PageRequest.of(page - 1, PAGE_SIZE, Sort.by(
                        Sort.Direction.DESC, "created_at"
                ))
        );

        List<BillingData> billingList = new ArrayList<>();

        for (Billing billing : billingPage) {
            billingList.add(BillingUtil.toDto(billing));
        }

        return new BillList(billingPage.getTotalPages(), billingList);
    }

    /**
     * Retrieves billings with a similar status
     *
     * @param status Payment status
     * @param page   Page number
     * @return An object containing the list of bills and total expected pages
     */
    public BillList getByStatus(PaymentStatus status, int page) {
        Page<Billing> billingPage = billingRepository.findByStatus(status, PageRequest.of(page - 1, PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, "created_at")));

        List<BillingData> list = new ArrayList<>();

        for (Billing billing : billingPage) {
            list.add(BillingUtil.toDto(billing));
        }

        return new BillList(billingPage.getTotalPages(), list);
    }

    /**
     * Retrieves a list of billings paid in a similar method
     *
     * @param method Payment method
     * @param page   Page number
     * @return An object containing the list of billings and total expected pages
     */
    public BillList getByPaymentMethod(String method, int page) {
        if (method == null || method.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide the payment method!");
        }

        Page<Billing> billingPage = billingRepository.findByPaymentMethod(method, PageRequest.of(
                page - 1, PAGE_SIZE, Sort.by(Sort.Direction.DESC, "created_at")
        ));

        List<BillingData> list = new ArrayList<>();

        for (Billing billing : billingPage) {
            list.add(BillingUtil.toDto(billing));
        }

        return new BillList(billingPage.getTotalPages(), list);
    }

    /**
     * Updates an existing billings details
     *
     * @param id          Billing's primary key
     * @param updatedBill Newly updated data
     * @return Newly saved updated data
     */
    @Transactional
    public BillingData update(Long id, NewBill updatedBill) {
        // Fetch the billings details
        Billing oldBill = getBilling(id);

        // Fetch the patient's details
        Patient patient = getPatient(updatedBill.patientId());

        BillingUtil.update(oldBill, updatedBill, patient);

        BillingData billingData = BillingUtil.toDto(billingRepository.save(oldBill));
        billingRepository.flush();

        log.info("A billing with ID:{} was updated successfully", id);
        return billingData;
    }

    /**
     * Removes an existing billing from the system
     *
     * @param id Primary key
     */
    @Transactional
    public void delete(Long id) {
        // Fetch the billing's data
        Billing billing = getBilling(id);

        billingRepository.delete(billing);
        log.info("Billing with ID:{} successfully deleted", id);
    }

    /**
     * Retrieves patient's data
     *
     * @param id Patient's primary key
     * @return Patient's data
     * @throws ResponseStatusException NOT_FOUND In case patient's record doesn't exist
     */
    private Patient getPatient(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified patient doesn't exist!"));
    }

    /**
     * Retrieves a billing's details
     *
     * @param id Primary key
     * @return THe billing's data
     */
    private Billing getBilling(Long id) {
        return billingRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "The specified billing wasn't found!")
        );
    }
}
