import {type JSX, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import Admin from "../pages/Dashboard/Admin.tsx";
import Receptionist from "../pages/Dashboard/Receptionist.tsx";

interface User {
    fullName: string;
    role: string;
}

export function ProtectedRoute(): JSX.Element {
    const navigate = useNavigate()
    const [user, setUser] = useState<User | null>(null)

    useEffect(() => {
        const stored = localStorage.getItem("userData")
        if (!stored) {
            navigate("/", {replace: true})
        } else {
            try {
                const parsed: User = JSON.parse(stored)
                setUser(parsed)
            } catch (e) {
                console.error("Invalid user data in storage", e)
                navigate("/", {replace: true})
            }
        }
    }, [navigate])

    if (!user) return <></>

    switch (user.role) {
        case "MANAGER":
            return <Admin userData={{name: user.fullName, role: user.role}}/>
        case "RECEPTIONIST":
            return <Receptionist/>
        default:
            return <></>
    }
}
