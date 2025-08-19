import styles from "./Dashboard.module.css"
import {type JSX, useEffect, useState} from "react";
import {toast} from "react-hot-toast";
import {FaCalendarAlt, FaCalendarCheck, FaCalendarPlus, FaCalendarTimes, FaUserPlus} from "react-icons/fa";
import type {Log} from "../../../Interfaces.tsx";
import PatientForm from "../../../components/forms/patient/PatientForm.tsx";
import Loading from "../../../components/animations/Loading.tsx";

/**
 * A view component that renders the Receptionist's dashboard
 * It displays statistics such as appointments(total, complete, incomplete) and recent logs from the user
 * It provides quick actions to add new patients, book appointments and view previously booked appointments
 */
function Dashboard(): JSX.Element {
    const [isLoading, setIsLoading] = useState(true)
    const [appointmentsToday, setAppointmentsToday] = useState<number>(0);
    const [completed, setCompleted] = useState<number>(0);
    const [incomplete, setIncomplete] = useState<number>(0);
    const [logs, setLogs] = useState<Log[]>([])
    const [showAddPatient, setShowAddPatient] = useState(false)
    const api = import.meta.env.VITE_API_URL + "/api/statistics/receptionist";

    function fetchStatistics(id: number): void {
        setIsLoading(true)
        fetch(`${api}/${id}`)
            .then(async response => {
                const data = await response.json()

                if (!response.ok && response.status === 404) {
                    toast.error("An error has occurred when fetching the statistics!")
                    console.warn(data)
                    return
                }
                return data
            })
            .then(data => {
                setAppointmentsToday(data.total);
                setCompleted(data.complete);
                setIncomplete(data.incomplete);
                setLogs(data.logData)
            })
            .catch(console.warn)
            .finally(() => setIsLoading(false))
    }

    // Hook to fetch the receptionist's statistics
    useEffect(() => {
        const userData = JSON.parse(String(localStorage.getItem("userData")))
        fetchStatistics(userData.id);
    }, []);

    return <div className={styles.background}>
        {showAddPatient && <PatientForm closeFunction={() => setShowAddPatient(false)} onSave={() => setShowAddPatient(false)}/>}
            <>
                <div className={styles.statistics}>
                    <h3>Statistics</h3>
                    <div>
                        <section className={styles.stats}>
                            {isLoading ? <Loading /> : <>
                                <FaCalendarPlus/>
                                <p>{appointmentsToday} appointments made today</p></>}
                        </section>
                        <section className={styles.stats}>
                            {isLoading ? <Loading/> : <>
                            <FaCalendarCheck/>
                            <p>{completed} completed appointments</p>
                            </>}
                        </section>
                        <section className={styles.stats}>
                            {isLoading ? <Loading /> : <>
                            <FaCalendarTimes/>
                            <p>{incomplete} incomplete appointments</p>
                            </>}
                        </section>
                    </div>
                </div>
                <div className={styles.recentActions}>
                    <h3>Recent actions</h3>
                    <ul className={styles.actionList}>
                        {logs.length !== 0 ?
                            logs.map(log => <li key={log.id}>
                                {log.action}
                            </li>)
                            :
                            <li> There are no recent actions</li>
                        }
                    </ul>
                </div>
                <div className={styles.quickActions}>
                    <h3>Quick actions</h3>
                    <div>
                        <button className={styles.quickActionButton } onClick={() => setShowAddPatient(true)}><FaUserPlus/> Add new Patient</button>
                        <button className={styles.quickActionButton } onClick={() => alert("Feature to be implemented soon!")}><FaCalendarPlus/> Create an appointment</button>
                        <button className={styles.quickActionButton } onClick={() => alert("Feature to be implemented soon!")}><FaCalendarAlt/> View appointments</button>
                    </div>
                </div>
            </>
    </div>
}

export default Dashboard