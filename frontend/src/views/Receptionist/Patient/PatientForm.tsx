import styles from "./PatientForm.module.css";
import {FaSave, FaTimes, FaUserPlus} from "react-icons/fa";
import * as React from "react";
import {type FormEventHandler, useEffect, useState} from "react";
import toast from "react-hot-toast";

interface PatientFormProps {
    patient?: {
        id?: number;
        fullName: string;
        email: string;
        phone: string;
        nationalId: string;
        gender: string;
        dateOfBirth: string;
        insuranceProvider: string;
        address?: string;
        emergencyContact?: string;
        emergencyName?: string;
        insuranceNumber?: string;
        bloodType?: string;
    };
    onSave: () => void;
    onCancel: () => void;
}

function PatientForm({patient, onSave, onCancel}: PatientFormProps) {
    const [formData, setFormData] = useState({
        fullName: "",
        email: "",
        phone: "",
        nationalId: "",
        gender: "Male",
        dateOfBirth: "",
        insuranceProvider: "",
        address: "",
        emergencyContact: "",
        emergencyName: "",
        insuranceNumber: "",
        bloodType: ""
    });
    const [isSubmitting, setIsSubmitting] = useState(false);

    // Initialize form data when patient prop changes
    useEffect(() => {
        if (patient) {
            setFormData({
                fullName: patient.fullName,
                email: patient.email,
                phone: patient.phone,
                nationalId: patient.nationalId,
                gender: patient.gender,
                dateOfBirth: patient.dateOfBirth,
                insuranceProvider: patient.insuranceProvider,
                address: patient.address || "",
                emergencyContact: patient.emergencyContact || "",
                emergencyName: patient.emergencyName || "",
                insuranceNumber: patient.insuranceNumber || "",
                bloodType: patient.bloodType || ""
            });
        } else {
            // Reset to empty form for new patient
            setFormData({
                fullName: "",
                email: "",
                phone: "",
                nationalId: "",
                gender: "Male",
                dateOfBirth: "",
                insuranceProvider: "",
                address: "",
                emergencyContact: "",
                emergencyName: "",
                insuranceNumber: "",
                bloodType: ""
            });
        }
    }, [patient]);

    const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const {name, value} = e.target;
        if (name === "dateOfBirth") {
            const dob = new Date(value).toISOString().split("T")[0];
            setFormData(prev => ({...prev, [name]: dob}));
        } else {
            setFormData(prev => ({...prev, [name]: value}));
        }
    };

    const api: string = import.meta.env.VITE_API_URL + "/api/patient"
    const handleSubmit: FormEventHandler<HTMLFormElement> = (e) => {
        e.preventDefault()
        if (patient) updatePatient()
        else addPatient()
    }

    /**
     * Adds a new patient to the system
     */
    function addPatient() {
        setIsSubmitting(true)
        fetch(api, {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            credentials: "include",
            body: JSON.stringify(formData)
        }).then(async response => {
            const data = await response.json()
            if (!response.ok) {
                toast.error("An error has occurred while adding the patient!")
                console.error(data)
            }
            return data
        }).then(() => {
            toast.success("Successfully added the patient")
            onSave()
        }).catch(e => {
            toast.error("Connection error!")
            console.warn(e)
        }).finally(() => setIsSubmitting(false))
    }

    /**
     * Updates an existing patient's details in the system
     */
    function updatePatient(): void {
        setIsSubmitting(true)
        fetch(api + `/${patient?.id}`, {
            method: "PUT",
            headers: {"Content-Type": "application/json"},
            credentials: "include",
            body: JSON.stringify(formData)
        }).then(async response => {
            const data = await response.json()
            if (!response.ok) {
                toast.error("An error has occurred while updating the patient!")
                console.error(data)
            }
            return await response.json()
        }).then(() => {
            toast.success("Successfully updated the patient")
            onSave()
        }).catch(e => {
            toast.error("Connection error!")
            console.warn(e)
        }).finally(() => setIsSubmitting(false))
    }

    return (
        <div className={styles.editFormContainer}>
            <div className={styles.editFormOverlay} onClick={onCancel}></div>
            <form className={styles.editForm} onSubmit={handleSubmit}>
                <h2>{patient ? "Edit Patient" : "Add New Patient"}</h2>
                <div className={styles.formGrid}>
                    <div className={styles.formSection}>
                        <h3>Personal Information</h3>
                        <div className={styles.formGroup}>
                            <label>Full Name *</label>
                            <input
                                type="text"
                                name="fullName"
                                value={formData.fullName}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className={styles.formGroup}>
                            <label>Email *</label>
                            <input
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className={styles.formGroup}>
                            <label>Phone *</label>
                            <input
                                type="tel"
                                name="phone"
                                value={formData.phone}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className={styles.formGroup}>
                            <label>National ID *</label>
                            <input
                                type="text"
                                name="nationalId"
                                value={formData.nationalId}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div className={styles.formGroup}>
                            <label>Date of Birth *</label>
                            <input
                                type="date"
                                name="dateOfBirth"
                                value={formData.dateOfBirth}
                                onChange={e => {
                                    handleChange(e)
                                }}
                                required
                            />
                        </div>
                        <div className={styles.formGroup}>
                            <label>Gender *</label>
                            <select
                                name="gender"
                                value={formData.gender}
                                onChange={handleChange}
                                required
                            >
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                            </select>
                        </div>
                    </div>

                    <div className={styles.formSection}>
                        <h3>Address & Emergency</h3>
                        <div className={styles.formGroup}>
                            <label>Address</label>
                            <input
                                type="text"
                                name="address"
                                value={formData.address}
                                onChange={handleChange}
                            />
                        </div>
                        <div className={styles.formGroup}>
                            <label>Emergency Contact Name</label>
                            <input
                                type="text"
                                name="emergencyName"
                                value={formData.emergencyName}
                                onChange={handleChange}
                            />
                        </div>
                        <div className={styles.formGroup}>
                            <label>Emergency Contact Phone</label>
                            <input
                                type="tel"
                                name="emergencyContact"
                                value={formData.emergencyContact}
                                onChange={handleChange}
                            />
                        </div>
                    </div>

                    <div className={styles.formSection}>
                        <h3>Medical Information</h3>
                        <div className={styles.formGroup}>
                            <label>Insurance Provider</label>
                            <input
                                type="text"
                                name="insuranceProvider"
                                value={formData.insuranceProvider}
                                onChange={handleChange}
                            />
                        </div>
                        <div className={styles.formGroup}>
                            <label>Insurance Number</label>
                            <input
                                type="text"
                                name="insuranceNumber"
                                value={formData.insuranceNumber}
                                onChange={handleChange}
                            />
                        </div>
                        <div className={styles.formGroup}>
                            <label>Blood Type</label>
                            <select
                                name="bloodType"
                                value={formData.bloodType}
                                onChange={handleChange}
                            >
                                <option value="">Select</option>
                                <option value="A+">A+</option>
                                <option value="A-">A-</option>
                                <option value="B+">B+</option>
                                <option value="B-">B-</option>
                                <option value="AB+">AB+</option>
                                <option value="AB-">AB-</option>
                                <option value="O+">O+</option>
                                <option value="O-">O-</option>
                            </select>
                        </div>
                    </div>
                </div>

                <div className={styles.formActions}>
                    <button type="button" className={styles.cancelButton} onClick={onCancel}>
                        <FaTimes/> Cancel
                    </button>
                    <button type="submit" className={styles.saveButton} disabled={isSubmitting}>
                        {patient ? <FaSave/> : <FaUserPlus/>}
                        {patient ? "Save Changes" : "Add Patient"}
                    </button>
                </div>
            </form>
        </div>
    );
}

export default PatientForm;