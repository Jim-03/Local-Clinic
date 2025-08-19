interface Log {
    id: number;
    action: string;
    time: string;
}

interface Patient {
    id?: number;
    fullName: string;
    email?: string;
    phone: string;
    nationalId: string;
    address: string;
    dateOfBirth: string;
    gender: "MALE" | "FEMALE";
    emergencyContact: string;
    emergencyName: string;
    insuranceProvider?: string;
    insuranceNumber: string;
    bloodType: "A+" | "A-" | "B+" | "B-" | "AB+" | "AB-" | "O+" | "O-"
}

export type {Log, Patient}