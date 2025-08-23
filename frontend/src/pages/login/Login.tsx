import styles from "./Login.module.css";
import { FaCircleNotch, FaEye, FaEyeSlash, FaLock, FaUser } from "react-icons/fa";
import { type JSX, useEffect, useState } from "react";
import { Link, NavLink, useNavigate } from "react-router";
import { toast } from "react-hot-toast";
import { validate } from "email-validator";
import { isValidPhoneNumber } from "libphonenumber-js";

interface user {
    username?: string;
    email?: string;
    phone?: string;
    password: string;
}

/**
 * A component that displays a form to log into the system
 */
function Login(): JSX.Element {
    const [isPasswordVisible, setIsPasswordVisible] = useState<boolean>(false);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [identifier, setIdentifier] = useState<string | null>();
    const [password, setPassword] = useState<string | null>();
    const api = import.meta.env.VITE_API_URL;
    const nav = useNavigate();

    useEffect(() => {
        document.title = "Clinicnet | Login"
    }, []);

    /**
     *  Changes the visibility of the password between true/false
     */
    function changeVisibility(): void {
        setIsPasswordVisible(prev => !prev);
    }

    /**
     * Sends the user credentials to the backend for authentication
     * TODO: JWT authentication
     */
    function logUserIn(): void {
        // Check if both the credentials are provided
        if (!identifier || !password) {
            toast.error("Provide your full account details!");
            return;
        }
        setIsLoading(true);

        // Create the credentials object for sending to the server
        const loginDetails: user = {
            password: password
        };

        // Check the type of identifier
        // Check if email
        if (validate(identifier)) {
            loginDetails.email = identifier;
        } else {
            // Check if phone number else set to username in failure
            try {
                const phone = isValidPhoneNumber(identifier, "KE");

                if (phone) {
                    loginDetails.phone = identifier;
                } else {
                    loginDetails.username = identifier;
                }
            } catch (e) {
                loginDetails.username = identifier;
            }
        }
        // Send user credentials to the server
        fetch(`${api}/api/staff/authenticate`, {
            method: "POST",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(loginDetails)
        }).then(async response => {

            const data = await response.json();

            // Check if the account was found
            if (response.status === 404) {
                toast.error(data.message);
                throw new Error(data.message);
            } else if (!response.ok) { // Any other errors
                toast.error("An error has occurred. Please try again later!");
                console.warn(data);
                throw new Error(data.message);
            }
            return data;
        }).then(data => { // Load the data to local storage for use with other components
            localStorage.setItem("userData", JSON.stringify(data));
            toast.success("Successfully logged in!");
            nav("/", { replace: true });
        }).catch(e => {
            console.error("An error has occurred while logging in: ", e.message);
        })
          .finally(() => setIsLoading(false));

    }

    return <form className={styles.loginForm}>
        <h1>Clinicnet</h1>
        <h2>Login</h2>
        <div className={styles.inputGroup}>
            <FaUser/>
            <input type="text" placeholder="Enter username, email or phone number"
                   onChange={e => setIdentifier(e.target.value.trim().toLowerCase())}/>
        </div>
        <div className={styles.inputGroup}>
            <FaLock/>
            <input type={isPasswordVisible ? "text" : "password"} placeholder={"Enter password"}
                   onChange={e => setPassword(e.target.value.trim().toLowerCase())}/>
            {isPasswordVisible ? <FaEyeSlash onClick={changeVisibility} className={styles.visibilityIcon}/> :
              <FaEye onClick={changeVisibility} className={styles.visibilityIcon}/>}
        </div>
        <button type={"submit"} disabled={isLoading} onClick={e => {
            e.preventDefault();
            logUserIn();
        }} className={styles.loginButton}>{isLoading ? <FaCircleNotch className={styles.spinningCircle}/> : "Login"}
        </button>
        <Link to={""} onClick={() => alert("To be implemented soon!")} className={styles.forgotPassword}>Forgot
            password</Link>
        <p onClick={() => alert("Visit the administrator's office")}>Don't have an account, contact administrator</p>
        <footer>Made by <NavLink target={"_blank"} to={"https://jims-portfolio.vercel.app"}>Jim</NavLink></footer>
    </form>;
}

export default Login;