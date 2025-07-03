import styles from "./Report.module.css";
import {type JSX, useEffect, useRef, useState} from "react";
import {MdPrint} from "react-icons/md";

interface ReportData {
    appointments: {
        total: number;
        completed: number;
        pending: number;
        cancelled: number;
    };
    doctors: Array<{
        id: number;
        name: string;
        completed: number;
        cancelled: number;
        revenue: number;
    }>;
    revenue: {
        expected: number;
        total: number;
    };
}

/**
 * A view component that renders a report for the manager
 */
function Reports(): JSX.Element {
    const [reportData, setReportData] = useState<ReportData | null>(null);
    const [loading, setLoading] = useState(true);
    const [startDate, setStartDate] = useState<Date>(new Date())
    const [endDate, setEndDate] = useState<Date>(new Date())
    const [timeRange, setTimeRange] = useState<"week" | "month" | "year">("month");
    const reportRef = useRef<HTMLDivElement>(null);
    const api: string = `${import.meta.env.VITE_API_URL}/api/report/manager`

    // Change the date range
    useEffect(() => {
        let startingDate: Date = new Date()
        const endingDate: Date = new Date()

        switch (timeRange) {
            case "week":
                startingDate = new Date(endingDate.getFullYear(), endingDate.getMonth(), endingDate.getDate() - endingDate.getDay(), 0, 0, 0)
                break
            case "month":
                startingDate = new Date(endingDate.getFullYear(), endingDate.getMonth(), 1, 0, 0, 0)
                break
            case "year":
                startingDate = new Date(endingDate.getFullYear(), 0, 1, 0, 0, 0)
                break
        }

        setStartDate(startingDate)
        setEndDate(endingDate)
    }, [timeRange]);

    // Fetch report data from API
    useEffect(() => {
        const fetchReportData = async () => {
            setLoading(true);
            try {
                const params : URLSearchParams = new URLSearchParams({
                    start: startDate.toISOString().replace("Z", ""),
                    end: endDate.toISOString().replace("Z", "")
                })
                const response = await fetch(`${api}?${params.toString()}`);
                const data = await response.json();

                if (!response.ok) {
                    throw new Error(data)
                }
                setReportData(data);
            } catch (error) {
                console.error("Failed to fetch report data:", error);
            } finally {
                setLoading(false);
            }
        };

        fetchReportData();
    }, [startDate, endDate]);

    /**
     * Calculate percentage for progress bars
      */
    const calculatePercentage = (value: number, total: number) => {
        return total > 0 ? Math.round((value / total) * 100) : 0;
    };

    if (loading) {
        return <div className={styles.loading}>Loading report data...</div>;
    }

    if (!reportData) {
        return <div className={styles.error}>Failed to load report data</div>;
    }

    return (
        <div className={styles.reportsContainer} ref={reportRef}>
            <div className={`${styles.header} no-print`}>
                <h1>Clinic Reports</h1>
                <div className={styles.controls}>
                    <select
                        value={timeRange}
                        onChange={(e) => setTimeRange(e.target.value as "week" | "month" | "year")}
                        className={styles.timeSelect}
                    >
                        <option value="week">This Week</option>
                        <option value="month">This Month</option>
                        <option value="year">This Year</option>
                    </select>
                    <button onClick={() => alert("Print functionality to be implemented soon!")}
                            className={styles.printButton}>
                        <MdPrint size={20}/> Print Report
                    </button>
                </div>
            </div>

            {/* Appointments Summary */}
            <section className={styles.section}>
                <h2>Appointments Summary</h2>
                <div className={styles.grid}>
                    <div className={styles.card}>
                        <h3>Total Appointments</h3>
                        <p className={styles.bigNumber}>{reportData.appointments.total}</p>
                    </div>

                    <div className={styles.card}>
                        <h3>Completed</h3>
                        <p className={styles.bigNumber}>{reportData.appointments.completed}</p>
                        <div className={styles.progressBar}>
                            <div
                                className={styles.progressFill}
                                style={{
                                    width: `${calculatePercentage(reportData.appointments.completed, reportData.appointments.total)}%`,
                                    backgroundColor: "#4CAF50"
                                }}
                            ></div>
                        </div>
                    </div>

                    <div className={styles.card}>
                        <h3>Pending</h3>
                        <p className={styles.bigNumber}>{reportData.appointments.pending}</p>
                        <div className={styles.progressBar}>
                            <div
                                className={styles.progressFill}
                                style={{
                                    width: `${calculatePercentage(reportData.appointments.pending, reportData.appointments.total)}%`,
                                    backgroundColor: "#FFC107"
                                }}
                            ></div>
                        </div>
                    </div>

                    <div className={styles.card}>
                        <h3>Cancelled</h3>
                        <p className={styles.bigNumber}>{reportData.appointments.cancelled}</p>
                        <div className={styles.progressBar}>
                            <div
                                className={styles.progressFill}
                                style={{
                                    width: `${calculatePercentage(reportData.appointments.cancelled, reportData.appointments.total)}%`,
                                    backgroundColor: "#F44336"
                                }}
                            ></div>
                        </div>
                    </div>
                </div>
            </section>

            {/* Doctors Performance */}
            <section className={styles.section}>
                <h2>Doctors Performance</h2>
                <div className={styles.tableWrapper}>
                    <table className={styles.doctorsTable}>
                        <thead>
                        <tr>
                            <th>Doctor</th>
                            <th>Completed</th>
                            <th>Cancelled</th>
                            <th>Revenue</th>
                            <th>Efficiency</th>
                        </tr>
                        </thead>
                        <tbody>
                        {reportData.doctors.map((doctor) => (
                            <tr key={doctor.id}>
                                <td>{`Dr. ${doctor.name}`}</td>
                                <td>{doctor.completed}</td>
                                <td>{doctor.cancelled}</td>
                                <td>Kshs. {doctor.revenue}</td>
                                <td>
                                    <div className={styles.progressBar}>
                                        <div
                                            className={styles.progressFill}
                                            style={{
                                                width: `${calculatePercentage(doctor.completed, doctor.completed + doctor.cancelled)}%`,
                                                backgroundColor: "#2196F3"
                                            }}
                                        ></div>
                                    </div>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </section>

            {/* Revenue Summary */}
            <section className={styles.section}>
                <h2>Revenue Summary</h2>
                <div className={styles.revenueGrid}>
                    <div className={`${styles.card} ${styles.revenueCard}`}>
                        <h3>Total Expected Revenue</h3>
                        <p className={styles.revenueNumber}>Kshs. {reportData.revenue.expected}</p>
                    </div>
                    <div className={`${styles.card} ${styles.revenueCard}`}>
                        <h3>Total Paid Revenue</h3>
                        <p className={styles.revenueNumber}>Kshs. {reportData.revenue.total}</p>
                    </div>


                </div>
            </section>

            <div className={`${styles.footer} no-print`}>
                <p>Report generated on {new Date().toLocaleDateString()}</p>
            </div>
        </div>
    );
}

export default Reports