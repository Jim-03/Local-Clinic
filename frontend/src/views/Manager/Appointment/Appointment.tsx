import styles from "./Appointment.module.css";
import * as React from "react";
import {type JSX, useEffect, useState} from "react";
import toast from "react-hot-toast";
import {MdChevronLeft, MdChevronRight} from "react-icons/md";

interface AppointmentData {
    id: number;
    patient: { id: number; name: string };
    doctor: { id: number; name: string };
    status: "PENDING" | "COMPLETE" | "INCOMPLETE" | "CANCELLED";
    createdAt: Date;
}

/**
 * An appointment view component that displays appointments in the system
 */
function Appointment(): JSX.Element {
    const [appointmentsType, setAppointmentsType] = useState<"PENDING" | "COMPLETE" | "CANCELLED" | "">("");
    const [timePeriod, setTimePeriod] = useState<string>("today");
    const [showCustomDatePicker, setShowCustomDatePicker] = useState<boolean>(false);
    const [startDate, setStartDate] = useState<Date>(new Date());
    const [endDate, setEndDate] = useState<Date>(new Date());
    const [page, setPage] = useState<number>(1);
    const [totalPages, setTotalPages] = useState<number>(1);
    const [appointments, setAppointments] = useState<AppointmentData[]>([]);
    const [filteredAppointments, setFilteredAppointments] = useState<AppointmentData[]>([])
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const api: string = `${import.meta.env.VITE_API_URL}/api/appointment`;

    /**
     * Fetches a list of appointments depending on the page and date range
     */
    async function fetchAppointments(): Promise<void> {
        setIsLoading(true);
        try {
            const params = new URLSearchParams({
                start: startDate.toISOString().replace("Z", ""),
                end: endDate.toISOString().replace("Z", ""),
                page: page.toString(),
            });

            const response: Response = await fetch(`${api}/date/${page}?${params.toString()}`);
            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message);
            }

            const data = await response.json();
            setTotalPages(data.totalPages);
            setAppointments(data.appointments);
        } catch (error) {
            toast.error("Failed to fetch appointments!");
            console.error(error)
            setAppointments([]);
        } finally {
            setIsLoading(false);
        }
    }

    // Hook to change the date range
    useEffect(() => {
        const end: Date = new Date();
        let newStartDate: Date = new Date();

        switch (timePeriod) {
            case "today":
                newStartDate = new Date(end.getFullYear(), end.getMonth(), end.getDate(), 0, 0, 0);
                break;
            case "week":
                newStartDate = new Date(end.getFullYear(), end.getMonth(), end.getDate() - end.getDay(), 0, 0, 0);
                break;
            case "month":
                newStartDate = new Date(end.getFullYear(), end.getMonth(), 1, 0, 0, 0);
                break;
            case "year":
                newStartDate = new Date(end.getFullYear(), 0, 1, 0, 0, 0);
                break;
            case "custom":
                setShowCustomDatePicker(true);
                return;
        }

        setShowCustomDatePicker(false);
        setStartDate(newStartDate);
        setEndDate(new Date(end.getFullYear(), end.getMonth(), end.getDate(), 23, 59, 59));
    }, [timePeriod]);

    // Hook to fetch appointments when component is loaded
    useEffect(() => {
        fetchAppointments();
    }, [startDate, endDate, page, api]);

    useEffect(() => {
        const filter: AppointmentData[] = appointmentsType === ""
            ? appointments
            : appointments.filter(a => a.status === appointmentsType.toUpperCase());


        setFilteredAppointments(filter)
    }, [appointments, appointmentsType]);

    function changeFilter(e: React.ChangeEvent<HTMLSelectElement>) {
        const value = e.target.value as "" | "PENDING" | "COMPLETE" | "CANCELLED";
        setAppointmentsType(value);
        setPage(1);
    }

    return (<div className={styles.appointmentBackground}>
        <section className={styles.header}>
            <h2>Appointments</h2>
            <div>
                <select
                    value={appointmentsType}
                    onChange={changeFilter}
                    disabled={isLoading}
                >
                    <option value="">All</option>
                    <option value="COMPLETE">Completed</option>
                    <option value="PENDING">Pending</option>
                    <option value="CANCELLED">Cancelled</option>
                </select>
            </div>
        </section>

        <section className={styles.dateSection}>
            <div>
                <label>Select Date:</label>
                <select
                    onChange={(e) => setTimePeriod(e.target.value)}
                    value={timePeriod}
                    disabled={isLoading}
                >
                    <option value="today">Today</option>
                    <option value="week">This week</option>
                    <option value="month">This month</option>
                    <option value="year">This year</option>
                    <option value="custom">Custom range</option>
                </select>
            </div>

            {showCustomDatePicker && (<div className={styles.customDatePicker}>
                <div>
                    <label>Start:</label>
                    <input
                        type="date"
                        onChange={(e) => {
                            const start = new Date(e.target.value);
                            start.setHours(0, 0, 0, 0);
                            setStartDate(start);
                        }}
                        max={endDate.toISOString().split('T')[0]}
                    />
                </div>
                <div>
                    <label>End:</label>
                    <input
                        type="date"
                        onChange={(e) => {
                            const end = new Date(e.target.value);
                            end.setHours(23, 59, 59, 999);
                            setEndDate(end);
                        }}
                        min={startDate.toISOString().split('T')[0]}
                    />
                </div>
            </div>)}
        </section>

        <div className={styles.mainView}>
            {isLoading ? (<div className={styles.loading}>Loading appointments...</div>) : appointments.length === 0 ? (
                <div className={styles.noAppointments}>
                    There are no appointments in the specified period!
                </div>) : (<div className={styles.tableContainer}>
                <table className={styles.appointmentsTable}>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Patient</th>
                        <th>Doctor</th>
                        <th>Date</th>
                        <th>Time</th>
                        <th>Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    {filteredAppointments.map((appointment) => (<tr key={appointment.id}>
                        <td>{appointment.id}</td>
                        <td>{appointment.patient.name}</td>
                        <td>{appointment.doctor.name}</td>
                        <td>{appointment.createdAt.toLocaleDateString()}</td>
                        <td>
                            {appointment.createdAt.toLocaleTimeString()}
                        </td>
                        <td>
                                                <span
                                                    className={`${styles.status} ${styles[appointment.status.toLowerCase()]}`}>
                                                    {appointment.status.toLowerCase()}
                                                </span>
                        </td>
                    </tr>))}
                    </tbody>
                </table>

                <div className={styles.pagination}>
                    <button
                        onClick={() => setPage(prev => prev - 1)}
                        disabled={page <= 1 || isLoading}
                    >
                        <MdChevronLeft/>
                    </button>
                    <span>
                                    Page {page} of {totalPages}
                                </span>
                    <button
                        onClick={() => setPage(prev => prev + 1)}
                        disabled={page >= totalPages || isLoading}
                    >
                        <MdChevronRight/>
                    </button>
                </div>
            </div>)}
        </div>
    </div>);
}

export default Appointment;