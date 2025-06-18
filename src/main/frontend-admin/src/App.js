import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import SignUp from './pages/SignUp';
import AdminHome from './pages/admin/AdminHome';
import Layouts from './components/shelter/layouts/Layouts';
import NoticeBoardList from './pages/shelter/NoticeList';
import DashBoard from './pages/shelter/DashBoard';
import WalkReservationList from './pages/shelter/WalkReservationList';
import DogRegister from './components/shelter/dog/DogRegister';
import AdoptionList from './pages/shelter/AdoptionList';
import AdoptionDetail from './components/shelter/adoption/AdoptionDetail';
import DonationList from './pages/shelter/DonationList';
import MyPage from './pages/shelter/MyPage';
import DogList from './pages/shelter/DogList';
import NoticeWrite from './components/shelter/notice/NoticeWrite';
import NoticeDetail from './components/shelter/notice/NoticeDetail';
import NoticeUpdate from './components/shelter/notice/NoticeUpdate';
import ShelterRegister from './pages/user/ShelterRequest';
import DogDetail from './components/shelter/dog/DogDetail';
import DogEdit from './components/shelter/dog/DogEdit';
// import WalkList from './pages/shelter/WalkList';
// import WalkRegister from './components/shelter/walk/WalkRegister';
import WalkReservationDetail from './components/shelter/walk/WalkReservationDetail';
import WalkManager from './components/shelter/walk/WalkManager';
import NotificationList from './components/shelter/notifications/NotificationList';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="" element={<Login />}/>
        <Route path="signup" element={<SignUp />}/>

        <Route path="shelter" element={<Layouts />}>
          <Route path="dashboard" element={<DashBoard />} />
          <Route path="noticelist" element={<NoticeBoardList />} />
          <Route path="noticewrite" element={<NoticeWrite />} />
          <Route path="notice/detail/:id" element={<NoticeDetail />} />
          <Route path="notice/update/:id" element={<NoticeUpdate />} />
          <Route path="walkreservationlist" element={<WalkReservationList />} />
          <Route path="walk-reservations/:id" element={<WalkReservationDetail />} />

          <Route path="dogregister" element={<DogRegister />} />
          <Route path="doglist" element={<DogList sheltersId={localStorage.getItem('shelters_id')} />} />
          <Route path="dog/:id" element={<DogDetail />} />
          <Route path="/shelter/dogedit/:id" element={<DogEdit />} />

          <Route path="adoptionlist" element={<AdoptionList />} />
          <Route path="/shelter/adoptions/:id" element={<AdoptionDetail />} />
          <Route
            path="walkmanager"
            element={<WalkManager sheltersId={localStorage.getItem('shelters_id')} />}
          />

          <Route path="donationlist" element={<DonationList />} />
          <Route path="mypage" element={<MyPage />} />
            <Route path="notifications" element={<NotificationList />} />
        </Route>

        <Route path="shelter-request" element={<ShelterRegister />}/>

        <Route path="admin/home" element={<AdminHome />}/>
      </Routes>
    </Router>
  );
}

export default App;
