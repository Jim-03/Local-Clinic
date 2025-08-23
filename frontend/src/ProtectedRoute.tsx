import {type JSX, useEffect, useState} from "react";
import {useNavigate} from "react-router";

/**
 * A component that serves other components based on the availability of user data
 * Will be used for conditional rendering
 */
function ProtectedRoute(): JSX.Element | null {
    const [jsonData] = useState(localStorage.getItem("userData"))
    const nav = useNavigate()
    const [portal] = useState(<></>)

    useEffect(() => {
        if (!jsonData || jsonData.trim().length === 0) {
            nav("/login", {replace: true})
        }
        }, []);

    return portal
}

export default ProtectedRoute;