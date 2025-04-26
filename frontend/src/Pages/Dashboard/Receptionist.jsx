import Header from "../../Components/Header/Header";
import SideBar from "../../Components/SideBar/SideBar";
import {
    FaBookMedical,
    FaClipboardList, FaHome,
    FaUser,
    FaWrench,
} from "react-icons/fa";
import { IoPersonAdd } from "react-icons/io5";
import styles from "./Dashboard.module.css";
import BookAppointment from "../../Views/Receptionist/BookAppointment/BookAppointment";
import { useState } from "react";
import AddPatient from "../../Views/Receptionist/AddPatient/AddPatient";
import AppointmentHistory from "../../Views/Receptionist/AppointmentHistory/AppointmentHistory.jsx";
import {FaMagnifyingGlass} from "react-icons/fa6";
import Home from "../../Views/Receptionist/Home/Home.jsx";
import SearchPatient from "../../Views/Receptionist/SearchPatient/SearchPatient.jsx";

function ReceptionistDashboard() {
  const [viewContent, setViewContent] = useState(<Home />);

  function addPatient() {
    setViewContent(<AddPatient />);
  }

  function bookAppointment() {
    setViewContent(<BookAppointment />);
  }

  function showAppointments() {
    setViewContent(<AppointmentHistory/>)
  }
  
  function searchForPatient() {
      setViewContent(<SearchPatient/>)
  }

  function showDashboard() {
      setViewContent(<Home firstName="Jimmy"/>)
  }
  return (
    <div className={styles.background}>
      <Header role="Receptionist" />
      <SideBar
        options={[{
            icon: <FaHome/>,
            operation: "Dashboard",
            service: showDashboard
        },
          {
            icon: <IoPersonAdd />,
            operation: "Add Patient",
            service: addPatient,
          }, {
            icon: <FaMagnifyingGlass/>,
                operation: "Search Patient",
                service: searchForPatient
            },
          {
            icon: <FaBookMedical />,
            operation: "Book Appointment",
            service: bookAppointment,
          },
          {
            icon: <FaClipboardList />,
            operation: "Appointment History",
            service: showAppointments,
          },
        ]}
      />
      <div className={styles.viewSection}>{viewContent}</div>
    </div>
  );
}
export default ReceptionistDashboard;
