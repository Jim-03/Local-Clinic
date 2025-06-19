import { FaEdit, FaPlus } from "react-icons/fa";
import styles from "./Staff.module.css"
import { MdChevronLeft, MdChevronRight, MdDelete } from "react-icons/md";
import {type JSX, useEffect, useState} from "react";
import validator from "email-validator";
import { parsePhoneNumber } from "libphonenumber-js/min";
import toast from "react-hot-toast";

interface Staff {
    id: number;
    fullName: string;
    email: string;
    phone: string;
    nationalId: string;
    address: string
    dateOfBirth: string;
    gender: string;
    image: string;
    isActive: string;
    role: string
    lastLogin: Date;
    createdAt: Date;
    updatedAt: Date
}

/**
 * Renders a staff view that allows performing CRUD operations on the staff model
 */
function Staff(): JSX.Element {
    const [searchWord, setSearchWord] = useState<string>("")
    const [staffList, setStaffList] = useState<Staff[]>([])
    const [page, setPage] = useState<number>(1)
    const [totalPages, setTotalPages] = useState<number>(1)
    const [sortCriteria, setSortCriteria] = useState<string>("")
    const [filterTerm, setFilterTerm] = useState<string>("")
    const [listType, setListType] = useState<"modified" | "default">("default")
    const [message, setMessage] = useState<string>("No staff members at the moment")
    const apiUrl: string = import.meta.env.VITE_API_URL + "/api/staff"

    useEffect(() => {
        switch (listType) {
            case "default":
                fetchStaff()
                break
            default:
                applySearchFilterAndSort()
                break
        }
    }, [page])

    /**
     * Determines the string type of the search word
     * @returns {"phone" | "email" | "name"} The string type
     */
    function getSearchTerm(): string {
        if (validator.validate(searchWord)) {
            return "email"

        } else {
            try {
                const phone = parsePhoneNumber(searchWord, "KE")

                if (phone.isValid()) {
                    return "phone"
                }

                else {
                    return "name"
                }
            } catch {
                return "name"
            }
        }
    }

    /**
     * Fetches a list of staff members
     * @returns {Promise<void>} A promise that resolves once the staff list is fetched
     */
    async function fetchStaff(): Promise<void> {
        setListType("default")
        try {
            const response = await fetch(`${apiUrl}/page/${page}`)

            const data = await response.json()

            if (!response.ok) {
                toast.error(data.message)
                return
            }

            if (data.staffList.length === 0) setMessage("No staff member found")

            setStaffList(data.staffList)
            setTotalPages(data.totalPages)
        } catch (error) {
            toast.error("Network error!")
            console.error(error)
        }
    }

    /**
     * Fetches a list of staff members based on the applied modifications of search, filter or sort
     * @returns {Promise<void>} A promise that resolves once the list is fetched
     */
    async function applySearchFilterAndSort(): Promise<void> {
        setListType("modified")
        const url = `${apiUrl}/get`
        const params: URLSearchParams = new URLSearchParams()
        params.set("page", page.toString())

        if (searchWord) {
            params.set("identifier", getSearchTerm())
            params.set("value", searchWord)
        }

        if (filterTerm) {
            params.set("filter", filterTerm)
        }

        if (sortCriteria) {
            params.set("sort", sortCriteria)
        }

        try {
            const response: Response = await fetch(`${url}?${params.toString()}`)

            const data = await response.json()

            if (!response.ok) {
                toast.error(data.message)
                return
            }

            if (data.staffList.length === 0) setMessage("No staff member found")
            setStaffList(data.staffList)
            setTotalPages(data.totalPages)
        } catch (error) {
            toast.error("Network error!")
            console.error(error)
        }
    }

    return (
        <div className={styles.background}>
            <h2>Staff</h2>
            <section className={styles.header}>
                <div>
                    <input type="search" placeholder="Email, Phone or Name" onChange={(e) => {
                        setSearchWord(e.target.value.trim())
                    }} />
                    <button className={styles.searchButton} disabled={!searchWord} onClick={applySearchFilterAndSort}>Search</button>
                </div>
                <div>
                    <label htmlFor="filter">Filter By:</label>
                    <select name="filter" value={filterTerm} onChange={(e) => { setFilterTerm(e.target.value) }}>
                        <option value="">Select</option>
                        <option value="NURSE">Nurses</option>
                        <option value="DOCTOR">Doctors</option>
                        <option value="PHARMACIST">Pharmacists</option>
                        <option value="TECHNICIAN">Lab Technicians</option>
                        <option value="RECEPTIONIST">Receptionists</option>
                        <option value="ON_DUTY">On duty</option>
                        <option value="OFF">Off duty</option>
                        <option value="SUSPENDED">Suspended</option>
                    </select>
                </div>
                <div>
                    <label htmlFor="sort">Sort by:</label>
                    <select name="sort" value={sortCriteria} onChange={(e) => setSortCriteria(e.target.value)}>
                        <option value="">Select</option>
                        <option value="ascendingDate">Employment Date Ascending</option>
                        <option value="descendingDate">Employment Date Descending</option>
                        <option value="lastLogin">Last Login</option>
                    </select>
                </div>
            </section>
            <section className={styles.mainSection}>
                <button className={styles.addStaffButton} onClick={() => toast("Add staff not implemented")}><FaPlus /> Add new Staff</button>
                <div className={styles.dataView}>
                    {staffList.length === 0 ?
                        <ul>
                            <li>{message}</li>
                        </ul>
                        :
                        <div className={styles.tableView}>
                            <table className={styles.staffTable}>
                                <thead>
                                    <tr>
                                        <th>Staff ID</th>
                                        <th>Full Name</th>
                                        <th>Email Address</th>
                                        <th>Phone Number</th>
                                        <th>Role</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {staffList.map(staffMember => {
                                        return <tr>
                                            <td>{staffMember.id}</td>
                                            <td>{staffMember.fullName}</td>
                                            <td>{staffMember.email}</td>
                                            <td>{staffMember.phone}</td>
                                            <td>{staffMember.role}</td>
                                            <td><div className={styles.actions}><FaEdit onClick={() => toast("Edit staff not implemented")} /> <MdDelete onClick={() => toast("Delete staff not implemented")} /></div></td>
                                        </tr>
                                    })}
                                </tbody>
                            </table>
                            <div className={styles.pageController}>
                                <button disabled={page === 1} onClick={() => {
                                    setPage(prev => prev - 1)
                                }}><MdChevronLeft /></button>
                                <p>{page}</p>
                                <button disabled={page === totalPages} onClick={() => {
                                    setPage(prev => prev + 1)
                                }}><MdChevronRight /></button>
                            </div>
                        </div>
                    }
                </div>
            </section>
        </div>
    )
}
export default Staff;
