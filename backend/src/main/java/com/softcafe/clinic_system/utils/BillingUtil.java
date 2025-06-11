package com.softcafe.clinic_system.utils;

import com.softcafe.clinic_system.dto.billing.BillingData;
import com.softcafe.clinic_system.dto.billing.NewBill;
import com.softcafe.clinic_system.entities.Billing;
import com.softcafe.clinic_system.entities.Patient;

import java.util.Map;

public class BillingUtil {

    /**
     * Converts a billing object to a DTO
     *
     * @param billing Billing data object
     * @return Billing DTO
     */
    public static BillingData toDto(Billing billing) {
        return new BillingData(
                billing.getId(),
                PatientUtil.toDto(billing.getPatient()),
                billing.getBills(),
                billing.getTotalAmount(),
                billing.getPaymentMethod(),
                billing.getAmountPaid(),
                billing.getStatus(),
                billing.getCreatedAt(),
                billing.getUpdatedAt()
        );
    }

    /**
     * Converts a DTO to the billing object
     *
     * @param newBill Billing DTO
     * @param patient Patient's details
     * @return Billing object
     */
    public static Billing toObject(NewBill newBill, Patient patient) {
        Billing billing = new Billing();
        billing.setAmountPaid(newBill.amountPaid());
        billing.setBills(newBill.bills());
        billing.setPatient(patient);
        billing.setPaymentMethod(newBill.paymentMethod());
        billing.setStatus(newBill.status());
        billing.setTotalAmount(calculateTotal(newBill.bills()));
        return billing;
    }

    /**
     * Calculates the total amount from the bills map object
     *
     * @param bills A map of bills and their amount
     * @return Total amount
     */
    private static Double calculateTotal(Map<String, Double> bills) {
        Double total = 0.00;
        for (String bill : bills.keySet()) {
            total += bills.get(bill);
        }
        return total;
    }

    /**
     * Updates the details of a billing object from a DTO
     *
     * @param oldBill     Billing object
     * @param updatedBill Billing DTO
     * @param patient     Patient's object
     */
    public static void update(Billing oldBill, NewBill updatedBill, Patient patient) {
        oldBill.setTotalAmount(calculateTotal(updatedBill.bills()));
        oldBill.setStatus(updatedBill.status());
        oldBill.setPaymentMethod(updatedBill.paymentMethod());
        oldBill.setPatient(patient);
        oldBill.setBills(updatedBill.bills());
        oldBill.setAmountPaid(updatedBill.amountPaid());
    }
}
