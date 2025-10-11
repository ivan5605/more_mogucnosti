// src/App.jsx
import React from "react";
import { ToastContainer } from "react-toastify";
import "bootstrap-icons/font/bootstrap-icons.css";
import "@fortawesome/fontawesome-free/css/all.min.css";
import "react-toastify/dist/ReactToastify.css";
import "react-datepicker/dist/react-datepicker.css";
import "./App.css";

import { AuthProvider } from "./auth/AuthContext";
import AppRouter from "./routes/AppRouter";

export default function App() {
  return (
    <>
      <ToastContainer />
      <AppRouter />
    </>
  );
}
