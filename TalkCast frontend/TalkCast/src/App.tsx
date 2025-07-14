import { BrowserRouter, Routes, Route } from 'react-router-dom';
import './App.css';
import GoogleLogin from './components/GoogleLogin';
import BroadcastForm from './components/BroadCastForm'; // Don't forget this import

function App() {
  return (
    <BrowserRouter>
      <div style={{ padding: "1rem" }}>
        <h1>YouTube Stream App</h1>
        <Routes>
          <Route path="/" element={<GoogleLogin />} />
          <Route path="/broadcast" element={<BroadcastForm />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
