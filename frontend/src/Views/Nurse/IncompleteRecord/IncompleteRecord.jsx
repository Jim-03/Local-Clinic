import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import styles from './IncompleteRecord.module.css';

/**
 * A component that fetches a list of incomplete records in the database
 */
function IncompleteRecord () {
  const [page, setPage] = useState(0);
  const [incompleteRecords, setIncompleteRecords] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');

  useEffect(() => {
    fetch('http://localhost:8080/api/records/incomplete', {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ page })
    })
      .then((response) => response.json())
      .then((data) => {
        toast(data.message);

        if (data.status === 'SUCCESS') {
          const filtered = data.data.filter(
            (record) => record.status === 'MISSING_VITALS'
          );
          setIncompleteRecords(filtered);
        }
      })
      .catch((error) => {
        console.error(error);
        toast.error('Connection error');
      });
  }, [page]);

  const filteredRecords = incompleteRecords.filter((record) =>
    record.patient.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  /**
   * Searches for a specific record while typing
   * @param {import("react").ChangeEvent} event A change event
   */
  function searchRecord (event) {
    setSearchTerm(event.target.value);
  }

  /**
   * Adds the new vitals details to a patient's record
   * @param {SubmitEvent} event A submit event
   */
  function addVitals (event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const recordId = formData.get('recordId');
    const temperature = formData.get('temperature');
    const height = formData.get('height');
    const mass = formData.get('mass');
    const heartRate = formData.get('heartRate');
    const systolicNumber = formData.get('systolicNumber');
    const diastolicNumber = formData.get('diastolicNumber');

    if (
      !temperature ||
      !height ||
      !mass ||
      !heartRate ||
      !systolicNumber ||
      !diastolicNumber
    ) {
      toast.error('Provide all details');
      return;
    }

    fetch('http://localhost:8080/api/vitals', {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        temperature,
        recordId,
        mass,
        height,
        heartRate,
        systolicNumber,
        diastolicNumber
      })
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.status === 'SUCCESS') {
          toast.success(data.message);
          setPage(1);
        } else {
          toast.error(data.message);
        }
      })
      .catch((error) => {
        console.error(error);
        toast.error('Connection error');
      });
  }

  if (incompleteRecords.length === 0) {
    return (
      <div className={styles.background}>
        <h1>Incomplete Records</h1>
        <p>No incomplete records found.</p>
      </div>
    );
  }

  if (filteredRecords.length === 1) {
    const record = filteredRecords[0];
    return (
      <div className={styles.background}>
        <form className={styles.addVitals} onSubmit={addVitals}>
          <h1>{record.patient.name}</h1>
          <input type='hidden' name='recordId' value={record.id} />

          <label>Temperature (°C):</label>
          <input type='number' min='1' name='temperature' required />

          <label>Height (cm):</label>
          <input type='number' min='1' name='height' required />

          <label>Mass (kg):</label>
          <input type='number' min='1' name='mass' required />

          <label>Heart Rate (bpm):</label>
          <input type='number' min='1' name='heartRate' required />

          <label>Systolic Number:</label>
          <input type='number' min='1' name='systolicNumber' required />

          <label>Diastolic Number:</label>
          <input type='number' min='1' name='diastolicNumber' required />

          <div className={styles.buttonGroup}>
            <button
              type='button'
              onClick={() => setPage(1)}
              className={styles.cancelButton}
            >
              Cancel
            </button>
            <button type='submit' className={styles.addButton}>
              Add
            </button>
            <button type='reset' className={styles.clearButton}>
              Clear
            </button>
          </div>
        </form>
      </div>
    );
  }

  return (
    <div className={styles.background}>
      <h1>Incomplete Records</h1>

      <section className={styles.searchSection}>
        <label htmlFor='search'>Search for patient:</label>
        <input
          id='search'
          type='search'
          placeholder="Enter patient's name"
          value={searchTerm}
          onChange={searchRecord}
        />
      </section>

      <ul>
        {filteredRecords.map((record) => (
          <li
            key={record.createdAt}
            onClick={() => {
              setIncompleteRecords([record]);
            }}
          >
            {record.patient.name} -{' '}
            {new Date(record.createdAt).toLocaleString()}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default IncompleteRecord;
