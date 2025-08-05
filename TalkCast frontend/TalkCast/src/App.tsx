import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css';
import BroadcastForm from './pages/BroadCastForm'; // Don't forget this import
import AuthPage from './pages/AuthPage';
import Dashboard from './pages/Dashboard';
import VideoUploadForm from './pages/VideoUploadForm';
import YouTubeShortUpload from './pages/YoutubeShortUpload';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<AuthPage />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/broadcast" element={<BroadcastForm />} />
        <Route path="/uploadVideo" element={<VideoUploadForm />} />
        <Route path="/uploadShort" element={<YouTubeShortUpload />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
