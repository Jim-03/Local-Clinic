import { Toaster } from "react-hot-toast"
import { BrowserRouter, Route, Routes } from "react-router-dom"
import Login from "./pages/Login/Login"

function App() {
    return (<>
        <Toaster position={"top-right"} reverseOrder={true} />
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login />} />
            </Routes>
        </BrowserRouter>
    </>)
}

export default App