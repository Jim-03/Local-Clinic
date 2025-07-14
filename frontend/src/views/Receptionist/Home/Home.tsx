import styles from "./Home.module.css";
import {FaCalendarPlus, FaHistory, FaUserPlus} from "react-icons/fa";
import {useEffect, useState} from "react";

interface Log {
    id: number;
    action: string;
    time: string;
}

/**
 * A view component that renders the receptionist's home page
 * It previews the appointments i.e. complete, incomplete and total
 * TODO: Implement quick actions
 */
function Home() {
    const [appointmentsToday, setAppointmentsToday] = useState<number>(0);
    const [completed, setCompleted] = useState<number>(0);
    const [incomplete, setIncomplete] = useState<number>(0);
    const [logs, setLogs] = useState<Log[]>([])

    const api = import.meta.env.VITE_API_URL + "/api/statistics/receptionist";

    useEffect(() => {
        const userData = JSON.parse(String(localStorage.getItem("userData")))
        const fetchStats = async () => {
            try {
                const response = await fetch(`${api}/${userData.id}`);
                const data = await response.json();
                setAppointmentsToday(data.total);
                setCompleted(data.completed);
                setIncomplete(data.incomplete);
                setLogs(data.logData)
            } catch (e) {
                console.error("Failed to load receptionist stats");
            }
        };

        fetchStats();
    }, []);

    return (
        <div className={styles.homeContainer}>
            <h2>Reception Dashboard</h2>

            <div className={styles.statsGrid}>
                <div className={`${styles.statCard} ${styles.today}`}>
                    <h3>Appointments Today</h3>
                    <p className={styles.statValue}>{appointmentsToday}</p>
                </div>
                <div className={`${styles.statCard} ${styles.complete}`}>
                    <h3>Completed</h3>
                    <p className={styles.statValue}>{completed}</p>
                </div>
                <div className={`${styles.statCard} ${styles.incomplete}`}>
                    <h3>Incomplete</h3>
                    <p className={styles.statValue}>{incomplete}</p>
                </div>
            </div>

            <div className={styles.quickActions}>
                <h3>Quick Actions</h3>
                <div className={styles.actionsGrid}>
                    <button className={styles.actionButton} onClick={() => alert("To be implemented soon!")}>
                        <FaUserPlus/> Add New Patient
                    </button>
                    <button className={styles.actionButton} onClick={() => alert("To be implemented soon!")}>
                        <FaCalendarPlus/> Book Appointment
                    </button>
                    <button className={styles.actionButton} onClick={() => alert("To be implemented soon!")}>
                        <FaHistory/> Appointment History
                    </button>
                </div>
            </div>
            <div className={styles.recentActions}>
                <h3>Recent Actions</h3>
                <ul className={styles.actionList}>
                    {logs.length !== 0 ?
                        logs.map(log => <li>
                            {log.action}
                            <span className={styles.timestamp}>{new Date(log.time).toLocaleString()}</span>
                        </li>)
                        :
                        <li> There are no recent actions</li>
                    }
                </ul>
            </div>

        </div>
    );
}

export default Home;
