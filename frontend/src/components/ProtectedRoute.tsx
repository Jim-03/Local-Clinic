import {type JSX} from "react";
import Login from "../pages/Login/Login.tsx";
import Admin from "../pages/Dashboard/Admin.tsx";
import Receptionist from "../pages/Dashboard/Receptionist.tsx";

/**
 * A component that serves other components based on the user's role
 * In case the user isn't authenticated, it renders the login component by default
 */
export function ProtectedRoute(): JSX.Element {
    const userData = sessionStorage.getItem("userData")

    if (userData === null) {
        return <Login/>
    } else {
        const user = JSON.parse(userData)

        switch (user.role) {
            case "MANAGER":
                return <Admin/>
            case "RECEPTIONIST":
                return <Receptionist/>
        }
    }

    return <Login/>
}