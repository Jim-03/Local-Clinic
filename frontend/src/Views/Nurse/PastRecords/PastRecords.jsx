import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import styles from './PastRecords.module.css';

/**
 * A component to review past vitals data
 */
function PastRecords () {
  const [vitalsList, setVitalsList] = useState([]);
  const [page, setPage] = useState(1);
  const [dateRange, setDateRange] = useState({
    start: new Date(),
    end: new Date()
  });
  const [totalPages, setTotalPages] = useState(1);
  const [showCustomRange, setShowCustomRange] = useState(false);
  const [vitalsForm, setVitalsForm] = useState({});

  // Fetch the list of records on mounting
  useEffect(() => {
  fetch('http://localhost:8080/api/vitals/date', {
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
      setTotalPages(data.data.totalPages);
      setVitalsList(data.data.vitals);
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
      case 'today': {
        const start = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0);
        const end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59);
        setDateRange({ start, end });
        break;
      }
      case 'week': {
        const week = new Date(now);
        week.setDate(week.getDate() - now.getDay());
        const start = new Date(week.getFullYear(), week.getMonth(), week.getDate(), 0, 0);
        const end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59);
        setDateRange({ start, end });
        break;
      }
      case 'month': {
        const start = new Date(now.getFullYear(), now.getMonth(), 1, 0, 0, 0);
        const end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59, 99);
        setDateRange({ start, end });
        break;
      }
      case 'year': {
        const start = new Date(now.getFullYear(), 1, 1, 0, 0, 0, 0);
        const end = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 23, 59, 59, 99);
        setDateRange({ start, end });
        break;
      }
      case 'custom': {
        setShowCustomRange(true);
        break;
      }
      default:
        setShowCustomRange(false);
        break;
    }
  }

  /**
   * Updates the vitals form content
   * @param event The changed object 
   */
  function changeData (event) {
    const { name, value } = event.target;

    setVitalsForm((prev) => ({
      ...prev,
      [name]: value
    }));
  }

  /**
   * Updates the vitals data
   */
  async function updateData(event) {
    event.preventDefault();

    try {
        const response = await fetch(`http:localhost:8080/api/vitals/${vitalsForm.id}`, {
            method: "PUT",
            credentials: "include",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(vitalsForm)
        });
        const data = await response.json()

        if (data.status === "SUCCESS") {
            toast.success(data.message)
            setVitalsForm({})
        } else {
            toast.error(data.message)
        }
    } catch (error) {
        console.error(error)
        toast.error("Connection error")
    }
  } 

  return (
    <div className={styles.background}>
      <section className={styles.title}>
        <h1>Records History</h1>
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
                type='date'
                onChange={(event) => {
                  setDateRange({ start: event.target.value });
                }}
              />
              <label>Ending date</label>
              <input
                type='date'
                onChange={(event) => {
                  setDateRange({ end: event.target.value });
                }}
              />
            </section>
          )}
        </div>
      </section>
      <section className={styles.mainSection}>
        {vitalsList.length === 0 && (
          <p id={styles.noData}>There were no records at the specified time</p>
        )}
        {vitalsForm && Object.keys(vitalsForm).length > 0
          ? (
            <form className={styles.vitalsForm} onSubmit={updateData}>
              <h1>{vitalsForm.record.patient.fullName}</h1>
              <div>
                <label>Record ID:</label>
                <input type='text' value={vitalsForm.record.id} disabled />
              </div>

              <div>
                <label>Body Temperature (°C):</label>
                <input type='number' name='bodyTemperature' value={vitalsForm.bodyTemperature} step='0.1' required onChange={changeData} />
              </div>

              <div>
                <label>Height (cm):</label>
                <input type='number' name='height' value={vitalsForm.height} required onChange={changeData} />
              </div>

              <div>
                <label>Mass (kg):</label>
                <input type='number' name='mass' value={vitalsForm.mass} step='0.1' required onChange={changeData} />
              </div>

              <div>
                <label>Heart Rate (bpm):</label>
                <input type='number' name='heartRate' value={vitalsForm.heartRate} required onChange={changeData} />
              </div>

              <div>
                <label>Systolic Pressure:</label>
                <input type='number' name='systolic' value={vitalsForm.systolic} required onChange={changeData} />
              </div>

              <div>
                <label>Diastolic Pressure:</label>
                <input type='number' name='diastolic' value={vitalsForm.diastolic} required onChange={changeData} />
              </div>

              <div>
                <label>Status:</label>
                <select name='status' value={vitalsForm.status} onChange={changeData}>
                  <option value='INCOMPLETE'>INCOMPLETE</option>
                  <option value='COMPLETE'>COMPLETE</option>
                </select>
              </div>

              <button type='submit'>Save Vitals</button>
              <button
                className={styles.cancelButton} onClick={() => {
                  setVitalsForm({});
                }}
              >Cancel
              </button>
            </form>
            )
          : (
            <ul>
              {vitalsList.map((vitals) => {
                return (
                  <li key={vitals.id} onClick={() => setVitalsForm(vitals)}>
                    {vitals.record.patient.fullName}
                  </li>
                );
              })}
            </ul>
            )}
      </section>
      {
        totalPages !== 1 &&
          <section className={styles.paging}>
            <button
              disabled={page <= 1}
              onClick={() => {
                setPage((prev) => prev - 1);
              }}
            >
              -
            </button>
            <p>{page}</p>
            <button
              disabled={page >= totalPages}
              onClick={() => {
                setPage((prev) => prev + 1);
              }}
            >
              +
            </button>
          </section>
      }
    </div>
  );
}

export default PastRecords;
