import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import SignUp from './pages/SignUp';
import AdminHome from './pages/admin/AdminHome';
import Layouts from './components/shelter/layouts/Layouts';
import NoticeBoardList from './pages/shelter/NoticeList';
import DashBoard from './pages/shelter/DashBoard';
import WalkReservation from './pages/shelter/WalkReservation';
import DogRegister from './components/shelter/dog/DogRegister';
import AdoptionManager from './pages/shelter/AdoptionManager';
import DonationManager from './pages/shelter/DonationManager';
import MyPage from './pages/shelter/MyPage';
import DogList from './pages/shelter/DogList';
import NoticeWrite from './components/shelter/notice/NoticeWrite';
import NoticeDetail from './components/shelter/notice/NoticeDetail';
import NoticeUpdate from './components/shelter/notice/NoticeUpdate';
import ShelterRegister from './pages/user/ShelterRequest';
import DogDetail from './components/shelter/dog/DogDetail';
import DogEdit from './components/shelter/dog/DogEdit';
import WalkList from './pages/shelter/WalkList';
import WalkRegister from './components/shelter/walk/WalkRegister';

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
          <Route path="walkreservation" element={<WalkReservation />} />
          <Route path="dogregister" element={<DogRegister />} />
          <Route path="doglist" element={<DogList sheltersId={localStorage.getItem('shelters_id')} />} />
          <Route path="dog/:id" element={<DogDetail />} />
          <Route path="/shelter/dogedit/:id" element={<DogEdit />} />


          <Route path="adoptionmanager" element={<AdoptionManager />} />
          <Route path="walklist" element={<WalkList />} />
          <Route path="walkregister" element={<WalkRegister />} />
          <Route path="donationmanager" element={<DonationManager />} />
          <Route path="mypage" element={<MyPage />} />
        </Route>

        <Route path="shelter-request" element={<ShelterRegister />}/>

        <Route path="admin/home" element={<AdminHome />}/>
      </Routes>
    </Router>
  );
}

export default App;
