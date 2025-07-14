import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { useState } from 'react';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import usePollingNotifications from "./hooks/usePollingNotifications";
import Login from './pages/Login';
import SignUp from './pages/SignUp';
import Layouts from './components/shelter/layouts/Layouts';
import AdminDashboard from './pages/admin/AdminDashboard';
import DashBoard from './pages/shelter/DashBoard';
import NoticeBoardList from './pages/shelter/NoticeList';
import NoticeWrite from './components/shelter/notice/NoticeWrite';
import NoticeDetail from './components/shelter/notice/NoticeDetail';
import NoticeUpdate from './components/shelter/notice/NoticeUpdate';
import WalkReservationList from './pages/shelter/WalkReservationList';
import WalkReservationDetail from './components/shelter/walk/WalkReservationDetail';
import WalkManager from './components/shelter/walk/WalkManager';
import DogRegister from './components/shelter/dog/DogRegister';
import DogList from './pages/shelter/DogList';
import DogDetail from './components/shelter/dog/DogDetail';
import DogEdit from './components/shelter/dog/DogEdit';
import AdoptionList from './pages/shelter/AdoptionList';
import AdoptionDetail from './components/shelter/adoption/AdoptionDetail';
import PartnerRequestList from './pages/shelter/PartnerRequestList';
import StaticMapTest from './components/shelter/walk/StaticMapTest';
import DonationList from './pages/shelter/DonationList';
import ShelterProfile from './pages/shelter/ShelterProfile';
import NotificationList from './components/shelter/notifications/NotificationList';
import ShelterRegister from './pages/user/ShelterRequest';
import AdminLayout from './components/admin/layouts/AdminLayout';
import UserManager from './pages/admin/UserManager';
import ShelterAdminManager from './pages/admin/ShelterAdminManager';
import ShelterWalksBar from './pages/admin/ShelterWalksBar';
import ReportManager from './pages/admin/Report/ReportManager';
import InquiryManager from './pages/admin/Inquiry/InquiryManager';
import AdminStatistics from './pages/admin/Statistics/AdminStatistics';
import SystemSettings from './pages/admin/SystemSettings';

function App() {
  const [unreadCount, setUnreadCount] = useState(0);
  const shelterId = localStorage.getItem("shelters_id");

  return (
    <>
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
          <Route path="partner-request" element={<PartnerRequestList />}/>
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
      {/* 로그인 상태에서만 알림 폴링 */}
      {shelterId && <PollingNotifications setUnreadCount={setUnreadCount} />}
      <ToastContainer />
    </>
  );
}
function PollingNotifications({ setUnreadCount }) {
  usePollingNotifications(setUnreadCount);
  return null;
}
export default App;
