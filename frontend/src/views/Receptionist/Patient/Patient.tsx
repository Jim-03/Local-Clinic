import styles from "./Patient.module.css";
import {FaCalendarPlus, FaEdit, FaPlus, FaSearch} from "react-icons/fa";
import {type JSX, useEffect, useState} from "react";
import PatientForm from "./PatientForm.tsx";
import BookAppointmentForm from "../Appointment/BookAppointment.tsx";
import { isValidNumber } from 'libphonenumber-js';
import * as EmailValidator from 'email-validator';

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

function Patients(): JSX.Element {
    const [patients, setPatients] = useState<Patient[]>([]);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [searchTerm, setSearchTerm] = useState("");
    const [searchType, setSearchType] = useState<"email" | "phone" | "nid" | "inn">("email");
    const [editingPatient, setEditingPatient] = useState<Patient | null>(null);
    const [showAddForm, setShowAddForm] = useState(false);
    const [bookingPatient, setBookingPatient] = useState<Patient | null>(null);
    const [searchError, setSearchError] = useState<string | null>(null);

    const api = import.meta.env.VITE_API_URL;

    useEffect(() => {
        fetchPatients();
    }, [page]);

    // Validate search input based on type
    useEffect(() => {
        if (!searchTerm.trim()) {
            setSearchError(null);
            return;
        }

        switch (searchType) {
            case "email":
                if (!EmailValidator.validate(searchTerm)) {
                    setSearchError("Please enter a valid email address");
                } else {
                    setSearchError(null);
                }
                break;
            case "phone":
                if (!isValidNumber(searchTerm, "KE")) {
                    setSearchError("Please enter a valid phone number");
                } else {
                    setSearchError(null);
                }
                break;
            case "nid":
                if (!/^\d+$/.test(searchTerm)) {
                    setSearchError("National ID should contain only numbers");
                } else {
                    setSearchError(null);
                }
                break;
            case "inn":
                if (searchTerm.length < 3) {
                    setSearchError("Insurance number too short");
                } else {
                    setSearchError(null);
                }
                break;
        }
    }, [searchTerm, searchType]);

    // Fetch existing patients in the system
    function fetchPatients(): void {
        fetch(`${api}/api/patient/page/${page}`)
            .then((res) => res.json())
            .then((data) => {
                setPatients(data.patients);
                setTotalPages(data.totalPages);
            })
            .catch(() => {
                setPatients([]);
            });
    }

    // Search for patient(s) satisfying the search criteria
    function searchPatients(): void {
        if (!searchTerm.trim()) {
            fetchPatients();
            return;
        }

        if (searchError) {
            return;
        }

        const params = new URLSearchParams();
        params.append(searchType, searchTerm);

        fetch(`${api}/api/patient?${params.toString()}`)
            .then(async (res) => {
                if (!res.ok) {
                    throw new Error(await res.text());
                }
                return res.json();
            })
            .then((data) => {
                setPatients([data]);
                setTotalPages(1);
            })
            .catch((err) => {
                setPatients([]);
                setSearchError("Patient not found");
                console.error(err);
            });
    }

    // Triggers the searching process
    function search(): void {
        if (searchError) return;
        setPage(1);
        searchPatients();
    }

    // Resets the search container to its defaults
    function reset(): void {
        setSearchTerm("");
        setSearchType("email");
        setPage(1);
        setSearchError(null);
        fetchPatients();
    }

    return (
        <>
            {(editingPatient || showAddForm) && (
                <PatientForm
                    patient={editingPatient || undefined}
                    onSave={() => {
                        fetchPatients();
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
                    <div className={styles.searchContainer}>
                        <select
                            value={searchType}
                            onChange={(e) => {
                                setSearchType(e.target.value as "email" | "phone" | "nid" | "inn");
                                setSearchTerm("");
                                setSearchError(null);
                            }}
                            className={styles.searchSelect}
                        >
                            <option value="email">Email</option>
                            <option value="phone">Phone</option>
                            <option value="nid">National ID</option>
                            <option value="inn">Insurance Number</option>
                        </select>
                        <input
                            type="search"
                            placeholder={`Search by ${searchType}...`}
                            value={searchTerm}
                            onChange={(e) => setSearchTerm(e.target.value)}
                            className={styles.searchInput}
                        />
                        <button
                            onClick={search}
                            className={styles.searchButton}
                            disabled={!!searchError || !searchTerm.trim()}
                        >
                            <FaSearch /> Search
                        </button>
                        {(searchTerm || searchError) && (
                            <>
                                <button
                                    onClick={reset}
                                    className={styles.resetButton}
                                >
                                    Reset
                                </button>
                                {searchError && (
                                    <span className={styles.errorMessage}>{searchError}</span>
                                )}
                            </>
                        )}
                    </div>
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
                            <td colSpan={7}>
                                {searchTerm ? "No matching patients found" : "No patients found"}
                            </td>
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