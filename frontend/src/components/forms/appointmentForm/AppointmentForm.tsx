import styles from './AppointmentForm.module.css';
import * as React from 'react';
import { type JSX, useEffect, useState } from 'react';
import { FaMagnifyingGlass } from 'react-icons/fa6';
import { validate } from 'email-validator';
import { toast } from 'react-hot-toast';
import { isValidPhoneNumber } from 'libphonenumber-js';
import Loading from '../../animations/Loading.tsx';
import { FaCalendarPlus } from 'react-icons/fa';
import { TbCancel } from 'react-icons/tb';
import type { Patient } from '../../../Interfaces.tsx';

interface patient {
  id: number;
  fullName: string;
  phone: string;
}

interface doctor extends patient {
  gender: 'MALE' | 'FEMALE';
}

interface AppointmentFormProps {
  closeFunction: () => void;
  patient?: Patient;
}

/**
 * A component that renders a form used to book appointments
 * It allows searching in case no patient data is provided
 * Once an appointment is successfully booked, it also creates a new billing
 * @param {function} closeFunction Function that unmounts the component
 * @param patient Patient's details
 */
function AppointmentForm({closeFunction, patient}: AppointmentFormProps): JSX.Element {
  const [ patientData, setPatientData ] = useState<patient | Patient>(patient ? patient : {
    id: 0,
    fullName: 'No patient\'s name',
    phone: 'No patient\'s phone number'
  });
  const [ doctorId, setDoctorId ] = useState<number>(0);
  const [ searchParameter, setSearchParameter ] = useState<'email' | 'phone' | 'nid'>('email');
  const [ searchTerm, setSearchTerm ] = useState<string>('');
  const [ isLoading, setIsLoading ] = useState(false);
  const [ activeDoctors, setActiveDoctors ] = useState<doctor[]>([]);
  const api = import.meta.env.VITE_API_URL;
  const [ isBooking, setIsBooking ] = useState(false);

  // Hook to close the form when 'esc' key is pressed
  useEffect(() => {
    function closeWhenEscIsPressed(e: KeyboardEvent) {
      if (e.code === 'Escape') {
        closeFunction();
      }
    }

    window.addEventListener('keydown', closeWhenEscIsPressed);

    return () => window.removeEventListener('keydown', closeWhenEscIsPressed);
  }, []);

  // Hook to fetch doctors on duty
  useEffect(() => {
    fetchActiveDoctors();
  }, []);

  /**
   * Searches for a patient's details based on the search parameter and search term
   */
  function searchForPatient() {
    switch (searchParameter) {
      case 'email':
        if (!validate(searchTerm)) {
          toast.error('Enter a valid email address');
          return;
        }
        break;
      case 'phone':
        try {
          const phone = isValidPhoneNumber(searchTerm, 'KE');

          if (!phone) {
            toast.error('Enter a valid phone number!');
            return;
          }
        } catch {
          toast.error('Enter a valid phone number');
          return;
        }
        break;
    }

    const params: URLSearchParams = new URLSearchParams();
    params.append(searchParameter, searchTerm);

    setIsLoading(true);
    fetch(`${api}/api/patient?${params.toString()}`)
      .then(async response => {
        if (!response.ok) {
          const text = await response.text();

          if (response.status === 404) {
            toast.error('The specified patient wasn\'t found!');
            throw new Error(text);
          }
          throw new Error(text);
        }

        return await response.json();
      })
      .then(data => {
        setPatientData(data);
        toast.success('Patient found');
      })
      .catch(e => {
        console.error('An error has occurred while fetching the patient\'s details: ', e.message);
      })
      .finally(() => setIsLoading(false));
  }

  /**
   * Fetches a list of doctors who are on duty
   */
  function fetchActiveDoctors() {
    fetch(`${api}/api/staff/doctors/active`)
      .then(async response => {
        if (!response.ok) {
          const text: string = await response.text();
          throw new Error(text);
        }

        return await response.json();
      })
      .then(data => {
        if (data.staffList.length === 0) {
          toast.error('There are no doctors on duty right now!');
        }
        setActiveDoctors(data.staffList);
      })
      .catch(e => {
        console.error('An error has occurred while fetching the list of staff:', e.message);
      });
  }

  /**
   * Creates a new appointment in the system
   * @param e Click/Submit event
   */
  function bookAppointment(e: React.MouseEvent<HTMLButtonElement>) {
    e.preventDefault();
    const appointment = {
      patientId: patientData.id,
      doctorId: doctorId,
      receptionistId: Number(JSON.parse(String(localStorage.getItem('userData'))).id)
    };

    setIsBooking(true);
    fetch(`${api}/api/appointment`, {
      method: 'POST',
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(appointment)
    })
      .then(async response => {
        if (!response.ok) {
          const text = JSON.parse(await response.text());

          if (response.status === 404) {
            toast.error(text.message);
          }

          throw new Error(text.message);
        }

        if (response.status === 201) {

          // Create new billing
          const bill = {
            patientId: patientData.id,
            bills: {
              'consultation': 6000.00
            },
            paymentMethod: 'cash',
            amountPaid: 0.00,
            status: 'PENDING'
          };

          fetch(`${api}/api/billing`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(bill)
          }).then(async response => {
            if (!response.ok) {
              const text = JSON.parse(await response.text());

              throw new Error(text.message);
            }
          }).then(() => {
            toast.success('Appointment successfully booked');
            closeFunction();
          }).catch(e => {
            toast.error('Failed to book the appointment. Please try again later!');
            console.error(e);
          });
        }
      })
      .catch(e => {
        console.error('An error has occurred while booking the appointment:', e.message);
      })
      .finally(() => setIsBooking(false));
  }

  return <div className={styles.background} onClick={closeFunction}>
    <form onClick={e => e.stopPropagation()} className={styles.form} onSubmit={e => e.preventDefault()}>
      {isBooking ? <Loading/> :
        <>
          <p onClick={closeFunction} className={styles.closeButton}>&times;</p>
          <h1>Book an appointment</h1>
          {!patient &&
              <div className={styles.searchForPatient}>
                  <select onChange={(e) => setSearchParameter(e.target.value as 'email' | 'phone' | 'nid')}>
                      <option value={'email'}>Email</option>
                      <option value={'phone'}>Phone</option>
                      <option value={'nid'}>National ID</option>
                  </select>
                  <input type={'search'} placeholder={'Search for patient'}
                         onChange={e => setSearchTerm(e.target.value)}/>
                  <button onClick={searchForPatient}><FaMagnifyingGlass/></button>
              </div>
          }
          {patientData && <>
              <h2>Patient's details</h2>
              <div className={styles.patientDetails}>
                {isLoading ? <Loading/> : <div>
                  <section>
                    <label>Name:</label>
                    <input type={'text'} value={patientData.fullName} disabled={true}/>
                  </section>
                  <section>
                    <label>Phone:</label>
                    <input type={'tel'} value={patientData.phone} disabled={true}/>
                  </section>
                </div>
                }
              </div>
          </>}
          <h2>Doctor's details</h2>
          <div className={styles.doctorDetails}>
            <div>
              <label>Select doctor:</label>
              <select onChange={e => setDoctorId(Number(e.target.value))}>
                <option value={'0'}>Random</option>
                {activeDoctors.map(doctor => <option key={doctor.id} value={doctor.id}>
                  {`Dr. ${doctor.fullName}`} | {doctor.gender[0]}
                </option>)}
              </select>
            </div>
          </div>
          <div className={styles.consultationFee}>
            <label>Consultation Fee:</label>
            <input readOnly value={'6000 Kshs'} disabled={true}/>
          </div>
          <div className={styles.actionButtons}>
            <button className={styles.bookButton} onClick={bookAppointment}><FaCalendarPlus/>Book</button>
            <button className={styles.cancelButton} onClick={closeFunction}><TbCancel/>Cancel</button>
          </div>
        </>
      }
    </form>
  </div>;
}

export default AppointmentForm;