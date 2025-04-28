import { FaBookMedical, FaClipboardList, FaUser } from "react-icons/fa";
import styles from "./Home.module.css";
import { useEffect, useState } from "react";
import { FaNoteSticky } from "react-icons/fa6";
import toast from "react-hot-toast";

function Home() {
  const [completeReviews, setCompleteReviews] = useState([]);
  const [incompleteReviews, setIncompleteReviews] = useState([]);
  const [completedInvestigations, setCompletedInvestigations] = useState([]);
  const [incompleteInvestigations, setIncompleteInvestigations] = useState([]);

  useEffect(() => {
    // Fetch the list of reviews
    fetch("http://localhost:8080/api/vitals/today")
      .then((response) => response.json())
      .then((data) => {
        if (data.status === "SUCCESS") {
          // Filter out complete and incomplete vitals reviews
          setCompleteReviews(
            data.data.filter((review) => review.status === "COMPLETE")
          );
          setIncompleteReviews(
            data.data.filter((review) => review.status === "INCOMPLETE")
          );
        } else {
          toast.error(data.message);
        }
      })
      .catch((error) => {
        toast.error("connection error");
        console.error(error);
      });

    // Fetch investigations done during the day
    fetch("http://localhost:8080/api/labtest/today")
      .then((response) => response.json())
      .then((data) => {
        if (data.status === "SUCCESS") {
          // Filter out the investigations

          setCompletedInvestigations(
            data.data.filter((test) => test.status === "REVIEWING")
          );
          setIncompleteInvestigations(
            data.data.filter((test) => test.status === "PENDING_REVIEW")
          );
        } else {
          toast.error(data.message);
        }
      })
      .catch((error) => {
        toast.error("Connection error");
        console.error(error);
      });
  });

  return (
    <div className={styles.homeBackground}>
      <section className={styles.overview}>
        <section className={styles.overviews}>
          <FaUser />
          <p>{completeReviews.length} reviews processed today</p>
        </section>
        <section className={styles.overviews}>
          <FaClipboardList />
          <p>{incompleteReviews.length} incomplete reviews</p>
        </section>
        <section className={styles.overviews}>
          <FaBookMedical />
          <p>{incompleteInvestigations.length} pending investigations</p>
        </section>
        <section className={styles.overviews}>
          <FaNoteSticky />
          <p>{completedInvestigations.length} investigations handed</p>
        </section>
      </section>
      <h2>Pending Reviews</h2>
      <section className={styles.pendingSection}>
        {incompleteReviews.length === 0 ? (
          <ul>
            <li>No pending reviews</li>
          </ul>
        ) : (
          <ol>
            {incompleteReviews.map((review) => {
              <li key={review.id}>{review.patient.name}</li>;
            })}
          </ol>
        )}
      </section>
    </div>
  );
}

export default Home;
