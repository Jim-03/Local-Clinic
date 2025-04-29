import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import styles from './AppointmentHistory.module.css';

/**
 * A component to review appointments
 * @returns {JSX.Element} A div component that allows the receptionist to view past appointments
 */
function AppointmentHistory () {
  const [appointmentList, setAppointmentList] = useState([]);
  const [page, setPage] = useState(0);
  const [dateRange, setDateRange] = useState({
    start: new Date(),
    end: new Date()
  });
  const [totalPages, setTotalPages] = useState(1);
  const [showCustomRange, setShowCustomRange] = useState(false);

  // Fetch the list of appointments on mounting
  useEffect(() => {
    fetch('http://localhost:8080/api/appointments/history', {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        start: dateRange.start,
        end: dateRange.end,
        page
      })
    }).then(response => response.json())
      .then(data => {
        // Check if the response is okay
        if (data.status !== 'SUCCESS') {
          toast.error(data.message);
          return;
        }
        setTotalPages(data.totalPages);
        setAppointmentList(data.appointments);
      }).catch(error => {
        console.error(error);
        toast.error('Connection error!');
      });
  }, [dateRange, page]);

    /**
     * A handler that sets the date range
     * @param event An on change event
     */
  function changeDate (event) {
    // Remove the custom date picker
    setShowCustomRange(false);

    const value = event.target.value;
    const now = new Date();

    switch (value) {
      case 'today':
      {
        const start = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0);
        const end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59, 99);
        setDateRange({ start, end });
        break;
      }
      case 'week':
      {
        const week = new Date(now);
        week.setDate(week.getDate() - now.getDay());
        const start = new Date(week.getFullYear(), week.getMonth(), week.getDate(), 0, 0, 0);
        const end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59);
        setDateRange({ start, end });
        break;
      } case 'month':
      {
        const start = new Date(now.getFullYear(), now.getMonth(), 1, 0, 0, 0);
        const end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59, 99);
        setDateRange({ start, end });
        break;
      } case 'year':
      {
        const start = new Date(now.getFullYear(), 1, 1, 0, 0, 0, 0);
        const end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59, 99);
        setDateRange({ start, end });
        break;
      } case 'custom':
      {
        setShowCustomRange(true);
        break;
      } default:
        setShowCustomRange(false);
        break;
    }
  }

  return (
    <div className={styles.background}>
      <section className={styles.title}>
        <h1>Appointment History</h1>
        <div className={styles.dates}>
          <label>Date</label>
          <select id='date' onChange={changeDate}>
            <option value='today'>Today</option>
            <option value='week'>This week</option>
            <option value='month'>This month</option>
            <option value='year'>This year</option>
            <option value='custom'>Custom range</option>
          </select>
          {showCustomRange && (
            <section>
              <label>Starting date</label>
              <input
                type='date' onChange={(event) => {
                  setDateRange({ start: event.target.value });
                }}
              />
              <label>Ending date</label>
              <input
                type='date' onChange={(event) => {
                  setDateRange({ end: event.target.value });
                }}
              />
            </section>
          )}
        </div>
      </section>
      <section className={styles.mainSection}>
        {appointmentList.length === 0 && <p id={styles.noData}>There were no appointments at the specified time</p>}
        {
            appointmentList.length !== 0 &&
              <table>
                <thead>
                  <tr>
                    <th>Date</th>
                    <th>Time</th>
                    <th>Patient</th>
                    <th>Doctor</th>
                  </tr>
                </thead>
                <tbody>
                  {appointmentList.map((appointment) => (
                    <tr key={appointment.id}>
                      <td>{new Date(appointment.createdAt).toLocaleDateString()}</td>
                      <td>{new Date(appointment.createdAt).toLocaleTimeString()}</td>
                      <td>{appointment.patient.name}</td>
                      <td>{appointment.doctor.name}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
        }
      </section>
      <section className={styles.paging}>
        <button disabled={page <= 1} onClick={() => { setPage(prev => prev - 1); }}>-</button>
        <p>{page}</p>
        <button disabled={page >= totalPages} onClick={() => { setPage(prev => prev + 1); }}>+</button>
      </section>
    </div>
  );
}

export default AppointmentHistory;
