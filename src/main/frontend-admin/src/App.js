import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import SignUp from './pages/SignUp';
import AdminDashboard from './pages/admin/AdminDashboard';
import Layouts from './components/shelter/layouts/Layouts';
import NoticeBoardList from './pages/shelter/NoticeList';
import DashBoard from './pages/shelter/DashBoard';
import WalkReservationList from './pages/shelter/WalkReservationList';
import DogRegister from './components/shelter/dog/DogRegister';
import AdoptionList from './pages/shelter/AdoptionList';
import AdoptionDetail from './components/shelter/adoption/AdoptionDetail';
import DonationList from './pages/shelter/DonationList';
import ShelterProfile from './pages/shelter/ShelterProfile';
import DogList from './pages/shelter/DogList';
import NoticeWrite from './components/shelter/notice/NoticeWrite';
import NoticeDetail from './components/shelter/notice/NoticeDetail';
import NoticeUpdate from './components/shelter/notice/NoticeUpdate';
import ShelterRegister from './pages/user/ShelterRequest';
import DogDetail from './components/shelter/dog/DogDetail';
import DogEdit from './components/shelter/dog/DogEdit';
import WalkReservationDetail from './components/shelter/walk/WalkReservationDetail';
import WalkManager from './components/shelter/walk/WalkManager';
import NotificationList from './components/shelter/notifications/NotificationList';
import AdminLayout from './components/admin/layouts/AdminLayout';
import UserManager from './pages/admin/UserManager';
import ShelterAdminManager from './pages/admin/ShelterAdminManager';
import ShelterWalksBar from './pages/admin/ShelterWalksBar';
import StaticMapTest from './components/shelter/walk/StaticMapTest';
import ReportManager from './pages/admin/Report/ReportManager';
import InquiryManager from './pages/admin/Inquiry/InquiryManager';
import AdminStatistics from './pages/admin/Statistics/AdminStatistics';
import SystemSettings from './pages/admin/SystemSettings';

function App() {
  return (
    <Router>
      <Routes>
        {/* 통합 로그인폼  */}
        <Route path="" element={<Login />}/>
        <Route path="signup" element={<SignUp />}/>

                      {/* 보호소관리자 */}
        <Route path="shelter" element={<Layouts />}>
          <Route path="dashboard" element={<DashBoard />} />
          <Route path="noticelist" element={<NoticeBoardList />} />
          <Route path="noticewrite" element={<NoticeWrite />} />
          <Route path="notice/detail/:id" element={<NoticeDetail />} />
          <Route path="notice/update/:id" element={<NoticeUpdate />} />
          <Route path="walkreservationlist" element={<WalkReservationList />} />
          <Route path="walk-reservations/:id" element={<WalkReservationDetail />} />
          <Route
            path="walkmanager"
            element={<WalkManager sheltersId={localStorage.getItem("shelters_id")} />}
          />
          <Route path="dogregister" element={<DogRegister />} />
          <Route path="doglist" element={<DogList sheltersId={localStorage.getItem('shelters_id')} />} />
          <Route path="dog/:id" element={<DogDetail />} />
          <Route path="/shelter/dogedit/:id" element={<DogEdit />} />
          <Route path="adoptionlist" element={<AdoptionList />} />
          <Route path="/shelter/adoptions/:id" element={<AdoptionDetail />} />
          <Route
            path='walk-reservations/:id'
            element={<WalkReservationDetail />}
          />
        <Route path="test" element={<StaticMapTest />} />
          <Route path="donationlist" element={<DonationList />} />
          <Route path="profile" element={<ShelterProfile />} />
            <Route path="notifications" element={<NotificationList />} />
        </Route>
        <Route path="shelter-request" element={<ShelterRegister />}/>

                          {/* 시스템관리자 */}
          <Route path="admin" element={<AdminLayout />}>
        <Route path="dashboard" element={<AdminDashboard />}/>
        <Route path="users" element={<UserManager />} />
        <Route path="report" element={<ReportManager />} />
        <Route path="inquiry" element={<InquiryManager />} />
        <Route path="shelter" element={<ShelterAdminManager />} />
        <Route path="statistics" element={<AdminStatistics />} />
        <Route path="reservation" element={<ShelterWalksBar />} />
        <Route path="settings" element={<SystemSettings />} />
        </Route>

      </Routes>
    </Router>
  );
}

export default App;
