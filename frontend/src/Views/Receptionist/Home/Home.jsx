import styles from "./Home.module.css"
import {useEffect, useState} from "react";
import toast from "react-hot-toast";
import {FaHandshake, FaUser} from "react-icons/fa";
import {RiNumbersFill} from "react-icons/ri";

function Home({firstName}) {

    useEffect(() => {
        // Fetch the list of incomplete appointments
        fetch("http://localhost:8080/api/appointments/incomplete").
        then(response => response.json()).
        then(data => {
            if (data.status === "SUCCESS") {
                setIncompleteAppointments(data.data)
            } else {
            setIncompleteAppointments([])
            toast.error(data.message)
            }
        })

        // Fetch total patients
        fetch("http://localhost:8080/api/patient/total").
        then(response => response.json()).
        then(data => {
            if (data.status === "SUCCESS") {
            setActivePatients(data.data)
            } else {
                setActivePatients(0)
                toast.error(data.message)
            }
        })
        // Fetch daily appointments
        fetch("http://localhost:8080/api/appointment/recent/total").
        then(response => response.json()).
        then(data => {
            if (data.status === "SUCCESS") {
                setTotalAppointments(data.data)
                } else {
                    setTotalAppointments(0)
                    toast.error(data.message)
                }
        })
        // Fetch appointments served today
        const now = new Date()
        const start = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0);
        const end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59, 99);
        fetch("http://localhost:8080/api/appointments", {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            credentials: "include",
            body: JSON.stringify({start, end})
        }).
        then(response => response.json()).
        then(data => {
            setTotalPatients(data.data)
        })
    }, [])


    const [totalPatients, setTotalPatients] = useState(0)
    const [totalAppointments, setTotalAppointments] = useState(0)
    const [activePatients, setActivePatients] = useState(0)
    const [recentAppointments, setIncompleteAppointments] = useState([])

    return (
        <div className={styles.background}>
            <section className={styles.title}>
                <h1>Welcome {firstName || "Receptionist"}</h1>
                <h2>{new Date().toLocaleDateString()}</h2>
                <h2>{new Date().toLocaleTimeString()}</h2>
            </section>
            <section className={styles.overview}>
                <span className={styles.overviewData}>
                    <FaUser/>
                    {totalPatients} patients served today
                </span>
                <span className={styles.overviewData}>
                    <FaHandshake/>
                    {totalAppointments} appointments made today
                </span>
                <span className={styles.overviewData}>
                    <RiNumbersFill/>
                    {activePatients} active patients
                </span>
            </section>
            <section className={styles.actions}>
                <section className={styles.appointmentSection}>
                    <h2>Recent incomplete appointments</h2>
                    {recentAppointments.length == 0 ? <ul><li>All appointments are completed</li></ul> :
                    <table>
                        <thead>
                            <tr>
                                <th>Patient</th>
                                <th>Doctor</th>
                                <th>Time</th>
                                <th>Room</th>
                            </tr>
                        </thead>
                        <tbody>
                            {recentAppointments.map(appointment => 
                                <td>
                                    <tr>{appointment.patient.name}</tr>
                                    <tr>{appointment.doctor.name}</tr>
                                    <tr>{appointment.time}</tr>
                                    <tr>{appointment.roomNumber}</tr>
                                </td>
                            )}
                        </tbody>
                    </table>}
                </section> 
            </section>
        </div>
    );
}

export default Home