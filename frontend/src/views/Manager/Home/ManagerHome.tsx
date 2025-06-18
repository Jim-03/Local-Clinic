import { FaBriefcase, FaClipboard, FaUser } from "react-icons/fa";
import styles from "./Home.module.css";
import { useEffect, useState, type JSX } from "react";
import toast from "react-hot-toast";

interface ActivityItem {
    id: string;
    action: string;
    time: string;
}

/**
 * A home view component for the manager role
 */
function Home(): JSX.Element {
    const [totalStaff, setTotalStaff] = useState<number>(0);
    const [staffOnDuty, setStaffOnDuty] = useState<number>(0);
    const [todaysAppointments, setTodaysAppointment] = useState<number>(0);
    const [activity, setActivity] = useState<ActivityItem[]>([]);
    const [isLoading, setIsLoading] = useState<boolean>(false);

    /**
     * Fetches the statistics to be displayed on the UI
     * @returns {Promise<void>} A promise that resolves once the data is fetched
     */
    async function fetchData(): Promise<void> {
        setIsLoading(true);
        try {
            const response: Response = await fetch(
                `${import.meta.env.VITE_API_URL}/api/statistics/manager`
            );

            if (!response.ok) {
                const data = await response.json();
                toast.error(data.message || "Failed to fetch data");
                return;
            }

            const data = await response.json();
            setTotalStaff(data.totalStaff || 0);
            setStaffOnDuty(data.staffOnDuty || 0);
            setTodaysAppointment(data.dailyAppointments || 0);
            setActivity(data.activity || []);
        } catch (error) {
            toast.error("Network error. Please try again!");
            console.error("Fetch error:", error);
        } finally {
            setIsLoading(false);
        }
    }

    useEffect(() => {
        fetchData();
    }, []);

    return (
        <div className={styles.adminHome}>
            <h2>Statistics</h2>
            {isLoading ? (
                <div>Loading...</div>
            ) : (
                <>
                    <div className={styles.summary}>
                        <div><FaUser /> Total Staff: {totalStaff}</div>
                        <div><FaBriefcase /> Staff on Duty: {staffOnDuty}</div>
                        <div><FaClipboard /> Appointments Today: {todaysAppointments}</div>
                    </div>
                    <div className={styles.quickActions}>
                        <h2>Quick Actions</h2>
                        <div>
                            <button>Add Staff</button>
                            <button>Schedule Appointment</button>
                            <button>Add Medication</button>
                        </div>
                    </div>
                    <div className={styles.activity}>
                        <h2>Recent Activity</h2>
                        <ul>
                            {activity.length > 0 ? (
                                activity.map((item: ActivityItem) => (
                                    <li key={item.id}>
                                        {item.action} — <small>{item.time}</small>
                                    </li>
                                ))
                            ) : (
                                <li>No activity</li>
                            )}
                        </ul>
                    </div>
                </>
            )}
        </div>
    );
}

export default Home;
