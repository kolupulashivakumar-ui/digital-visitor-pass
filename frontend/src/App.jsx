import React, { useState } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const [visitorName, setVisitorName] = useState('');
  const [purpose, setPurpose] = useState('');
  const [dateTime, setDateTime] = useState('');
  const [passData, setPassData] = useState(null);
  const [loading, setLoading] = useState(false);

  const BACKEND_URL = 'http://localhost:8081';

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    const formattedDateTime = dateTime.length === 16 ? `${dateTime}:00` : dateTime;
    const cleanName = visitorName.toLowerCase().replace(/\s+/g, '');

    // User table mandatory fields anni ikkada pass chesthunnam
    const payload = {
      purpose: purpose,
      visitDateTime: formattedDateTime,
      visitor: {
        name: visitorName,
        email: `${cleanName}${Math.floor(Math.random() * 1000)}@example.com`,
        password: "password123",
        role: "VISITOR"
      }
    };

    try {
      const createRes = await axios.post(`${BACKEND_URL}/api/visits/create`, payload);
      const newPass = createRes.data;

      try {
        const approveRes = await axios.put(`${BACKEND_URL}/api/visits/${newPass.id}/status?status=APPROVED`);
        setPassData(approveRes.data);
      } catch (approveErr) {
        setPassData(newPass);
      }

    } catch (err) {
      console.error("Backend Error Details:", err.response ? err.response.data : err);
      alert('Backend error! Check backend terminal for details.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container">
      <div className="card">
        <h2>Digital Visitor Pass</h2>
        <p className="subtitle">Fill details to get your QR Entry Pass</p>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Visitor Name</label>
            <input
              type="text"
              required
              placeholder="Enter your name"
              value={visitorName}
              onChange={(e) => setVisitorName(e.target.value)}
            />
          </div>

          <div className="form-group">
            <label>Purpose of Visit</label>
            <input
              type="text"
              required
              placeholder="Meeting, Interview, Delivery..."
              value={purpose}
              onChange={(e) => setPurpose(e.target.value)}
            />
          </div>

          <div className="form-group">
            <label>Visit Date & Time</label>
            <input
              type="datetime-local"
              required
              value={dateTime}
              onChange={(e) => setDateTime(e.target.value)}
            />
          </div>

          <button type="submit" disabled={loading}>
            {loading ? 'Generating QR Pass...' : 'Generate Pass'}
          </button>
        </form>

        {passData && (
          <div className="pass-card">
            <h3>Entry Pass Generated</h3>
            <p><strong>Pass ID:</strong> #{passData.id}</p>
            <p><strong>Visitor:</strong> {visitorName}</p>
            <p><strong>Purpose:</strong> {passData.purpose}</p>
            <p>
              <strong>Status:</strong>{' '}
              <span className="badge">{passData.status}</span>
            </p>

            {passData.passQrCode && (
              <div>
                <img
                  className="qr-image"
                  src={`data:image/png;base64,${passData.passQrCode}`}
                  alt="Visitor QR Pass"
                  width="180"
                />
                <p style={{ fontSize: '13px', color: '#64748b' }}>
                  Show this QR code at the Security Checkpoint
                </p>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

export default App;