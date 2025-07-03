import {FaClipboardList, FaHome, FaMoneyBill, FaUser} from "react-icons/fa";
import Sidebar from "../../components/Sidebar";
import Header from "../../components/Header";
import {TbReportAnalytics} from "react-icons/tb";
import {type JSX, useEffect, useState} from "react";
import ManagerHome from "../../views/Manager/Home/ManagerHome";
import Staff from "../../views/Manager/Staff/Staff";
import Appointment from "../../views/Manager/Appointment/Appointment.tsx";
import Billing from "../../views/Manager/Billing/Billing.tsx";
import Reports from "../../views/Manager/Report/Report.tsx";

type UserData = {
    userData: { name: string, role: string }
}

/**
 * A component that renders the managers dashboard
 */
function Admin({userData}: UserData): JSX.Element {

    const [view, setView] = useState<JSX.Element>(<ManagerHome/>)

    useEffect(() => {
        document.title = "Manager"
    }, [])

    return (
        <div className="dashboard">
            <Header user={{
                name: userData.name,
                role: userData.role
            }}/>
            <div className="main">
                <Sidebar options={[
                    {
                        name: "Home",
                        action: () => {
                            setView(<ManagerHome/>)
                        },
                        icon: <FaHome/>
                    }, {
                        name: "Staff",
                        action: () => {
                            setView(<Staff/>)
                        },
                        icon: <FaUser/>
                    },
                    {
                        name: "Appointments",
                        action: () => {
                            setView(<Appointment/>)
                        },
                        icon: <FaClipboardList/>
                    }, {
                        name: "Billing",
                        action: () => {
                            setView(<Billing/>)
                        },
                        icon: <FaMoneyBill/>
                    }, {
                        name: "Reports",
                        action: () => {
                            setView(<Reports/>)
                        },
                        icon: <TbReportAnalytics/>
                    }
                ]}/>
                <section className="viewSection">{view}</section>
            </div>
        </div>
    );
}

export default Admin;
