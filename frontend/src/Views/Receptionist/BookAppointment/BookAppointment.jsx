import { useState } from 'react';
import styles from './BookAppointment.module.css';
import { FaMagnifyingGlass } from 'react-icons/fa6';
import { FaAddressCard, FaPhone } from 'react-icons/fa';
import { MdEmail } from 'react-icons/md';
import parsePhoneNumberFromString from 'libphonenumber-js';
import toast from 'react-hot-toast';
import AppointmentForm from "../../../Components/AppointmentForm/AppointmentForm.jsx";
import validator from "validator"


/**
 * A React component that searches for a patient's details
 * If the patient is found, it injects another component for booking the appointment
 * @returns {JSX.Element} A React component that allows booking for an appointment
 */
function BookAppointment () {
  const [content, setContent] = useState(
    <form className={styles.form} onSubmit={searchForPatient}>
      <h1>Book An Appointment!</h1>
      <section className={styles.patientSection}>
        <h2>Patient's information</h2>
        <hr />
        <label htmlFor='phoneNumber'>Phone Number:</label>
        <div>
          <FaPhone />
          <input type='tel' placeholder='0712345678' id='phoneNumber' />
        </div>
        <label htmlFor='cardNumber'>National ID number</label>
        <div>
          <FaAddressCard />
          <input type='text' placeholder='237679862739' id='cardNumber' />
        </div>
        <label htmlFor='emailAddress'>Email Address:</label>
        <div>
          <MdEmail />
          <input type='email' placeholder='johnDoe@example.com' id='emailAddress' />
        </div>
        <button type='submit'>
          <FaMagnifyingGlass /> Search
        </button>
      </section>
    </form>
  );

  /**
   * Searches for a patient's details
   * @param event A submit event
   * @returns {Promise<void>} A promise that resolves when the fetch is completed
   */
  async function searchForPatient (event) {
    // Prevent default submit event
    event.preventDefault();

    // Extract patient's data
    const phoneNumber = event.target.phoneNumber.value;
    const email = event.target.emailAddress.value;
    const cardNumber = event.target.cardNumber.value;

    const user = {};

    // Validate the data
    if (phoneNumber) {
      const phone = parsePhoneNumberFromString(phoneNumber, 'KE');
      if (phone && phone.isValid()) {
        user.phone = phoneNumber;
      } else {
        toast.error('Enter a valid phone number');
        return
      }
    }

    if (email) {
        if (validator.isEmail(email)) {
            user.email = email
        } else {
            toast.error("Enter a valid email")
            return
        }
    }

    if (cardNumber) {
        user.cardNumber = cardNumber
    }

    try {
        // Fetch the patient's details
        const response = await fetch("http://localhost:8080/api/patient/search", {
          method: "POST",
          headers: {"Content-Type": "application/json"},
          body: JSON.stringify(user)
        })
      const data = await  response.json()

      // Notify the user
      if(data.status === "SUCCESS") {
        toast.success(data.message)
        setContent(<AppointmentForm patient={data.data}/>)
      } else {
        toast.error(data.message)
      }


    } catch (error) {
      console.error(error)
      toast.error("Connection failed!")
    }
  }
  return <>{content}</>
}

export default BookAppointment;
