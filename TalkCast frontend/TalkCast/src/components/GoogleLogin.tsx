import React, { useEffect } from "react";
import { GOOGLE_CLIENT_ID } from "../config";
import { useDispatch } from 'react-redux';
import { setToken } from '../slice/authSlice';
import { useNavigate } from 'react-router-dom';

const GoogleLogin: React.FC = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();  
  useEffect(() => {
    const script = document.createElement("script");
    script.src = "https://accounts.google.com/gsi/client";
    script.async = true;
    script.defer = true;

    script.onload = () => {
      if (window.google) {
        window.google.accounts.id.initialize({
          client_id: GOOGLE_CLIENT_ID,
          callback: handleCredentialResponse,
          auto_select: true,
        });

        const buttonContainer = document.getElementById("google-signin-button");
        if (buttonContainer) {
          window.google.accounts.id.renderButton(buttonContainer, {
            theme: "outline",
            size: "large",
            shape: "pill",
            text: "signin_with",
          });
        }

        window.google.accounts.id.prompt();
      }


    };

    document.body.appendChild(script);
  }, []);

  const handleCredentialResponse = (response: CredentialResponse) => {
    console.log("Google ID Token:", response);

    fetch("http://localhost:8080/oauth2/callback", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ credential: response.credential }),
    })
      .then((res) => res.json())
      .then((data) => {
        console.log("Auth success:", data);
        const idToken = response;
        dispatch(setToken(idToken.clientId)); // ✅ Store in Redux
        navigate("/broadcast"); // ✅ Navigate after storing
      })
      .catch((err) => {
        console.error("Auth failed:", err);
      });
  };

  return <div id="google-signin-button"></div>;
};

export default GoogleLogin;
