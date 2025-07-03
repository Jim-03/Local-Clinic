import { Toaster } from "react-hot-toast"
import { BrowserRouter, Route, Routes } from "react-router-dom"
import {ProtectedRoute} from "./components/ProtectedRoute.tsx";
import Login from "./pages/Login/Login.tsx";

function App() {
    return (<>
        <Toaster position={"top-center"} reverseOrder={true} />
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login />} />
                <Route path="/dashboard" element={<ProtectedRoute/>}/>
            </Routes>
        </BrowserRouter>
    </>)
}

export default App