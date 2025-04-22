import { FaUser } from "react-icons/fa";
import { RiLogoutBoxFill } from "react-icons/ri";
import styles from "./SideBar.module.css";

/**
 * The system's sidebar component
 * @param {Object} options - Options props
 * @param {string} options.operation - The name of the option
 * @param {string} options.service - A function triggered on click event
 * @returns {JSX.Element} A re-usable sidebar component
 */
function SideBar({ options }) {
  return (
    <nav className={styles.sideBar}>
      {options.map((option, index) => (
        <button key={index} onClick={option.service}>
          {option.icon} {option.operation}
        </button>
      ))}
      <button> <FaUser/> Profile </button>
      <button> <RiLogoutBoxFill/> Logout</button>
    </nav>
  );
}

export default SideBar;
