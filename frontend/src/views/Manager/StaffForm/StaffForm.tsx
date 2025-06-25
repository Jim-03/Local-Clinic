import { useState, type FormEvent, type ChangeEvent } from "react";
import styles from "./StaffForm.module.css"
import toast from "react-hot-toast";
import { validate } from "email-validator";
import { parsePhoneNumber } from "libphonenumber-js/min";

interface Staff {
    id?: number;
    fullName: string;
    email: string;
    phone: string;
    nationalId: string;
    address: string;
    dateOfBirth: Date;
    gender: "MALE" | "FEMALE";
    image?: File;
    username?: string;
    password?: string;
    staffStatus: "ON_DUTY" | "OFF" | "SUSPENDED";
    role: "DOCTOR" | "NURSE" | "PHARMACIST" | "RECEPTIONIST" | "MANAGER";
    createdAt?: Date;
    updatedAt?: Date;
}

interface FormProps {
    staffData: Staff | undefined;
    closeFunction: () => void;
    reloadFunction: () => void;
}

/**
 * Display a staff form that allows adding/updating staff data
 * @param props.closeFunction A function triggered when the close button is clicked
 * @param props.reloadFunction A function that's triggered when a data update occurs
 * @param props.staffData An optional staff data for update
 * @returns A staff form component
 */
function StaffForm({ closeFunction, reloadFunction, staffData }: FormProps) {
    const defaultStaff: Staff = {
        fullName: "",
        email: "",
        phone: "",
        nationalId: "",
        address: "",
        dateOfBirth: new Date("2000-01-01"),
        gender: "MALE",
        username: "",
        password: "password",
        staffStatus: "OFF",
        role: "DOCTOR"
    };

    const [staffForm, setStaffForm] = useState<Staff>(staffData ? staffData : defaultStaff);

    const isUpdate: boolean = staffData != undefined;
    const submitFunction = async function (e: FormEvent): Promise<void> {
        e.preventDefault()
        if (isUpdate) {
            updateStaff()
        } else {
            addStaff()
        }
    }
    const url: string = import.meta.env.VITE_API_URL + "/api/staff";

    /**
     * Updates an existing staff's data in the system
     * @returns {Promise<void>} A promise that resolves once the staff's data is updated
     */
    async function updateStaff(): Promise<void> {
        if (!validateStaffData()) return;

        try {
            const confirmation: string | null = prompt("Enter the user's password")

            if (!confirmation) return

            const response = await fetch(url + `/${staffForm.id}`, {
                method: "PUT",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ updatedData: staffForm, oldPassword: confirmation })
            });

            const data = await response.json();

            console.log(data)
            if (!response.ok) {
                toast.error(data.message);
                return;
            }

            toast.success("Account updated");

            reloadFunction();
            closeFunction();
        } catch (e) {
            toast.error("Network Error");
            console.error(e);
        }
    }

    /**
     * Adds a new staff to the system
     * @returns {Promise<void>} A promise that resolves once the staff's data is added
     */
    async function addStaff(): Promise<void> {
        if (!validateStaffData()) return;

        try {
            const response = await fetch(url, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(staffForm)
            });

            const data = await response.json();
console.error(data)
            if (response.status !== 201) {
                toast.error(data.message);
                return;
            }

            toast.success("Account created");
            reloadFunction();
            closeFunction();
        } catch (e) {
            toast.error("Network Error");
            console.error(e);
        }
    }

    /**
     * Checks if the staff's data is valid
     * @returns {boolean} true if the staff data is valid, false otherwise
     */
    function validateStaffData(): boolean {
        if (!staffForm.fullName) {
            toast.error("Provide the staff's full name!");
            return false;
        };

        if (!staffForm.email) {
            toast.error("Provide the staff's email address!");
            return false;
        } else if (!validate(staffForm.email)) {
            toast.error("Provide a valid email address!");
            return false;
        }

        if (!staffForm.phone) {
            toast.error("Provide the staff's phone number!");
            return false;
        } else {
            try {
                const phone = parsePhoneNumber(staffForm.phone, "KE");
                if (!phone.isValid()) {
                    toast.error("Provide a valid phone number!");
                    return false;
                }
            } catch (e) {
                toast.error("Provide a valid phone number");
                return false;
            }
        }

        if (!staffForm.nationalId) {
            toast.error("Provide a valid national ID number!");
            return false;
        }

        if (!staffForm.dateOfBirth) {
            toast.error("Provide the date of birth!");
            return false;
        }

        if (!staffForm.gender) {
            toast.error("Provide the staff's gender!");
            return false;
        }

        if (!staffForm.staffStatus) {
            toast.error("Provide the working status of the staff member!");
            return false;
        }

        if (!staffForm.role) {
            toast.error("Provide the role of the staff member!")
            return false
        }

        if (!staffForm.address) {
            toast.error("Provide the staff's address!");
            return false;
        }

        return true;
    }

    function changeForm(e: ChangeEvent<HTMLInputElement | HTMLSelectElement>) {
        const { name, value } = e.target;

        if (name === "dateOfBirth") {
            setStaffForm({ ...staffForm, [name]: new Date(value) });
        } else {
            setStaffForm({ ...staffForm, [name]: value });
        }
    }



    return <div className={styles.formBackground}>
        <form onSubmit={submitFunction} className={styles.staffForm}>
            <div className={styles.closingDiv}>
                <p onClick={closeFunction}>&times;</p>
            </div>
            <h1>{isUpdate ? staffData?.fullName : "Add New Staff"}</h1>
            <div className={styles.staffDetails}>
                <div className={styles.basicDetails}>
                    <div>
                        <label htmlFor="fullName">Full Name:</label>
                        <input type="text" name="fullName" value={staffForm.fullName} onChange={changeForm} />
                    </div>
                    <div>
                        <label htmlFor="email">Email Address:</label>
                        <input type="email" name="email" value={staffForm.email} onChange={changeForm} />
                    </div>
                    <div>
                        <label htmlFor="phone">Phone Number:</label>
                        <input type="tel" name="phone" value={staffForm.phone} onChange={changeForm} />
                    </div>
                    <div>
                        <label htmlFor="nationalId">National ID number:</label>
                        <input type="text" name="nationalId" value={staffForm.nationalId} onChange={changeForm} />
                    </div>
                    <div>
                        <label htmlFor="dateOfBirth">Date Of Birth:</label>
                        <input
                            type="date"
                            name="dateOfBirth"
                            value={new Date(staffForm.dateOfBirth).toISOString().split("T")[0]}
                            onChange={changeForm}
                        />
                    </div>
                    <div>
                        <label htmlFor="address">Address:</label>
                        <input type="text" name="address" value={staffForm.address} onChange={changeForm} />
                    </div>
                </div>
                <div className={styles.secondaryDetails}>
                    {!isUpdate && (
                        <>
                            <div>
                                <label htmlFor="username">Username:</label>
                                <input type="text" name="username" value={staffForm.username} onChange={changeForm} />
                            </div>
                            <div>
                                <label htmlFor="password">Password:</label>
                                <input type="password" name="password" value={staffForm.password} onChange={changeForm} />
                            </div>
                        </>
                    )}
                    <div>
                        <label htmlFor="gender">Gender</label>
                        <select name="gender" value={staffForm.gender} onChange={changeForm}>
                            <option value="MALE">Male</option>
                            <option value="FEMALE">Female</option>
                        </select>
                    </div>
                    <div>
                        <label htmlFor="staffStatus">Status</label>
                        <select name="staffStatus" value={staffForm.staffStatus} onChange={changeForm}>
                            <option value="ON_DUTY">On duty</option>
                            <option value="OFF">Off Duty</option>
                            <option value="SUSPENDED">Suspended</option>
                        </select>
                    </div>
                    <div>
                        <label htmlFor="role">Role:</label>
                        <select name="role" value={staffForm.role} onChange={changeForm}>
                            <option value="DOCTOR">Doctor</option>
                            <option value="NURSE">Nurse</option>
                            <option value="PHARMACIST">Pharmacist</option>
                            <option value="RECEPTIONIST">Receptionist</option>
                            <option value="MANAGER">Manager</option>
                        </select>
                    </div>
                    {isUpdate && (
                        <div>
                            <label htmlFor="dateJoined">Date Joined:</label>
                            <input
                                type="text"
                                disabled
                                value={String(staffForm.createdAt).split("T")[0]}
                            />
                        </div>
                    )}
                </div>
            </div>
            <div className={styles.controls}>
                <button type="submit" className={isUpdate ? styles.updateButton : styles.addButton}>
                    {isUpdate ? "Update" : "Add"}
                </button>
                <button type="button" onClick={closeFunction} className={styles.cancelButton}>
                    Cancel
                </button>
            </div>
        </form>
    </div>
}

export default StaffForm;