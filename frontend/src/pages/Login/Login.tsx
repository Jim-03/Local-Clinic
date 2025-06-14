import { FaEye, FaEyeSlash, FaKey, FaUser } from "react-icons/fa";
import { useEffect, useState, type JSX } from "react";
import login from "../../assets/images/login.jpg";
import styles from "./Login.module.css";
import toast from "react-hot-toast";
import validator from "email-validator";
import { parsePhoneNumber } from "libphonenumber-js/min";
import { useNavigate } from "react-router-dom";
import.meta.env.VITE_API_URL

interface UserDetails {
    username?: string;
    email?: string;
    phone?: string;
    password: string;
}

/**
 * Renders a page used by the users to login in to the system
 * @returns {JSX.Element} Login Page
 */
function Login(): JSX.Element {
    const [isPasswordVisible, setPasswordVisible] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        document.title = "Login"
    })


    /**
     * Changes password visibility to on/off depending on previous state
     */
    function togglePasswordVisibility(): void {
        setPasswordVisible((prev) => !prev);
    }

    /**
     * Sends user's credentials for authentication
     * @param {React.FormEvent<HTMLFormElement>} e Form submit event
     * @returns {Promise<void>} A promise that resolves once the operation is completed
     */
    async function logUserIn(e: React.FormEvent<HTMLFormElement>): Promise<void> {
        e.preventDefault();
        setIsLoading(true);

        const form = new FormData(e.currentTarget);
        const identifier = String(form.get("identifier")).trim();
        const password = String(form.get("password")).trim();

        if (!identifier || !password) {
            toast.error("Please provide your account details!");
            setIsLoading(false);
            return;
        }

        const user: UserDetails = { password };

        // Determine identifier type
        if (validator.validate(identifier)) {
            user.email = identifier;
        } else {
            try {
                const phone = parsePhoneNumber(identifier, "KE");
                if (phone.isValid()) {
                    user.phone = identifier;
                }
            } catch {
                user.username = identifier;
            }
        }

        try {
            const api = import.meta.env.VITE_API_URL
            const response = await fetch(`${api}/api/staff/authenticate`, {
                credentials: "include",
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(user)
            });

            if (!response.ok) {
                const errorData = await response.json()
                const errorMessage = errorData.message
                toast.error(errorMessage)
                return;
            }

            const data = await response.json()
            localStorage.setItem("userData", JSON.stringify(data))
            toast.success("Login successful!")
            navigate("/", { replace: true })
        } catch (error) {
            console.error(error)
            toast.error("An unexpected error has occurred!")
        } finally {
            setIsLoading(false)
        }
    }

    return (
        <div className={styles.loginBackground}>
            <section className={styles.formSection}>
                <form onSubmit={logUserIn}>
                    <h1>Login</h1>
                    <section>
                        <FaUser />
                        <input
                            type="text"
                            required
                            placeholder="Enter username, phone number or email"
                            name="identifier"
                            autoComplete="username"
                        />
                    </section>
                    <section>
                        <FaKey />
                        <input
                            type={isPasswordVisible ? "text" : "password"}
                            required
                            placeholder="Enter your password"
                            name="password"
                            autoComplete="current-password"
                        />
                        {isPasswordVisible ? (
                            <FaEye
                                className={styles.viewPassword}
                                onClick={togglePasswordVisibility}
                                aria-label="Hide password"
                            />
                        ) : (
                            <FaEyeSlash
                                className={styles.viewPassword}
                                onClick={togglePasswordVisibility}
                                aria-label="Show password"
                            />
                        )}
                    </section>
                    <button className={styles.loginButton} disabled={isLoading}>
                        {isLoading ? "Logging in..." : "Login"}
                    </button>
                </form>
            </section>
            <section className={styles.imageSection}>
                <img src={login} alt="Login illustration" />
            </section>
        </div>
    );
}

export default Login;