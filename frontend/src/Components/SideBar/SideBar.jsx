import { FaUser } from "react-icons/fa";
import { RiLogoutBoxFill } from "react-icons/ri";
import styles from "./SideBar.module.css";
import { useNavigate } from "react-router-dom";

/**
 * The system's sidebar component
 * @param {Object} options - Options props
 * @param {string} options.operation - The name of the option
 * @param {string} options.service - A function triggered on click event
 * @returns {JSX.Element} A re-usable sidebar component
 */
function SideBar({ options }) {
  const navigate = useNavigate()

  return (
    <nav className={styles.sideBar}>
      {options.map((option, index) => (
        <button key={index} onClick={option.service}>
          {option.icon} {option.operation}
        </button>
      ))}
      <button> <FaUser/> Profile </button>
      <button onClick={() => {
        // Clear out the user's data
        sessionStorage.setItem("userData", null)

        // Re-direct to the login page
        navigate("/login")
      }}> <RiLogoutBoxFill/> Logout</button>
    </nav>
  );
}

export default SideBar;
