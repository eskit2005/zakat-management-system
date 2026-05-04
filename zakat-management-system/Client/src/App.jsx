import { useEffect, useState } from "react";
import { Routes, Route } from "react-router-dom";
import { AuthContext } from "./auth/authContext";
import Navbar from "./components/Navbar/Navbar";
import Homepage from "./pages/Homepage/Homepage";
import Login from "./pages/Login/Login";
import Register from "./pages/Register/Register";
import DonationPage from "./pages/Donation/DonationPage";
import BeneficiaryForm from "./pages/Beneficiary/BeneficiaryForm";
import AdminDashboard from "./pages/Admin/AdminDashboard";
import DirectDonation from "./pages/DirectDonation/DirectDonation";
import ItemDonation from "./pages/ItemDonation/ItemDonation";
import Profile from "./pages/Profile/Profile";
import { persistReload } from "./domain/authService";

function App() {
  const [accessToken, setAccessToken] = useState(null);
  const [user, setUser] = useState(null);

  useEffect(() => {
    const reload = async () => {
      try {
        await persistReload(setAccessToken, setUser);
      } catch (error) {
        console.log("User not authenticated");
      }
    };
    reload();
  }, []);

  return (
    <AuthContext.Provider value={{ accessToken, setAccessToken, user, setUser }}>
      <Navbar />
      <main className="main-content">
        <Routes>
          <Route path="/" element={<Homepage />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/donate" element={<DonationPage />} />
          <Route path="/direct-donate" element={<DirectDonation />} />
          <Route path="/donate-item" element={<ItemDonation />} />
          <Route path="/beneficiary-registration" element={<BeneficiaryForm />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/admin" element={<AdminDashboard />} />
        </Routes>
      </main>
      <footer className="footer">
        <div className="container">
          <p>&copy; 2024 Zakat Management System. All rights reserved.</p>
        </div>
      </footer>
    </AuthContext.Provider>
  );
}

export default App;
