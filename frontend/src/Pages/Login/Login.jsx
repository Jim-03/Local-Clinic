import { FaUser, FaKey } from "react-icons/fa"
import styles from "./Login.module.css";
import { useEffect, useRef } from "react";
import {parsePhoneNumberFromString} from "libphonenumber-js";
import validator from "validator"
import loginImage from "../../assets/Images/login.jpg"
import { replace, useNavigate } from "react-router-dom";

function Login() {
  const notification = useRef()
  const navigate = useNavigate()

  // Change the title of the page
  useEffect(() => {
    document.title = "Login"
  })

  /**
   * Displays a notification on the UI for 5 seconds
   * @param {("SUCCESS" | "REJECTED" | "NOT_FOUND" | "ERROR")} type The type of notification
   * @param {string} message The message to display
   */
  async function notify(type, message) {
    notification.current.textContent = ''
    notification.current.style["background-color"] = getBackgroundColor(type)
    notification.current.textContent = message
    notification.current.style.display = "block"
    setTimeout(() => {
        notification.current.style.display = "none"
    }, 5000)
  }

  async function login(event) {
    // Prevent default submit event
    event.preventDefault()

    // Get user details
    const identifier = event.target.username.value
    const password = event.target.password.value

    // Check if credentials are provided
    if (!identifier || identifier.length === 0 || !password || password.length === 0) {
        notify("REJECTED", "Provide your account credentials")
        return
    }

    // Create the user object
    const user = {
        password
    }

    // Determine the identifier
    if (identifier && validator.isEmail(identifier)) {
            user.email = identifier
    } else {
        const phone = parsePhoneNumberFromString(identifier)
        if (phone && phone.isValid()) {
            user.phone = identifier
        } else {
            user.username = identifier
        }
    }

    // Authenticate the user
    try {
        const response = await fetch("http://localhost:8080/api/staff/authorize", {
            credentials: "include",
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(user)
        })
        const data = await response.json()

        // Notify the user
        notify(data.status, data.message)

        // Redirect to the dashboard if successful
        if (data.status === "SUCCESS")  {
        sessionStorage.setItem("user", JSON.stringify(data.data))
        navigate("/", {replace: true})
        }
    } catch (error) {
        console.error(error)
        notify("ERROR", "An error has occurred!")
    }
  }
  return (
    <div className={styles.loginBackground}>
      <section className={styles.formSection}>
        <h1>Login</h1>
        <form className={styles.loginForm} onSubmit={login}>
            <p ref={notification} className={styles.notification}></p>
            <label htmlFor="username">Username:</label>
            <div className={styles.inputDiv}>
                <FaUser/>
                <input type="text" placeholder="Enter your username, email or phone number" id="username"/>
            </div>
            <label htmlFor="password">Password</label>
            <div>
                <label htmlFor="showPassword">Show password</label>
                <input type="checkbox" />
            </div>
            <div className={styles.inputDiv}>
                <FaKey/>
                <input type="password" placeholder="Enter your password" id="password"/>
            </div>
            <button type="submit">Login</button>
            <p className={styles.forgot}>Forgot password!</p>
        </form>
      </section>
      <section className={styles.imageSection}>
        <img src={loginImage} className={styles.loginImage}/>
      </section>
    </div>
  );
}

/**
 * Determines the color for the notification
 * @param {("SUCCESS" | "REJECTED" | "NOT_FOUND" | "ERROR")} type The notification type
 * @returns {string} The background color
 */
function getBackgroundColor(type) {
    switch (type) {
        case "SUCCESS":
            return "rgba(0, 128, 0, 0.3)"
        case "REJECTED":
            return "rgba(255, 166, 0, 0.3)"
        case "NOT_FOUND":
            return "rgba(0, 0, 255, 0.3)"
        default:
            return "rgba(255, 0, 0, 0.3)"
    }
}
export default Login;
