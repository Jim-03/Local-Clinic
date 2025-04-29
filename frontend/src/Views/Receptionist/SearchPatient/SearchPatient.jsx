import { useEffect, useState } from 'react';
import toast from 'react-hot-toast';
import styles from './SearchPatient.module.css';
import { FaMagnifyingGlass } from 'react-icons/fa6';
import { FaMinus, FaPlus } from 'react-icons/fa';
import { validData } from '../../util.js';

function SearchPatient () {
  const [patients, setPatients] = useState([]);
  const [totalPages, setTotalPages] = useState(1);
  const [page, setPage] = useState(1);

  useEffect(() => {
    // Fetch the list of all patients
    fetch('http://localhost:8080/api/patient/all', {
      method: 'POST',
      credentials: 'include',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ page })
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.status !== 'SUCCESS') {
          toast.error(data.message);
          return;
        }
        setTotalPages(data.data.totalPages);
        setPatients(data.data.patients);
      })
      .catch((error) => {
        console.error(error);
        toast.error('Connection error!');
      });
  }, [page]);

  async function updatePatient (event) {
    event.preventDefault();

    const id = event.target.id.value;
    const fullName = event.target.fullName.value;
    const dateOfBirth = event.target.dateOfBirth.value;
    const email = event.target.email.value;
    const phone = event.target.phone.value;
    const nationalId = event.target.nationalId.value;
    const gender = event.target.gender.value;
    const address = event.target.address.value;
    const kinName = event.target.kinName.value;
    const kinContact = event.target.kinContact.value;
    const bloodGroup = event.target.bloodGroup.value;

    const patient = validData(fullName, dateOfBirth, email, phone, nationalId, gender, address, kinName, kinContact, bloodGroup);

    if (!patient) return;

    try {
      // Update the patient's data
      const response = await fetch(`http://localhost:8080/api/patient/${id}`, {
        credentials: 'include',
        method: 'PUT',
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

  return (
    <div className={styles.searchPatient}>
      <section className={styles.search}>
        <h2>Search For Patient</h2>
        <div>
          <input type='search' placeholder="Enter patient's unique detail" />
          <button>
            <FaMagnifyingGlass />
          </button>
        </div>
      </section>
      <hr />
      <section className={styles.patients}>
        {patients.length === 0
          ? (
            <ul>
              <li>There are no patients at the moment</li>
            </ul>
            )
          : patients.length === 1
            ? (
              <form onSubmit={updatePatient}>
                <label htmlFor='patientNumber'>Patient Number:</label>
                <output>{patients[0].id}</output>
                <label htmlFor='fullName'>Name</label>
                <input type='text' value={patients[0].fullName} id='fullName' />
                <label htmlFor='dateOfBirth'>Date Of Birth</label>
                <input type='date' id='dateOfBirth' value={patients[0].dateOfBirth} />
                <label htmlFor='email'>Email</label>
                <input type='email' id='email' value={patients[0].email} />
                <label htmlFor='phone'>Phone</label>
                <input type='tel' id='phone' value={patients[0].phone} />
                <label htmlFor='nationalId'>ID number</label>
                <input type='text' id='nationalId' value={patients[0].nationalId} />
                <label htmlFor='gender'>Gender</label>
                <select id='gender' value={patients[0].gender}>
                  <option value='MALE'>Male</option>
                  <option value='FEMALE'>Female</option>
                </select>
                <label htmlFor='address'>Home Address</label>
                <input type='text' id='address' value={patients[0].address} />
                <label htmlFor='kinName'>Next of kin name</label>
                <input type='text' id='kinName' value={patients[0].kinName} />
                <label htmlFor='kinContact'>Next of kin contact</label>
                <input type='tel' id='kinContact' value={patients[0].kinContact} />
                <label htmlFor='bloodGroup'>Blood group</label>
                <select id='bloodGroup' value={patients[0].bloodType}>
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
                <button>Update</button>
              </form>
              )
            : (
              <table>
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                  </tr>
                </thead>
                <tbody>
                  {patients.map((patient) => {
                    return (
                      <tr key={patient.id} onClick={() => setPatients([patient])}>
                        <td>{patient.fullName}</td>
                        <td>{patient.email}</td>
                        <td>{patient.phone}</td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
              )}
      </section>
      {
      patients.length > 1
        ? <section className={styles.pageSection}>
          <button disabled={page >= totalPages} onClick={() => setPage((prev) => prev + 1)}><FaPlus /></button>
          <p>{page}</p>
          <button disabled={page === 1} onClick={() => setPage((prev) => prev - 1)}><FaMinus /></button>
        </section>
        : null
        }
    </div>
  );
}
export default SearchPatient;
