import { type JSX, useEffect, useState } from "react";
import styles from "./Patient.module.css";
import { FaAngleLeft, FaAngleRight, FaEdit, FaSearch, FaUserPlus } from "react-icons/fa";
import type { Patient } from "../../../Interfaces.tsx";
import Loading from "../../../components/animations/Loading.tsx";
import PatientForm from "../../../components/forms/patient/PatientForm.tsx";
import { FcCalendar } from "react-icons/fc";
import { toast } from "react-hot-toast";
import { validate } from "email-validator";
import { isValidPhoneNumber } from "libphonenumber-js";
import AppointmentForm from '../../../components/forms/appointmentForm/AppointmentForm.tsx';

/**
 * A component that render a view of the patients in the system
 * It allows viewing of patients' details by page
 * Contains a button for adding a new patient
 * Each patient row has two action buttons, one for editing the patient's details
 * and one for booking an appointment for that patient
 */
function Patients(): JSX.Element {
    const [isLoading, setIsLoading] = useState(false);
    const [patientsList, setPatientsList] = useState<Patient[]>([]);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [searchTerm, setSearchTerm] = useState("");
    const [searchParameter, setSearchParameter] = useState<"email" | "phone" | "nid">("email");
    const [showAddForm, setShowAddForm] = useState(false);
  const [ showBookingForm, setShowBookingForm ] = useState(false);
    const [patientData, setPatientData] = useState<Patient>();
    const [editPatient, setEditPatient] = useState(false);
    const api: string = import.meta.env.VITE_API_URL;

    useEffect(() => {
        if (!searchTerm) {
            fetchPatients();
        }
    }, [searchTerm]);

    /**
     * Searches for a patient based on the unique identifier specified
     */
    function searchPatient() {
        if (!searchTerm) return;

        switch (searchParameter) {
            case "email":
                if (!validate(searchTerm)) {
                    toast.error("Enter a valid email address");
                    return;
                }
                break;
            case "phone":
                try {
                    const phone = isValidPhoneNumber(searchTerm, "KE");

                    if (!phone) {
                        toast.error("Enter a valid phone number!");
                        return;
                    }
                } catch {
                    toast.error("Enter a valid phone number");
                    return;
                }
                break;
        }
        const params = new URLSearchParams();
        params.append(searchParameter, searchTerm);

        setIsLoading(true);
        fetch(`${api}/api/patient?${params.toString()}`)
          .then(async (response) => {
              if (!response.ok) {
                  const text = await response.text();

                  if (response.status === 404) {
                      toast.error("Patient not found!");
                      throw new Error(text);
                  }

                  throw new Error(text);
              }
              return await response.json();
          })
          .then((data) => {
              setPatientsList([data]);
              setTotalPages(1);
          })
          .catch((err) => {
              setPatientsList([]);
              console.error(err);
          }).finally(() => {
            setIsLoading(false);
        });
    }

    /**
     * Fetches a list of 10 patients sorted on recently added first
     */
    function fetchPatients() {
        setIsLoading(true);
        fetch(`${api}/api/patient/page/${page}`)
          .then((res) => res.json())
          .then((data) => {
              setPatientsList(data.patients);
              setTotalPages(data.totalPages);
          })
          .catch((e) => {
              toast.error("Failed to fetch patients list!");
              console.warn(e.message);
              setPatientsList([]);
          }).finally(() => setIsLoading(false));
    }

    return <div className={styles.background}>
        {showAddForm && <PatientForm closeFunction={() => setShowAddForm(false)} onSave={() => {
            fetchPatients();
        }}/>}
        {editPatient &&
          <PatientForm patient={patientData} closeFunction={() => setEditPatient(false)} onSave={fetchPatients}/>}
      {showBookingForm && <AppointmentForm patient={patientData} closeFunction={() => {setPatientData(undefined); setShowBookingForm(false)}}/>}
        <div className={styles.header}>
            <div className={styles.searchGroup}>
                <select value={searchParameter}
                        onChange={e => setSearchParameter(e.target.value as "email" | "phone" | "nid")}>
                    <option value={"email"}>Email</option>
                    <option value={"nid"}>ID</option>
                    <option value={"phone"}>Phone</option>
                </select>
                <input type={"search"} onChange={e => {
                    setSearchTerm(e.target.value.trim());
                }} placeholder={"Search for patient"}/>
                <button onClick={searchPatient} disabled={!searchTerm} className={styles.searchButton}><FaSearch/>
                </button>
            </div>
            <button onClick={() => setShowAddForm(true)} className={styles.addPatientButton}><FaUserPlus/> Add new
                patient
            </button>
        </div>
        {isLoading ? <Loading/> : <>
            <div className={styles.contentArea}>
                {patientsList.length === 0 ?
                  <p>There are no patients in the system!</p>
                  :
                  <div className={styles.tableArea}>
                      <table className={styles.patientsTable}>
                          <thead>
                          <tr>
                              <th>First Name</th>
                              <th>Last Name</th>
                              <th>Email Address</th>
                              <th>Phone Number</th>
                              <th>Id Number</th>
                              <th>Actions</th>
                          </tr>
                          </thead>
                          <tbody>
                          {patientsList.map(patient => <tr key={patient.id}>
                              <td>{patient.fullName.split(' ')[0]}</td>
                              <td>{patient.fullName.split(' ') [1]}</td>
                              <td>{patient.email}</td>
                              <td>{patient.phone}</td>
                              <td>{patient.nationalId}</td>
                              <td>
                                  <div className={styles.actions}>
                                      <FaEdit title={"Edit Patient"} color={"purple"} onClick={() => {
                                          setPatientData(patient);
                                          setEditPatient(true);
                                      }}/>
                                      <FcCalendar title={"Book An appointment"}
                                                  onClick={() => {
                                                    setPatientData(patient)
                                                    setShowBookingForm(true)
                                                    }}/>
                                  </div>
                              </td>
                          </tr>)}
                          </tbody>
                      </table>
                      <div className={styles.pageControls}>
                          <button disabled={page === 1} onClick={() => setPage(page - 1)}><FaAngleLeft/></button>
                          <p>Page {page} of {totalPages}</p>
                          <button disabled={page === totalPages} onClick={() => {
                              setPage(page + 1);
                          }}><FaAngleRight/></button>
                      </div>
                  </div>
                }
            </div>
        </>}
    </div>;
}

export default Patients;