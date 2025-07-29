import React, { useEffect, useState } from "react";
import axios from "axios";

const CLIENT_ID = "494131558045-mt4avl1rfuf59oj5r73b2fihrtokfc0m.apps.googleusercontent.com";
const REDIRECT_URI = "http://localhost:5173/";
const SCOPE = "https://www.googleapis.com/auth/youtube https://www.googleapis.com/auth/userinfo.email";

const AUTH_URL = `https://accounts.google.com/o/oauth2/v2/auth?client_id=${CLIENT_ID}` +
  `&redirect_uri=${encodeURIComponent(REDIRECT_URI)}` +
  `&response_type=code` +
  `&scope=${encodeURIComponent(SCOPE)}` +
  `&access_type=offline` +
  `&prompt=consent`;

const GoogleOAuthLogin: React.FC = () => {
  const [broadcastData, setBroadcastData] = useState({
    title: "",
    description: "",
    startTime: "",
    endTime: "",
    enableEmbed: true,
    enableAutoStart: false,
    recordFromStart: false,
    privacyStatus: "PRIVATE", // or 'public', 'unlisted'
  });

  useEffect(() => {
    const params = new URLSearchParams(window.location.search);
    const code = params.get("code");

    if (code) {
      window.history.replaceState({}, document.title, window.location.pathname);

      (async () => {
        try {
          const payload = {
            authCode: code,
            ...broadcastData,
          };

          const response = await axios.post("http://localhost:8080/api/youtube/broadcast", payload);
          console.log("Backend response:", response);

          if (response.data.accessToken) {
            localStorage.setItem("accessToken", response.data.accessToken);
          }

          window.location.href = "/broadcast";
        } catch (error) {
          console.error("Backend error:", error);
        }
      })();
    }
  }, [broadcastData]);

  const handleInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>
  ) => {
    const { name, type, value } = e.target;

    const newValue =
      type === "checkbox"
        ? (e.target as HTMLInputElement).checked
        : value;

    setBroadcastData((prev) => ({
      ...prev,
      [name]: newValue,
    }));
  };


  const handleLogin = () => {
    window.location.href = AUTH_URL;
  };

  return (
    <div style={{ padding: "2rem" }}>
      <h2>YouTube Live Broadcast Setup</h2>
      <div style={{ maxWidth: "400px", marginBottom: "1rem" }}>
        <input
          type="text"
          name="title"
          placeholder="Title"
          value={broadcastData.title}
          onChange={handleInputChange}
          style={{ width: "100%", marginBottom: "8px", padding: "8px" }}
        />
        <textarea
          name="description"
          placeholder="Description"
          value={broadcastData.description}
          onChange={handleInputChange}
          style={{ width: "100%", marginBottom: "8px", padding: "8px" }}
        />
        <input
          type="datetime-local"
          name="startTime"
          value={broadcastData.startTime}
          onChange={handleInputChange}
          style={{ width: "100%", marginBottom: "8px", padding: "8px" }}
        />
      </div>

      <button onClick={handleLogin} style={{ padding: "12px 24px", fontSize: "16px" }}>
        Sign in with Google
      </button>
    </div>
  );
};

export default GoogleOAuthLogin;
