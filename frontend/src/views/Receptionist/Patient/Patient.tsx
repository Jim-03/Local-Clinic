import styles from "./Patient.module.css";
import {FaCalendarPlus, FaEdit, FaPlus} from "react-icons/fa";
import {type JSX, useEffect, useState} from "react";
import PatientForm from "./PatientForm.tsx";
import BookAppointmentForm from "../Appointment/BookAppointment.tsx";

interface Patient {
    id: number;
    fullName: string;
    email: string;
    phone: string;
    nationalId: string;
    gender: string;
    dateOfBirth: string;
    insuranceProvider: string;
}

/**
 * A patient view component
 * Allows adding a new patient to the system
 * Allows a patient's data to be edited
 * An appointment with a doctor can also be made here
 */
function Patients(): JSX.Element {
    const [patients, setPatients] = useState<Patient[]>([]);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [searchTerm, setSearchTerm] = useState(""); // TODO: Implement searching
    const [editingPatient, setEditingPatient] = useState<Patient | null>(null);
    const [showAddForm, setShowAddForm] = useState(false);
    const [bookingPatient, setBookingPatient] = useState<Patient | null>(null);

    const api = import.meta.env.VITE_API_URL;

    useEffect(() => {
        fetchPatients()
    }, [page]);

    function fetchPatients(): void {
        fetch(`${api}/api/patient/page/${page}`)
            .then((res) => res.json())
            .then((data) => {
                setPatients(data.patients);
                setTotalPages(data.totalPages)
            })
            .catch(() => {
                setPatients([]);
            });
    }

    return (
        <>
            {(editingPatient || showAddForm) && (
                <PatientForm
                    patient={editingPatient || undefined}
                    onSave={() => {
                        fetchPatients()
                        setEditingPatient(null);
                        setShowAddForm(false);
                    }}
                    onCancel={() => {
                        setEditingPatient(null);
                        setShowAddForm(false);
                    }}
                />
            )}
            {bookingPatient && (
                <BookAppointmentForm
                    patient={bookingPatient}
                    onClose={() => setBookingPatient(null)}
                />
            )}
            <div className={styles.patientsView}>
                <h2>Patients</h2>

                <section className={styles.controls}>
                    <input
                        type="search"
                        placeholder="Search by email, phone, insurance number or national id number"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                    <div className={styles.controlButtons}>
                        <button onClick={() => setShowAddForm(true)}><FaPlus/> Add New Patient</button>
                    </div>
                </section>

                <table className={styles.patientTable}>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Gender</th>
                        <th>Insurance</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    {patients.length === 0 ? (
                        <tr>
                            <td colSpan={7}>No patients found.</td>
                        </tr>
                    ) : (
                        patients.map((p) => (
                            <tr key={p.id}>
                                <td>{p.id}</td>
                                <td>{p.fullName}</td>
                                <td>{p.email}</td>
                                <td>{p.phone}</td>
                                <td>{p.gender}</td>
                                <td>{p.insuranceProvider || "None"}</td>
                                <td className={styles.actions}>
                                    <button title="Edit" onClick={() => setEditingPatient(p)}><FaEdit/></button>
                                    <button title="Book" onClick={() => setBookingPatient(p)}><FaCalendarPlus/></button>
                                </td>
                            </tr>
                        ))
                    )}
                    </tbody>
                </table>

                <div className={styles.pagination}>
                    <button disabled={page === 1} onClick={() => setPage((p) => p - 1)}>‹</button>
                    <span>Page {page} of {totalPages}</span>
                    <button disabled={page === totalPages} onClick={() => setPage((p) => p + 1)}>›</button>
                </div>
            </div>
        </>
    );
}

export default Patients;
