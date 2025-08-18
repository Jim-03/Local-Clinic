import * as React from "react";
import {useState} from "react";

type button = {
    buttonName: string;
    buttonFunction: () => void;
    buttonIcon: React.ReactNode
};

type props = {
    buttons: button[]
};

/**
 * A reusable component that renders the sidebar
 * @param {props} buttons A list of objects that build the sidebar's buttons
 * @param {string} buttons.buttonName The name of the button
 * @param {function} buttons.buttonFunction The function triggered when the button is clicked
 * @param {React.ReactNode} buttons.buttonIcon The button's icon
 */
function Sidebar({buttons}: props) {
    const [active, setActive] = useState("Dashboard");

    return <nav className={"sidebar"}>
        {buttons.map(button => <button onClick={() => {
            button.buttonFunction();
            setActive(button.buttonName)
        }}
                                       className={`button ${button.buttonName == active ? "active" : ""}`}>{button.buttonIcon} {button.buttonName}</button>)}
    </nav>
}

export default Sidebar;
