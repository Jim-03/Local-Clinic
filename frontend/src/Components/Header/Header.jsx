import styles from "./Header.module.css"
import { useEffect } from "react";

/**
 * The header component of the system
 * @param {string} role The role of the user
 * @returns {JSX.Element} The header component
 */
function Header({role = "Dashboard"}) {
    useEffect(() => {
        document.title = role
    }, [role])

  return (
    <header className={styles.header}>
      <h1>{role}</h1>
    </header>
  );
}
export default Header;
