import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import styles from './AppointmentForm.module.css';

/**
 * A React Component for booking an appointment
 * @param patient The patient prop
 * @returns {JSX.Element} A Re-usable component for adding appointments for patients
 */
function AppointmentForm ({ patient }) {
  const [doctors, setDoctors] = useState([]);
  const [selectedDoctorId, setSelectedDoctorId] = useState('');
  const [specialization, setSpecialization] = useState('Any');
  const [roomNumber, setRoomNumber] = useState('General');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchListOfDoctors().catch(error => {
      console.error(error)
      toast.error("An error has occurred")
    });
  }, []);

  /**
   * Retrieves a list of doctors from the backend
   * @returns {Promise<void>} A promise that resolves when the list is fetched
   */
  async function fetchListOfDoctors () {
    try {
      const response = await fetch('http://localhost:8080/api/staff', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ role: 'DOCTOR', page: 1 })
      });

      const data = await response.json();

      // Check if the retrieval was successful
      if (data.status !== 'SUCCESS') {
        toast.error('No doctors at the moment!');
        return;
      }

      // Extract the list of doctors who are active for appointment
      const activeDoctors = data.data.filter(doctor => doctor.isActive);

      // Check if any doctor is active
      if (!activeDoctors.length) {
        toast.error('All doctors are occupied. Please wait!');
        return;
      }

      setDoctors(activeDoctors);
    } catch (error) {
      console.error(error);
      toast.error('Connection failed!');
    } finally {
      setLoading(false);
    }
  }

  /**
   * Handles on change event on the doctor's select tag
   */
  function handleDoctorChange (event) {
    const doctorId = event.target.value;
    setSelectedDoctorId(doctorId);

    // Fetch the doctor's information
    const doctor = doctors.find(doc => doc.id === doctorId);

    // Set the doctor's details
    if (doctor) {
      setSpecialization(doctor.specialization || 'Any');
      setRoomNumber(doctor.roomNumber || 'General');
    }
  }

  /**
   * Adds a new appointment data to the system
   * @param event Submit event
   * @returns {Promise<void>} A promise that resolves once the appointment data is sent
   */
  async function bookAppointment (event) {
    // Prevent default submit event
    event.preventDefault();

    // Check if doctor is selected
    if (!selectedDoctorId) {
      toast.error('Please select a doctor.');
      return;
    }

    try {
      /*
        * TODO Appointments API
        */
      const response = await fetch('http://localhost:8080/api/appointments', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          patientId: patient.id,
          doctorId: selectedDoctorId,
          date: new Date().toISOString()
        })
      });

      const result = await response.json();
      if (result.status === 'SUCCESS') {
        toast.success('Appointment booked successfully!');
      } else {
        toast.error('Booking failed.');
      }
    } catch (error) {
      console.error(error);
      toast.error('Could not book appointment.');
    }
  }

  /**
   * Calculates a person's age from there date of birth
   * @param {Date} dob The date of birth
   * @returns {number} The patient's age
   */
  const calculateAge = (dob) => {
    const birthDate = new Date(dob);
    const today = new Date();
    return today.getFullYear() - birthDate.getFullYear();
  };

  return (
    <div className={styles.appointmentView}>
      <h1 className={styles.heading}>Book Appointment</h1>

      <section>
        <form className={styles.appointmentForm} onSubmit={bookAppointment}>
          <fieldset>
            <h2 className={styles.subHeading}>Patient's Details</h2>
            <label>Name</label>
            <output>{patient.fullName}</output>
            <label>Email</label>
            <output>{patient.email}</output>
            <label>National ID</label>
            <output>{patient.nationalId}</output>
            <label>Phone</label>
            <output>{patient.phone}</output>
            <label>Age</label>
            <output>{calculateAge(patient.dateOfBirth)}</output>
          </fieldset>

          <fieldset>
            <h2 className={styles.subHeading}>Select a doctor</h2>
            <label htmlFor='doctor'>Doctor Name</label>
            <select id='doctor' onChange={handleDoctorChange} value={selectedDoctorId}>
              <option value=''>Select Doctor</option>
              {doctors.map(doctor => (
                <option key={doctor.id} value={doctor.id}>{doctor.fullName}</option>
              ))}
            </select>

            <label>Room Number:</label>
            <output>{roomNumber}</output>

            <label>Specialization:</label>
            <output>{specialization}</output>
          </fieldset>

          <button type='submit' disabled={loading}>
            {loading ? 'Loading...' : 'Book Appointment'}
          </button>
        </form>
      </section>
    </div>
  );
}

export default AppointmentForm;
