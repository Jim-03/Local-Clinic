import { BrowserRouter, Route, Routes } from 'react-router';
import { type JSX } from 'react';
import Login from './pages/login/Login.tsx';
import ProtectedRoute from './ProtectedRoute.tsx';

function App(): JSX.Element {
  return <BrowserRouter>
    <Routes>
      <Route path={"/"} element={<ProtectedRoute/>}/>
      <Route path={"/login"} element={<Login/>}/>
    </Routes>
  </BrowserRouter>;

}

export default App;