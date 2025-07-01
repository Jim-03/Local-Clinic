import {type JSX, useEffect, useState} from "react";
import styles from "./Billing.module.css"
import {FaFilter, FaMoneyBill} from "react-icons/fa";
import toast from "react-hot-toast";
import {MdChevronLeft, MdChevronRight} from "react-icons/md";

interface BillingData {
    id: number;
    patient: { id: number; phone: string, fullName: string };
    bills: Record<string, number>;
    totalAmount: number;
    paymentMethod: string;
    amountPaid: number;
    status: "PENDING" | "CANCELLED" | "PARTIALLY_PAID" | "PAID";
    createdAt: string;
    updatedAt: string;
}

/**
 * A view component to show bills
 */
function Billing(): JSX.Element {
    const [startDate, setStartDate] = useState<Date>(new Date())
    const [endDate, setEndDate] = useState<Date>(new Date())
    const [page, setPage] = useState<number>(1)
    const [totalPages, setTotalPages] = useState<number>(1)
    const [period, setPeriod] = useState<"TODAY" | "WEEK" | "MONTH" | "YEAR" | "CUSTOM">("TODAY")
    const [showCustomDatePicker, setShowCustomDatePicker] = useState<boolean>(false) // Fixed: should be false initially
    const [loading, setLoading] = useState<boolean>(false)
    const [billList, setBillList] = useState<BillingData[]>([])
    const [filterTerm, setFilterTerm] = useState<"PAID" | "PENDING" | "PARTIALLY_PAID" | "CANCELLED" | "">("")
    const [filteredList, setFilteredList] = useState<BillingData[]>([])
    const [expandedBills, setExpandedBills] = useState<Set<number>>(new Set())
    const api = `${import.meta.env.VITE_API_URL}/api/billing`

    // Fetch the list of billings
    useEffect(() => {
        fetchBillings()
    }, [startDate, endDate, page]);

    // Set date periods
    useEffect(() => {
        const end: Date = new Date();
        let newStartDate: Date = new Date();

        switch (period) {
            case "TODAY":
                newStartDate = new Date(end.getFullYear(), end.getMonth(), end.getDate(), 0, 0, 0);
                break;
            case "WEEK":
                newStartDate = new Date(end.getFullYear(), end.getMonth(), end.getDate() - end.getDay(), 0, 0, 0);
                break;
            case "MONTH":
                newStartDate = new Date(end.getFullYear(), end.getMonth(), 1, 0, 0, 0);
                break;
            case "YEAR":
                newStartDate = new Date(end.getFullYear(), 0, 1, 0, 0, 0);
                break;
            case "CUSTOM":
                setShowCustomDatePicker(true);
                return;
        }

        setShowCustomDatePicker(false);
        setStartDate(newStartDate);
        setEndDate(new Date(end.getFullYear(), end.getMonth(), end.getDate(), 23, 59, 59));
    }, [period])

    // Filters the billing list
    useEffect(() => {
        const filteredList: BillingData[] = filterTerm === ""
            ? billList
            : billList.filter(bill => bill.status === filterTerm)
        setFilteredList(filteredList);
    }, [filterTerm, billList]);

    /**
     * Fetches a list of billings within the specified date period
     */
    function fetchBillings() {
        setLoading(true)
        const params: URLSearchParams = new URLSearchParams({
            start: startDate.toISOString().replace("Z", ""),
            end: endDate.toISOString().replace("Z", ""),
            page: page.toString()
        })

        fetch(`${api}?${params.toString()}`)
            .then(async response => {
                if (!response.ok) {
                    const error = await response.json()
                    throw new Error(error)
                }
                return response.json()
            })
            .then(data => {
                setTotalPages(data.totalPages)
                setBillList(data.bills)
            })
            .catch(e => {
                toast.error("Failed to fetch billings!")
                console.error(e)
            })
            .finally(() => setLoading(false))
    }

    const toggleBillExpansion = (billId: number) => {
        const newExpanded = new Set(expandedBills);
        if (newExpanded.has(billId)) {
            newExpanded.delete(billId);
        } else {
            newExpanded.add(billId);
        }
        setExpandedBills(newExpanded);
    };

    const formatCurrency = (amount: number) => {
        return new Intl.NumberFormat('en-KE', {
            style: 'currency',
            currency: 'KES'
        }).format(amount);
    };

    const getStatusClass = (status: string) => {
        switch (status) {
            case 'PAID':
                return styles.statusPaid;
            case 'PENDING':
                return styles.statusPending;
            case 'PARTIALLY_PAID':
                return styles.statusPartiallyPaid;
            case 'CANCELLED':
                return styles.statusCancelled;
            default:
                return '';
        }
    };

    return <div className={styles.background}>
        <section className={styles.header}>
            <h1><FaMoneyBill/> Billing</h1>
            <div className={styles.filterControls}>
                <div className={styles.filterBox}>
                    <label><FaFilter/></label>
                    <select value={filterTerm} onChange={e => {
                        setFilterTerm(e.target.value as "PAID" | "PENDING" | "PARTIALLY_PAID" | "CANCELLED" | "")
                    }}>
                        <option value="">All Status</option>
                        <option value="PENDING">Pending</option>
                        <option value="PARTIALLY_PAID">Partially Paid</option>
                        <option value="PAID">Paid</option>
                        <option value="CANCELLED">Cancelled</option>
                    </select>
                </div>
            </div>
        </section>

        <section className={styles.dateControls}>
            <label>Select Date</label>
            <select value={period} onChange={e => {
                setPeriod(e.target.value as "TODAY" | "WEEK" | "MONTH" | "YEAR" | "CUSTOM")
            }}>
                <option value="TODAY">Today</option>
                <option value="WEEK">This Week</option>
                <option value="MONTH">This Month</option>
                <option value="YEAR">This Year</option>
                <option value="CUSTOM">Custom</option>
            </select>
            {showCustomDatePicker && <>
                <label>Start:</label>
                <input
                    type="date"
                    onChange={(e) => {
                        const start = new Date(e.target.value);
                        start.setHours(0, 0, 0, 0);
                        setStartDate(start);
                    }}
                    max={endDate.toISOString().split('T')[0]}
                />
                <label>End:</label>
                <input
                    type="date"
                    onChange={(e) => {
                        const end = new Date(e.target.value);
                        end.setHours(23, 59, 59, 999);
                        setEndDate(end);
                    }}
                    min={startDate.toISOString().split('T')[0]}
                />
            </>}
        </section>

        <section className={styles.mainSection}>
            {loading && <div className={styles.loading}>
                <p>Loading...</p>
            </div>}
            {!loading && filteredList.length === 0 ? (
                <div className={styles.noBills}>
                    <p>{filterTerm === ""
                        ? "There are no bills in the specified period"
                        : "No bills found matching your criteria"}</p>
                </div>
            ) : (
                <div className={styles.tableView}>
                    <div className={styles.tableContainer}>
                        <table className={styles.billingTable}>
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Patient</th>
                                <th>Bills</th>
                                <th>Total Amount</th>
                                <th>Amount Paid</th>
                                <th>Status</th>
                                <th>Date</th>
                                <th>Time</th>
                            </tr>
                            </thead>
                            <tbody>
                            {filteredList.map(bill => {
                                const createdDate = new Date(bill.createdAt);
                                const isExpanded = expandedBills.has(bill.id);

                                return (
                                    <tr key={bill.id}>
                                        <td className={styles.idCell}>{bill.id}</td>
                                        <td className={styles.patientCell}>
                                            <div className={styles.patientInfo}>
                                                <p className={styles.patientName}>{bill.patient.fullName}</p>
                                                <p className={styles.patientPhone}>{bill.patient.phone}</p>
                                            </div>
                                        </td>
                                        <td className={styles.billsCell}>
                                            <button
                                                className={styles.billsToggle}
                                                onClick={() => toggleBillExpansion(bill.id)}
                                            >
                                                {Object.keys(bill.bills).length} item(s) {isExpanded ? '▼' : '▶'}
                                            </button>
                                            {isExpanded && (
                                                <div className={styles.billsExpanded}>
                                                    {Object.entries(bill.bills).map(([billName, amount], index) => (
                                                        <div key={index} className={styles.billItem}>
                                                                <span className={styles.billName}>
                                                                    {billName.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase())}
                                                                </span>
                                                            <span className={styles.billAmount}>
                                                                    {formatCurrency(amount)}
                                                                </span>
                                                        </div>
                                                    ))}
                                                </div>
                                            )}
                                        </td>
                                        <td className={styles.amountCell}>{formatCurrency(bill.totalAmount)}</td>
                                        <td className={styles.amountCell}>{formatCurrency(bill.amountPaid)}</td>
                                        <td className={styles.statusCell}>
                                                <span
                                                    className={`${styles.statusBadge} ${getStatusClass(bill.status)}`}>
                                                    {bill.status.replace('_', ' ').toLowerCase()}
                                                </span>
                                        </td>
                                        <td className={styles.dateCell}>{createdDate.toLocaleDateString()}</td>
                                        <td className={styles.timeCell}>{createdDate.toLocaleTimeString()}</td>
                                    </tr>
                                );
                            })}
                            </tbody>
                        </table>
                    </div>
                    <div className={styles.pageControls}>
                        <button
                            onClick={() => setPage(prev => Math.max(1, prev - 1))}
                            disabled={page === 1}
                            className={styles.pageButton}
                        >
                            <MdChevronLeft/>
                        </button>
                        <p className={styles.pageInfo}>Page {page} of {totalPages}</p>
                        <button
                            onClick={() => setPage(prev => Math.min(totalPages, prev + 1))}
                            disabled={page === totalPages}
                            className={styles.pageButton}
                        >
                            <MdChevronRight/>
                        </button>
                    </div>
                </div>
            )}
        </section>
    </div>
}

export default Billing