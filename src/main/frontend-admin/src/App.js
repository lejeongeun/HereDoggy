import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import SignUp from './pages/SignUp';
import Admin_Home from './pages/admin/Admin_Home';
import Layouts from './components/shelter/layouts/Layouts';
import NoticeBoardList from './pages/shelter/NoticeBoardList';
import DashBoard from './pages/shelter/DashBoard';
import WalkReservationManager from './pages/shelter/WalkReservationManager';
import DogRegister from './pages/shelter/DogRegister';
import AdoptionManager from './pages/shelter/AdoptionManager';
import RouteManager from './pages/shelter/RouteManager';
import DonationManager from './pages/shelter/DonationManager';
import MyPage from './pages/shelter/MyPage';
import ShelterRegister from './pages/user/ShelterRegister';
import DogList from './pages/shelter/DogList';



function App() {
  return (
    <Router>
      <Routes>
        <Route path="" element={<Login />}/>
        <Route path="signup" element={<SignUp />}/>

        <Route path="shelter" element={<Layouts />}>
          <Route path="dashboard" element={<DashBoard />} />
          <Route path="noticeboardlist" element={<NoticeBoardList />} />
          <Route path="walkreservationmanager" element={<WalkReservationManager />} />
          <Route path="dogregister" element={<DogRegister />} />
          <Route path="doglist" element={<DogList sheltersId={localStorage.getItem('shelters_id')} />} />
          <Route path="adoptionmanager" element={<AdoptionManager />} />
          <Route path="routemanager" element={<RouteManager />} />
          <Route path="donationmanager" element={<DonationManager />} />
          <Route path="mypage" element={<MyPage />} />
        </Route>

        <Route path="/shelter/register" element={<ShelterRegister />}/>

        <Route path="/admin/home" element={<Admin_Home />}/>
      </Routes>
    </Router>
  );
}

export default App;
