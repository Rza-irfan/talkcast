// GoogleOAuthLogin.tsx
import React from "react";
import { GoogleOAuthProvider, GoogleLogin } from "@react-oauth/google";
import type { CredentialResponse } from "@react-oauth/google";
import axios from "axios";

const CLIENT_ID = "494131558045-mt4avl1rfuf59oj5r73b2fihrtokfc0m.apps.googleusercontent.com"

const GoogleOAuthLogin: React.FC = () => {
  const handleLoginSuccess = async (credentialResponse: CredentialResponse): Promise<void> => {
    const token = credentialResponse.credential;

    if (!token) {
      console.error("No credential received from Google.");
      return;
    }

    console.log("Google JWT Token:", token);

    try {
        const response = await axios.post("http://localhost:8080/api/youtube/broadcast", {
            token,
          });
      console.log("Backend response:", response.data);

      localStorage.setItem("accessToken", response.data.accessToken);

      // Redirect to broadcast page
      window.location.href = "/broadcast";
    } catch (error) {
      console.error("Error during backend call:", error);
    }
  };

  return (
    <GoogleOAuthProvider clientId={CLIENT_ID}>
      <div className="flex justify-center items-center h-screen">
        <GoogleLogin
          onSuccess={handleLoginSuccess}
          onError={() => console.error("Google login failed")}
          useOneTap
        />
      </div>
    </GoogleOAuthProvider>
  );
};

export default GoogleOAuthLogin;
