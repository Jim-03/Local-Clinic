import styles from "./PatientForm.module.css";
import * as React from "react";
import {type FormEvent, type JSX, useEffect, useState} from "react";
import type {Patient} from "../../../Interfaces.tsx";
import {RxUpdate} from "react-icons/rx";
import {FaUserPlus} from "react-icons/fa";
import {TbCancel} from "react-icons/tb";
import {toast} from "react-hot-toast";
import {validate} from "email-validator";
import {isValidPhoneNumber} from "libphonenumber-js";

type props = {
    patient?: Patient;
    closeFunction: () => void;
    onSave: () => void;
}

/**
 * A reusable form component that renders the patient's form
 * It allows adding a new patient into the system or update an existing patient's data
 * @param patient An optional patient's data
 * @param closeFunction A function triggered when the form is to be closed
 * @param onSave A function to be triggered once the patient's data is successfully added/updated
 */
function PatientForm({patient, closeFunction, onSave}: props): JSX.Element {

    // Hook to close the form in case the 'Esc' is pressed
    useEffect(() => {
        const closeWhenEscIsPressed = (e: KeyboardEvent) => {
            if (e.code === "Escape") closeFunction();
        };
        window.addEventListener('keydown', closeWhenEscIsPressed);

        return () => window.removeEventListener('keydown', closeWhenEscIsPressed);
    }, [closeFunction]);

    const [patientData, setPatientData] = useState(patient ? patient : {
        id: 0,
        fullName: "",
        email: "",
        phone: "",
        nationalId: "",
        address: "",
        dateOfBirth: "2000-01-01",
        gender: "MALE",
        emergencyContact: "",
        emergencyName: "",
        insuranceProvider: "",
        insuranceNumber: "",
        bloodType: "O-"
    });
    const [isSubmitting, setIsSubmitting] = useState(false)
    const api: string = import.meta.env.VITE_API_URL

    /**
     * Sends a POST request to add a new patient to the system
     */
    function addPatient() {
        setIsSubmitting(true)
        fetch(api + "/api/patient", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            credentials: "include",
            body: JSON.stringify(patientData)
        }).then(async response => {
            const data = await response.json()

            // Check if patent was added successfully
            if (!response.ok) {
                toast.error("Failed to add the patient!")
                throw new Error(data.message)
            }
            return data
        }).then(() => {
            toast.success("Successfully added the patient")
            onSave()
        }).catch(e => {
            console.warn(e)
        }).finally(() => setIsSubmitting(false))
    }

    /**
     * Function that triggers adding/updating a patient
     * If patient data was passed to the component, it results to updating else adding a patient
     * @param e
     */
    function submitData(e: FormEvent<HTMLFormElement>): void {
        e.preventDefault()

        if (!validatePatientData()) return
        if (patient) updatePatient()
        else addPatient()
    }

    /**
     * TODO: Test
     * Sends a PUT request to update a patient's data in the system
     */
    function updatePatient(): void {
        setIsSubmitting(true)
        fetch(api + `/api/patient/${patient?.id}`, {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            credentials: "include",
            body: JSON.stringify(patientData)
        }).then(async response => {
            const data = await response.json()

            // Check for any errors while updating
            if (!response.ok) {
                toast.error("Failed to update the specified patient")
                throw new Error(data.message)
            }
            return data
        }).then(() => {
            toast.success("Successfully updated the patient")
            onSave()
        }).catch(e => {
            console.warn("An error occurred while updating the patient", e.message)
        }).finally(() => setIsSubmitting(false))
    }

    /**
     * Checks if the forms inputs' values are correct
     * @returns {boolean} true if valid, false otherwise
     */
    function validatePatientData(): boolean {
        if (!patientData.fullName.trim()) {
            toast.error("Provide the patient's full name!");
            return false;
        }

        if (patientData.email && !validate(patientData.email)) {
            toast.error("Enter a valid email address!");
            return false;
        }

        if (!patientData.phone.trim()) {
            toast.error("Enter the patient's phone number");
            return false;
        } else {
            try {
                const phoneValid = isValidPhoneNumber(patientData.phone, 'KE');
                if (!phoneValid) {
                    toast.error("Enter a valid phone number");
                    return false;
                }
            } catch (e) {
                console.warn(e);
                toast.error("Enter a valid phone number");
                return false;
            }
        }

        if (!patientData.dateOfBirth) {
            toast.error("Please provide date of birth");
            return false;
        }

        if (patientData.emergencyContact && !isValidPhoneNumber(patientData.emergencyContact, 'KE')) {
            toast.error("Please enter a valid emergency contact number");
            return false;
        }

        return true;
    }

    /**
     * Changes the values of the patient data when an input value is changed
     */
    function changePatientDetails(e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>): void {
        const {name, value} = e.target;

        if (name === "dateOfBirth") {
            const dob: string = new Date(value).toISOString().split("T")[0];
            setPatientData({...patientData, [name]: dob});
        } else {
            setPatientData({...patientData, [name]: value});
        }
    }

    return (
        <div className={styles.background} onClick={closeFunction}>
            <form className={styles.form} onClick={e => e.stopPropagation()} onSubmit={submitData}>
                <div className={styles.closeDiv}>
                    <p onClick={closeFunction}>&times;</p>
                </div>
                <h1>{patient ? patient.fullName : "Add New Patient"}</h1>

                <div className={styles.patientDetails}>
                    <h2>Personal Details</h2>
                    <div className={styles.formInputGroup}>
                        {patient && (
                            <div className={styles.inputGroup}>
                                <label>Patient ID:</label>
                                <input type={"text"} disabled={true} value={patientData?.id} readOnly={true}/>
                            </div>
                        )}
                        <div className={styles.inputGroup}>
                            <label>Full Name:</label>
                            <input
                                type={"text"}
                                name={"fullName"}
                                value={patientData.fullName}
                                placeholder={"John Doe"}
                                onChange={changePatientDetails}
                                required
                            />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Email Address:</label>
                            <input
                                type={"email"}
                                name={"email"}
                                value={patientData.email}
                                onChange={changePatientDetails}
                                placeholder={"johnDoe@example.com"}
                            />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Phone Number:</label>
                            <input
                                type={"tel"}
                                name={"phone"}
                                value={patientData.phone}
                                onChange={changePatientDetails}
                                placeholder={"0712345678"}
                                required
                            />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Date of Birth:</label>
                            <input
                                type={"date"}
                                name={"dateOfBirth"}
                                value={patientData.dateOfBirth}
                                onChange={changePatientDetails}
                                required
                            />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Gender:</label>
                            <select name="gender" value={patientData.gender} onChange={changePatientDetails}>
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                            </select>
                        </div>
                        <div className={styles.inputGroup}>
                            <label>National ID number:</label>
                            <input
                                type={"text"}
                                name={"nationalId"}
                                value={patientData.nationalId}
                                placeholder={"12314546"}
                                onChange={changePatientDetails}
                            />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Home Address:</label>
                            <input
                                type={"text"}
                                name={"address"}
                                value={patientData.address}
                                placeholder={"Town X, street Y"}
                                onChange={changePatientDetails}
                            />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Blood Type:</label>
                            <select name="bloodType" value={patientData.bloodType} onChange={changePatientDetails}>
                                <option value="O-">O-</option>
                                <option value="O+">O+</option>
                                <option value="A-">A-</option>
                                <option value="A+">A+</option>
                                <option value="B-">B-</option>
                                <option value="B+">B+</option>
                                <option value="AB-">AB-</option>
                                <option value="AB+">AB+</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div className={styles.emergencyDetails}>
                    <h2>Emergency Contact Details</h2>
                    <div className={styles.formInputGroup}>
                        <div className={styles.inputGroup}>
                            <label>Full Name:</label>
                            <input
                                type={"text"}
                                name={"emergencyName"}
                                value={patientData.emergencyName}
                                placeholder={"Jane Doe"}
                                onChange={changePatientDetails}
                            />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Phone Number:</label>
                            <input
                                type={"tel"}
                                name={"emergencyContact"}
                                value={patientData.emergencyContact}
                                placeholder={"0712346578"}
                                onChange={changePatientDetails}
                            />
                        </div>
                    </div>
                </div>

                <div className={styles.insuranceDetails}>
                    <h2>Insurance Details</h2>
                    <div className={styles.formInputGroup}>
                        <div className={styles.inputGroup}>
                            <label>Insurance Provider:</label>
                            <input
                                type={"text"}
                                name={"insuranceProvider"}
                                value={patientData.insuranceProvider}
                                placeholder={"InsureCare"}
                                onChange={changePatientDetails}
                            />
                        </div>
                        <div className={styles.inputGroup}>
                            <label>Insurance Number:</label>
                            <input
                                type={"text"}
                                name={"insuranceNumber"}
                                value={patientData.insuranceNumber}
                                placeholder={"PHI789"}
                                onChange={changePatientDetails}
                            />
                        </div>
                    </div>
                </div>

                <div className={styles.buttons}>
                    <button type={"submit"} className={patient ? styles.updateButton : styles.addButton}
                            disabled={isSubmitting}>
                        {patient ? <RxUpdate/> : <FaUserPlus/>}
                        {patient ? "Update" : "Add"}
                    </button>
                    <button type={"button"} className={styles.cancelButton} onClick={closeFunction}
                            disabled={isSubmitting}>
                        <TbCancel/> Cancel
                    </button>
                </div>
            </form>
        </div>
    );
}

export default PatientForm;