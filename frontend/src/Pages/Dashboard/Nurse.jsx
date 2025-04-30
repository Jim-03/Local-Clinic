import { FaBookMedical, FaClipboardList, FaHome } from "react-icons/fa";
import Header from "../../Components/Header/Header";
import SideBar from "../../Components/SideBar/SideBar";
import { RiTestTubeFill } from "react-icons/ri";
import { useState } from "react";
import styles from "./Dashboard.module.css";
import Home from "../../Views/Nurse/Home/Home";
import { GrDocumentTest } from "react-icons/gr";
import IncompleteRecord from "../../Views/Nurse/IncompleteRecord/IncompleteRecord";
import PastRecords from "../../Views/Nurse/PastRecords/PastRecords";

function Nurse() {
  const [viewContent, setViewContent] = useState(<Home />);

  return (
    <div className={styles.background}>
      <Header role="Nurse" />
      <SideBar
        options={[
          {
            icon: <FaHome />,
            operation: "Dashboard",
            service: () => {
              setViewContent(<Home />);
            },
          },
          {
            icon: <FaClipboardList />,
            operation: "Incomplete Records",
            service: () => {
              setViewContent(<IncompleteRecord/>)
            },
          },
          {
            icon: <FaBookMedical />,
            operation: "Past Records",
            service: () => {
              setViewContent(<PastRecords/>)
            },
          },
          {
            icon: <RiTestTubeFill />,
            operation: "Complete Investigations",
            service: null,
          },
          {
            icon: <GrDocumentTest />,
            operation: "Past Investigations",
            service: null,
          },
        ]}
      />
      <div className={styles.viewSection}>{viewContent}</div>
    </div>
  );
}

export default Nurse;
