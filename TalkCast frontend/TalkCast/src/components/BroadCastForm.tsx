import React, { useState } from "react";
import { useSelector } from "react-redux";
import type { RootState } from "../store/store";

const BroadcastForm: React.FC = () => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [startTime, setStartTime] = useState("");
  const token = useSelector((state: RootState) => state.auth.token);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const response = await fetch(`http://localhost:8080/api/youtube/broadcast?code=${token}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include", // optional, if you're using cookies
    //   body: JSON.stringify({ title, description, startTime, token}),
    });

    const data = await response.text();
    alert(data);
  };

  return (
    <div style={{ padding: "2rem", maxWidth: "500px", margin: "auto" }}>
      <h2>📺 Schedule YouTube Live Broadcast</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Title:</label><br />
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
            style={{ width: "100%", padding: "0.5rem", marginBottom: "1rem" }}
          />
        </div>

        <div>
          <label>Description:</label><br />
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            rows={4}
            style={{ width: "100%", padding: "0.5rem", marginBottom: "1rem" }}
          />
        </div>

        <div>
          <label>Start Time (ISO):</label><br />
          <input
            type="datetime-local"
            value={startTime}
            onChange={(e) => setStartTime(e.target.value)}
            required
            style={{ width: "100%", padding: "0.5rem", marginBottom: "1.5rem" }}
          />
        </div>

        <button
          type="submit"
          style={{
            padding: "10px 20px",
            backgroundColor: "#4285F4",
            color: "#fff",
            border: "none",
            borderRadius: "8px",
            cursor: "pointer",
          }}
        >
          🎬 Create Broadcast
        </button>
      </form>
    </div>
  );
};

export default BroadcastForm;
