import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import SignUp from './pages/SignUp';
import User_Home from './pages/user/User_Home';
import Admin_Home from './pages/admin/Admin_Home';
import Layouts from './components/shelter/layouts/Layouts';
import NoticeBoardList from './pages/shelter/NoticeBoardList';
import DashBoard from './pages/shelter/DashBoard';
import WalkReservationManager from './pages/shelter/WalkReservationManager';
import AnimalManager from './pages/shelter/AnimalManager';
import AdoptionManager from './pages/shelter/AdoptionManager';
import RouteManager from './pages/shelter/RouteManager';
import DonationManager from './pages/shelter/DonationManager';
import MyPage from './pages/shelter/MyPage';


function App() {
  return (
    <Router>
      <Routes>
        <Route path="" element={<Login />}/>
        <Route path="/signup" element={<SignUp />}/>

        <Route path="/shelter" element={<Layouts />}>
          <Route path="dashboard" element={<DashBoard />} />
          <Route path="noticeboardlist" element={<NoticeBoardList />} />
          <Route path="walkreservationmanager" element={<WalkReservationManager />} />
          <Route path="animalmanager" element={<AnimalManager />} />
          <Route path="adoptionmanager" element={<AdoptionManager />} />
          <Route path="routemanager" element={<RouteManager />} />
          <Route path="donationmanager" element={<DonationManager />} />
          <Route path="mypage" element={<MyPage />} />
        </Route>

        <Route path="/user/home" element={<User_Home />}/>

        <Route path="/admin/home" element={<Admin_Home />}/>
      </Routes>
    </Router>
  );
}

export default App;
