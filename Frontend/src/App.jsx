import React, { useEffect, useMemo, useState } from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Navigate, Route, Routes, useNavigate } from "react-router-dom";
import api from "./api/axios";
import "./styles/app.css";

const useAuth = () => {
  const [token, setToken] = useState(localStorage.getItem("token"));
  const [user, setUser] = useState({ name: "", role: "", email: "" });
  const [loadingProfile, setLoadingProfile] = useState(false);

  useEffect(() => {
    if (!token) {
      setUser({ name: "", role: "", email: "" });
      return;
    }

    const fetchProfile = async () => {
      if (loadingProfile || user.role) {
        return;
      }
      setLoadingProfile(true);
      try {
        const response = await api.get("/auth/me");
        setUser({
          name: response.data.name,
          role: response.data.role,
          email: response.data.email
        });
      } catch (err) {
        localStorage.removeItem("token");
        setToken(null);
        setUser({ name: "", role: "", email: "" });
      } finally {
        setLoadingProfile(false);
      }
    };

    fetchProfile();
  }, [token, loadingProfile, user.role]);

  const saveAuth = (payload) => {
    localStorage.setItem("token", payload.token);
    setToken(payload.token);
    setUser({ name: payload.name, role: payload.role, email: payload.email || "" });
  };

  const clearAuth = () => {
    localStorage.removeItem("token");
    setToken(null);
    setUser({ name: "", role: "", email: "" });
  };

  return { token, user, saveAuth, clearAuth };
};

const ProtectedRoute = ({ token, children }) => {
  if (!token) {
    return <Navigate to="/login" replace />;
  }
  return children;
};

const LandingPage = ({ token }) => {
  const navigate = useNavigate();

  return (
    <div className="card">
      <h2>Welcome to SecureTasker</h2>
      <p>Choose an option to continue.</p>
      <div style={{ display: "flex", gap: 12, flexWrap: "wrap", marginTop: 16 }}>
        <button className="button" type="button" onClick={() => navigate("/login")}>
          Login
        </button>
        <button className="button secondary" type="button" onClick={() => navigate("/register")}>
          Register
        </button>
        <button
          className="button"
          type="button"
          onClick={() => navigate(token ? "/dashboard" : "/login")}
        >
          Admin Dashboard
        </button>
      </div>
    </div>
  );
};

const AuthLayout = ({ children, title, subtitle }) => (
  <div className="card">
    <h2>{title}</h2>
    <p>{subtitle}</p>
    {children}
  </div>
);

const RegisterPage = ({ onRegister }) => {
  const navigate = useNavigate();
  const [form, setForm] = useState({ name: "", email: "", password: "" });
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);

  const handleChange = (event) => {
    setForm((prev) => ({ ...prev, [event.target.name]: event.target.value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError(null);
    setMessage(null);
    try {
      const response = await api.post("/auth/register", form);
      onRegister(response.data);
      setMessage("Account created successfully.");
      navigate("/dashboard", { replace: true });
    } catch (err) {
      setError(err.response?.data?.message || "Registration failed.");
    }
  };

  return (
    <AuthLayout title="Create account" subtitle="Start managing tasks in minutes.">
      <form onSubmit={handleSubmit}>
        <input className="input" name="name" placeholder="Full name" value={form.name} onChange={handleChange} />
        <input className="input" name="email" placeholder="Email" value={form.email} onChange={handleChange} />
        <input className="input" name="password" type="password" placeholder="Password" value={form.password} onChange={handleChange} />
        <div style={{ marginTop: 16 }}>
          <button className="button" type="submit">Register</button>
        </div>
      </form>
      {message && <div className="message">{message}</div>}
      {error && <div className="message error">{error}</div>}
    </AuthLayout>
  );
};

const LoginPage = ({ onLogin }) => {
  const navigate = useNavigate();
  const [form, setForm] = useState({ email: "", password: "" });
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);

  const handleChange = (event) => {
    setForm((prev) => ({ ...prev, [event.target.name]: event.target.value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError(null);
    setMessage(null);
    try {
      const response = await api.post("/auth/login", form);
      onLogin(response.data);
      setMessage("Welcome back!");
      navigate("/dashboard", { replace: true });
    } catch (err) {
      setError(err.response?.data?.message || "Login failed.");
    }
  };

  return (
    <AuthLayout title="Welcome back" subtitle="Log in to continue.">
      <form onSubmit={handleSubmit}>
        <input className="input" name="email" placeholder="Email" value={form.email} onChange={handleChange} />
        <input className="input" name="password" type="password" placeholder="Password" value={form.password} onChange={handleChange} />
        <div style={{ marginTop: 16 }}>
          <button className="button" type="submit">Login</button>
        </div>
      </form>
      {message && <div className="message">{message}</div>}
      {error && <div className="message error">{error}</div>}
    </AuthLayout>
  );
};

const DashboardPage = ({ token, user, onLogout }) => {
  const [tasks, setTasks] = useState([]);
  const [form, setForm] = useState({ title: "", description: "", status: "PENDING" });
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);

  const fetchTasks = async () => {
    setError(null);
    try {
      const response = await api.get("/tasks");
      setTasks(response.data);
    } catch (err) {
      setError(err.response?.data?.message || "Unable to load tasks.");
    }
  };

  useEffect(() => {
    if (token) {
      fetchTasks();
    }
  }, [token]);

  const handleChange = (event) => {
    setForm((prev) => ({ ...prev, [event.target.name]: event.target.value }));
  };

  const handleCreate = async (event) => {
    event.preventDefault();
    setError(null);
    setMessage(null);
    try {
      await api.post("/tasks", form);
      setMessage("Task created.");
      setForm({ title: "", description: "", status: "PENDING" });
      fetchTasks();
    } catch (err) {
      setError(err.response?.data?.message || "Unable to create task.");
    }
  };

  const handleToggleStatus = async (task) => {
    const nextStatus = task.status === "PENDING" ? "COMPLETED" : "PENDING";
    try {
      await api.patch(`/tasks/${task.id}/status`, { status: nextStatus });
      fetchTasks();
    } catch (err) {
      setError(err.response?.data?.message || "Unable to update task.");
    }
  };

  const handleDelete = async (taskId) => {
    try {
      await api.delete(`/tasks/${taskId}`);
      fetchTasks();
    } catch (err) {
      setError(err.response?.data?.message || "Unable to delete task.");
    }
  };

  const taskCount = useMemo(() => tasks.length, [tasks]);

  return (
    <div className="grid">
      <div className="card">
        <h2>Hello {user.name || "there"}</h2>
        <p>Role: {user.role || "ROLE_USER"}</p>
        <p>Total tasks: {taskCount}</p>
        <button className="button secondary" type="button" onClick={onLogout}>Log out</button>
      </div>

      <div className="card">
        <h3>Create task</h3>
        <form onSubmit={handleCreate}>
          <input className="input" name="title" placeholder="Task title" value={form.title} onChange={handleChange} />
          <textarea className="input" name="description" placeholder="Description" value={form.description} onChange={handleChange} />
          <select className="input" name="status" value={form.status} onChange={handleChange}>
            <option value="PENDING">PENDING</option>
            <option value="COMPLETED">COMPLETED</option>
          </select>
          <div style={{ marginTop: 16 }}>
            <button className="button" type="submit">Add task</button>
          </div>
        </form>
        {message && <div className="message">{message}</div>}
        {error && <div className="message error">{error}</div>}
      </div>

      <div className="card" style={{ gridColumn: "1 / -1" }}>
        <h3>Tasks</h3>
        <div className="grid">
          {tasks.map((task) => (
            <div className="task" key={task.id}>
              <div className="task-row">
                <strong>{task.title}</strong>
                <span className="badge">{task.status}</span>
              </div>
              {user.role === "ROLE_ADMIN" && task.userName && task.userEmail && (
                <p>
                  Owner: {task.userName} ({task.userEmail})
                </p>
              )}
              <p>{task.description || "No description"}</p>
              <div className="task-row">
                <button className="button secondary" type="button" onClick={() => handleToggleStatus(task)}>
                  Toggle status
                </button>
                <button className="button" type="button" onClick={() => handleDelete(task.id)}>
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

const AppShell = () => {
  const { token, user, saveAuth, clearAuth } = useAuth();

  return (
    <BrowserRouter>
      <div className="app-shell">
        <header className="header">
          <div className="brand">SecureTasker</div>
          <div>Secure workflow for teams</div>
        </header>
        <Routes>
          <Route path="/" element={<LandingPage token={token} />} />
          <Route path="/register" element={<RegisterPage onRegister={saveAuth} />} />
          <Route path="/login" element={<LoginPage onLogin={saveAuth} />} />
          <Route
            path="/dashboard"
            element={
              <ProtectedRoute token={token}>
                <DashboardPage token={token} user={user} onLogout={clearAuth} />
              </ProtectedRoute>
            }
          />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
};

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<AppShell />);
