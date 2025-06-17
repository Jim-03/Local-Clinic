import type { JSX } from "react"
import { GiHamburgerMenu } from "react-icons/gi"

type User = {
    name?: string;
    role?: string
}

type HeaderProp = {
    user: User
}

function Header({ user }: HeaderProp): JSX.Element {
    function greet(): string {
        const hour: number = new Date().getHours()

        if (hour < 12) {
            return "Good Morning"
        } else if (hour < 16) {
            return "Good Afternoon"
        } else {
            return "Good evening"
        }
    }

    // TODO: Create options menu
    return (
        <div className="header">
            <h1>Clinicnet</h1>
            <h2>{greet()} {user.name?.split(" ")[0] || user.role?.toLowerCase()} </h2>
            <GiHamburgerMenu className="options" />
        </div>
    )
}

export default Header