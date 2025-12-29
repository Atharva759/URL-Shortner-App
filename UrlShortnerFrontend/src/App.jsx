import { useState, useEffect } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  useParams,
} from "react-router-dom";

function ShortenForm() {
  const [originalUrl, setOriginalUrl] = useState("");
  const [customAlias, setCustomAlias] = useState("");
  const [expiresAt, setExpiresAt] = useState("1M");
  const [message, setMessage] = useState("");
  const [shortUrl, setShortUrl] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setShortUrl("");

    let now = new Date();
    let expireDate = new Date(now);
    switch (expiresAt) {
      case "1D":
        expireDate.setDate(now.getDate() + 1);
        break;
      case "5D":
        expireDate.setDate(now.getDate() + 5);
        break;
      case "7D":
        expireDate.setDate(now.getDate() + 7);
        break;
      case "1M":
        expireDate.setMonth(now.getMonth() + 1);
        break;
      default:
        expireDate.setMonth(now.getMonth() + 1);
    }

    const body = {
      originalUrl,
      customAlias: customAlias || null,
      expiresAt: expireDate.toISOString(),
    };

    try {
      const res = await fetch(`${import.meta.env.VITE_API_BASE_URL}/shorten`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });

      if (res.ok) {
        const data = await res.json();
        const frontendUrl = `${import.meta.env.VITE_FRONTEND_URL}/${
          data.shortCode
        }`;
        setShortUrl(frontendUrl);
        setMessage("URL shortened successfully!");
      } else if (res.status === 400) {
        setMessage("Bad request: check your input");
      } else if (res.status === 404) {
        setMessage("URL not found");
      } else {
        setMessage("Something went wrong!");
      }
    } catch (err) {
      setMessage("Error connecting to server");
    }
  };

  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-linear-to-r from-purple-100 via-pink-50 to-yellow-50 p-6">
      {/* Form Container */}
      <div className="bg-white/30 backdrop-blur-md shadow-2xl rounded-2xl p-10 max-w-md w-full transform transition duration-500 hover:scale-105 border border-white/20">
        {/* Heading + Tagline */}
        <div className="text-center mb-8">
          <h1 className="text-3xl font-extrabold text-gray-800 mb-2">
            Shorten Your URL
          </h1>
          <p className="text-gray-700">
            Make long links simple, memorable & shareable
          </p>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="block font-medium text-gray-700 mb-1">
              Original URL
            </label>
            <input
              type="url"
              required
              value={originalUrl}
              onChange={(e) => setOriginalUrl(e.target.value)}
              className="mt-1 block w-full px-4 py-3 border border-gray-300/50 rounded-xl focus:outline-none focus:ring-2 focus:ring-gray-400 focus:border-transparent bg-white/70 shadow-sm"
              placeholder="https://example.com"
            />
          </div>

          <div>
            <label className="block font-medium text-gray-700 mb-1">
              Custom Alias (optional)
            </label>
            <input
              type="text"
              value={customAlias}
              onChange={(e) => setCustomAlias(e.target.value)}
              className="mt-1 block w-full px-4 py-3 border border-gray-300/50 rounded-xl focus:outline-none focus:ring-2 focus:ring-gray-400 focus:border-transparent bg-white/70 shadow-sm"
              placeholder="myalias"
            />
          </div>

          <div>
            <label className="block font-medium text-gray-700 mb-1">
              Expiry
            </label>
            <select
              value={expiresAt}
              onChange={(e) => setExpiresAt(e.target.value)}
              className="mt-1 block w-full px-4 py-3 border border-gray-300/50 rounded-xl focus:outline-none focus:ring-2 focus:ring-gray-400 focus:border-transparent bg-white/70 shadow-sm"
            >
              <option value="1D">1 Day</option>
              <option value="5D">5 Days</option>
              <option value="7D">7 Days</option>
              <option value="1M">1 Month</option>
            </select>
          </div>

          <button
            type="submit"
            className="w-full py-3 px-4 bg-gray-600 text-white font-semibold rounded-xl hover:bg-gray-700 transition shadow-md cursor-pointer"
          >
            Shorten URL
          </button>
        </form>

        {/* Messages */}
        {message && (
          <p className="mt-5 text-center text-gray-800 italic">{message}</p>
        )}
        {shortUrl && (
          <p className="mt-3 text-center text-green-500 font-semibold break-all">
            <a href={shortUrl} target="_blank" rel="noopener noreferrer">
              {shortUrl}
            </a>
          </p>
        )}
      </div>
    </div>
  );
}

function RedirectPage() {
  const { code } = useParams();

  useEffect(() => {
    const url = `${import.meta.env.VITE_API_BASE_URL}/${code}`
    window.location.href = url;
  }, [code]);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <p className="text-gray-800 font-semibold text-lg">Redirecting...</p>
    </div>
  );
}

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<ShortenForm />} />
        <Route path="/:code" element={<RedirectPage />} />
      </Routes>
    </Router>
  );
}
