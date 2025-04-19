import styles from './AddPatient.module.css';
import toast from 'react-hot-toast';

/**
 * A reusable component that adds new patients to the system
 * @returns {JSX.Element} A reusable add patient component
 */
function AddPatient () {
  /**
   * Validates patient's data and adds them to the system
   * @param event On submit event
   * @returns {Promise<void>} A Promise that resolves after the patient's data is sent to the backend
   */
  async function addPatient (event) {
    // Stop default submit event
    event.preventDefault();

    // Get patient's details
    const fullName = event.target.fullName.value;
    const dob = event.target.dateOfBirth.value;
    const email = event.target.email.value;
    const phone = event.target.phoneNumber.value;
    const nationalId = event.target.idNumber.value;
    const gender = event.target.gender.value;
    const address = event.target.address.value;
    const kinName = event.target.kinName.value;
    const kinContact = event.target.kinContact.value;
    const bloodType = event.target.bloodType.value;

    // Validate the data
    const patient = validData({ fullName, dateOfBirth, email, phoneNumber, idNumber, gender, address, kinName, kinContact, bloodType });
    if (!patient) return;

    try {
      // Add the patient's data to the system
      const response = await fetch('/api/patients', {
        credentials: 'include',
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(patient)
      });
      const data = await response.json();

      // Notify the user
      data.status === 'SUCCESS' ? toast.success(data.message) : toast.error(data.message);
    } catch (error) {
      console.error(error);
      toast.error('Connection failed!');
    }
  }

  /**
   * Checks if the patient's data is provided
   * @param {Object} user The patient's data
   * @returns A patient object
   */
  function validData (user) {
    const requiredFields = Object.keys(user);

    for (const field of requiredFields) {
      if (!user[field]) {
        toast.error('Please fill in all fields!');
        return null;
      }
    }

    return user;
  }

  return (
    <>
      <form className={styles.addPatientForm} onSubmit={addPatient}>
        <div className={styles.inputDiv}>
          <fieldset>
            <legend>Basic Details</legend>
            <label className={styles.label} htmlFor='fullName'>Full Name:</label>
            <input className={styles.input} type='text' placeholder='John Doe' id='fullName' />
            <label className={styles.label} htmlFor='dateOfBirth'>Date of Birth</label>
            <input className={styles.input} type='date' id='dateOfBirth' />
            <label className={styles.label} htmlFor='email'>Email Address:</label>
            <input className={styles.input} type='email' placeholder='johnDoe@example.com' id='email' />
            <label className={styles.label} htmlFor='phoneNumber'>Phone Number:</label>
            <input className={styles.input} type='tel' placeholder='0712345678' id='phoneNumber' />
            <label className={styles.label} htmlFor='idNumber'>National ID Number:</label>
            <input className={styles.input} type='text' placeholder='31256346' id='idNumber' />
            <label className={styles.label} htmlFor='gender'>Gender:</label>
            <select className={styles.input} id='gender'>
              <option value='MALE'>Male</option>
              <option value='FEMALE'>Female</option>
            </select>
            <label className={styles.label} htmlFor='address'>Home address:</label>
            <input className={styles.input} type='text' placeholder='Nakuru, kiamunyi' id='address' />
          </fieldset>
          <fieldset>
            <legend>Personal Details</legend>
            <label className={styles.label} htmlFor='kinName'>Next of kin:</label>
            <input className={styles.input} type='text' placeholder='Jane Doe' id='kinName' />
            <label className={styles.label} htmlFor='kinContact'>Next of Kin Name</label>
            <input className={styles.input} type='tel' placeholder='0723456789' id='kinContact' />
            <label className={styles.label} htmlFor='bloodType'>Blood Type:</label>
            <select className={styles.input} id='bloodType'>
              <option value=''>Select Blood Type</option>
              <option value='A+'>A+</option>
              <option value='A-'>A-</option>
              <option value='B+'>B+</option>
              <option value='B-'>B-</option>
              <option value='AB+'>AB+</option>
              <option value='AB-'>AB-</option>
              <option value='O+'>O+</option>
              <option value='O-'>O-</option>
            </select>
          </fieldset>
        </div>
        <div className={styles.buttonsDiv}>
          <button className={styles.addButton} type='submit'>Add</button>
          <button className={styles.resetButton} type='reset'>Reset</button>
        </div>
      </form>
    </>
  );
}

export default AddPatient;
