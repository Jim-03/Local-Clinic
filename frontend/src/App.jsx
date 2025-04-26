import { BrowserRouter, Route, Routes } from "react-router-dom";
import Login from "./Pages/Login/Login";
import ReceptionistDashboard from "./Pages/Dashboard/Receptionist";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/receptionist" element={<ReceptionistDashboard/>} />
      </Routes>
    </BrowserRouter>
  );
}
export default App;
