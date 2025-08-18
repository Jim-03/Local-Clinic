import {FaUser} from "react-icons/fa";
import {useEffect, useState} from "react";

type props = {
    user: {
        fullName: string;
        role: "receptionist" | "doctor" | "nurse" | "technician" | "pharmacist"
    }
};

/**
 * A reusable component that renders the system's header
 * @param {props} user A user object
 * @param {string} user.fullName The user's full name
 * @param {"receptionist" | "doctor" | "nurse" | "technician" | "pharmacist"} user.role The user's role
 */
function Header({user}: props) {
    const [greeting, setGreeting] = useState<string>("Welcome")

    // Hook to greet the user according to time of the day
    useEffect(() => {
        const hour = new Date().getHours();

        if (hour < 12) setGreeting("Good Morning");
        else if (hour < 16) setGreeting("Good Afternoon");
        else setGreeting("Good Evening");
    }, []);

    return <header className={"header"}>
        <h1 className={"logo"}>Clinicnet</h1>
        <h2>{user.fullName ? `${greeting} ${user.fullName.split(' ')[0]}` : user.role.toUpperCase()}</h2>
        <div onClick={() => alert("Hello World")} className={"account"}>
            <FaUser/>
        </div>
    </header>
}

export default Header;
