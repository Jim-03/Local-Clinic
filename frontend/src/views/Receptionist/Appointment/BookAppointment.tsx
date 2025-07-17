import styles from "./BookAppointment.module.css";
import {FaCalendarAlt, FaTimes} from "react-icons/fa";
import * as React from "react";
import {type JSX, useEffect, useState} from "react";
import toast from "react-hot-toast";

interface BookAppointmentFormProps {
    patient: {
        id: number;
        fullName: string;
    };
    onClose: () => void;
}

interface Doctor {
    id: number;
    fullName: string;
}

/**
 * Renders a form to book appointments
 * @param patient Patient's details
 * @param onClose Function triggered to unmount the form
 */
function BookAppointmentForm({patient, onClose}: BookAppointmentFormProps): JSX.Element {
    const [doctors, setDoctors] = useState<Doctor[]>([]);
    const [selectedDoctorId, setSelectedDoctorId] = useState<number | null>(null);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const api: string = import.meta.env.VITE_API_URL;

    // Fetch active doctors
    useEffect(() => {
        fetch(api + '/api/staff/doctors/active')
            .then((res) => res.json())
            .then((data) => setDoctors(data))
            .catch((err) => console.error("Failed to fetch doctors:", err));
    }, []);

    // Adds a new appointment to the system
    function bookAppointment(e: React.FormEvent): void {
        e.preventDefault();

        if (!selectedDoctorId) {
            toast.error("Please select a doctor");
            return;
        }

        setIsSubmitting(true);

        const appointmentData = {
            patientId: patient.id,
            doctorId: selectedDoctorId
        };

        fetch(api + "/api/appointment", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            credentials: "include",
            body: JSON.stringify(appointmentData)
        }).then(async response => {
            const data = await response.json();
            if (!response.ok) {
                throw new Error(data.message || "Failed to book appointment");
            }
            return data;
        }).then(() => {
            toast.success("Appointment booked successfully!");
            onClose();
        }).catch(error => {
            toast.error(error.message || "Failed to book appointment");
            console.error(error);
        }).finally(() => {
            setIsSubmitting(false);
        });
    }

    return (
        <div className={styles.formContainer}>
            <div className={styles.overlay} onClick={onClose}></div>
            <form className={styles.form} onSubmit={bookAppointment}>
                <div className={styles.header}>
                    <h2>
                        <FaCalendarAlt/> Book Appointment
                    </h2>
                    <button
                        type="button"
                        onClick={onClose}
                        className={styles.closeButton}
                        disabled={isSubmitting}
                    >
                        <FaTimes/>
                    </button>
                </div>

                <div className={styles.patientInfo}>
                    <h3>Patient: {patient.fullName}</h3>
                    <p>ID: {patient.id}</p>
                </div>

                <div className={styles.formGroup}>
                    <label>Select Doctor *</label>
                    <select
                        value={selectedDoctorId || ""}
                        onChange={(e) => setSelectedDoctorId(Number(e.target.value))}
                        required
                        disabled={isSubmitting}
                    >
                        <option value="">-- Select Doctor --</option>
                        {doctors.map((doctor) => (
                            <option key={doctor.id} value={doctor.id}>
                                Dr. {doctor.fullName}
                            </option>
                        ))}
                    </select>
                </div>

                <div className={styles.formActions}>
                    <button
                        type="button"
                        onClick={onClose}
                        className={styles.cancelButton}
                        disabled={isSubmitting}
                    >
                        Cancel
                    </button>
                    <button
                        type="submit"
                        className={styles.submitButton}
                        disabled={isSubmitting || !selectedDoctorId}
                    >
                        {isSubmitting ? "Booking..." : "Book Appointment"}
                    </button>
                </div>
            </form>
        </div>
    );
}

export default BookAppointmentForm;