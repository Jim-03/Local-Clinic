import { useState, type ReactNode } from "react";

type SidebarOption = {
    name: string;
    icon: ReactNode;
    action: () => void;
};

type SidebarProps = {
    options: SidebarOption[];
};

/**
 * Renders a Sidebar on the UI
 * @param {SidebarProps} options A list of objects that form the buttons on the sidebar
 * @returns The Sidebar React component
 */
function Sidebar({ options }: SidebarProps) {

    const [active, setActive] = useState<string>(options[0]?.name || "");

    return (
        <nav className="sidebar">
            {options.map((option) => (
                <button
                    key={option.name}
                    onClick={() => {
                        setActive(option.name);
                        option.action();
                    }}
                    className={active === option.name ? "active" : ""}
                >
                    {option.icon} {option.name}
                </button>
            ))}
        </nav>
    )
}

export default Sidebar;
