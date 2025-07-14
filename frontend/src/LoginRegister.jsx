import React, { useState } from "react";

export default function LoginRegister({ onLogin, onRegister, error }) {
  const [isLogin, setIsLogin] = useState(true);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [validationError, setValidationError] = useState("");

  const handleSubmit = (e) => {
    e.preventDefault();
    // Validación para registro
    if (!isLogin) {
      if (username.length < 4) {
        setValidationError("El usuario debe tener al menos 4 caracteres.");
        return;
      }
      if (password.length < 6) {
        setValidationError("La contraseña debe tener al menos 6 caracteres.");
        return;
      }
    }
    setValidationError("");
    if (isLogin) {
      onLogin(username, password);
    } else {
      onRegister(username, password);
    }
  };

  return (
    <div style={{
      display: "flex",
      justifyContent: "center",
      alignItems: "center",
      minHeight: "60vh",
      padding: "20px"
    }}>
      <div style={{
        backgroundColor: "white",
        borderRadius: "20px",
        padding: "40px",
        boxShadow: "0 10px 30px rgba(0,0,0,0.1)",
        width: "100%",
        maxWidth: "400px",
        border: "2px solid #f0f0f0"
      }}>
        <h2 style={{
          textAlign: "center",
          color: "#6a4c93",
          marginBottom: "30px",
          fontSize: "28px",
          fontWeight: "bold"
        }}>
          {isLogin ? "Iniciar Sesión" : "Crear Cuenta"}
        </h2>
        
        <form onSubmit={handleSubmit} style={{ marginBottom: "20px" }}>
          <div style={{ marginBottom: "20px" }}>
            <label style={{
              display: "block",
              marginBottom: "8px",
              color: "#6a4c93",
              fontWeight: "bold",
              fontSize: "14px"
            }}>
              Usuario
            </label>
            <input
              type="text"
              placeholder="Ingresa tu usuario"
              value={username}
              onChange={e => setUsername(e.target.value)}
              required
              style={{
                width: "100%",
                padding: "12px 16px",
                borderRadius: "10px",
                border: "2px solid #e8d5f0",
                fontSize: "16px",
                transition: "all 0.3s ease",
                boxSizing: "border-box"
              }}
              onFocus={(e) => {
                e.target.style.borderColor = "#b497bd";
                e.target.style.boxShadow = "0 0 0 3px rgba(180, 151, 189, 0.1)";
              }}
              onBlur={(e) => {
                e.target.style.borderColor = "#e8d5f0";
                e.target.style.boxShadow = "none";
              }}
            />
          </div>
          
          <div style={{ marginBottom: "25px" }}>
            <label style={{
              display: "block",
              marginBottom: "8px",
              color: "#6a4c93",
              fontWeight: "bold",
              fontSize: "14px"
            }}>
              Contraseña
            </label>
            <input
              type="password"
              placeholder="Ingresa tu contraseña"
              value={password}
              onChange={e => setPassword(e.target.value)}
              required
              style={{
                width: "100%",
                padding: "12px 16px",
                borderRadius: "10px",
                border: "2px solid #e8d5f0",
                fontSize: "16px",
                transition: "all 0.3s ease",
                boxSizing: "border-box"
              }}
              onFocus={(e) => {
                e.target.style.borderColor = "#b497bd";
                e.target.style.boxShadow = "0 0 0 3px rgba(180, 151, 189, 0.1)";
              }}
              onBlur={(e) => {
                e.target.style.borderColor = "#e8d5f0";
                e.target.style.boxShadow = "none";
              }}
            />
          </div>
          
          <button 
            type="submit"
            style={{
              width: "100%",
              backgroundColor: "#b497bd",
              color: "white",
              padding: "14px",
              borderRadius: "10px",
              border: "none",
              fontSize: "16px",
              fontWeight: "bold",
              cursor: "pointer",
              transition: "all 0.3s ease",
              marginBottom: "15px"
            }}
            onMouseOver={(e) => {
              e.target.style.backgroundColor = "#a085b0";
              e.target.style.transform = "translateY(-2px)";
            }}
            onMouseOut={(e) => {
              e.target.style.backgroundColor = "#b497bd";
              e.target.style.transform = "translateY(0)";
            }}
          >
            {isLogin ? "Iniciar Sesión" : "Crear Cuenta"}
          </button>
        </form>
        
        {validationError && (
          <div style={{
            color: "#dc3545",
            backgroundColor: "#f8d7da",
            border: "1px solid #f5c6cb",
            borderRadius: "8px",
            padding: "12px",
            marginBottom: "15px",
            fontSize: "14px"
          }}>
            {validationError}
          </div>
        )}
        
        {error && (
          <div style={{
            color: "#dc3545",
            backgroundColor: "#f8d7da",
            border: "1px solid #f5c6cb",
            borderRadius: "8px",
            padding: "12px",
            marginBottom: "15px",
            fontSize: "14px"
          }}>
            {error}
          </div>
        )}
        
        <div style={{ textAlign: "center" }}>
          <button
            onClick={() => setIsLogin(!isLogin)}
            style={{
              backgroundColor: "transparent",
              border: "none",
              color: "#6a4c93",
              cursor: "pointer",
              fontSize: "14px",
              textDecoration: "underline",
              transition: "color 0.3s ease"
            }}
            onMouseOver={(e) => {
              e.target.style.color = "#b497bd";
            }}
            onMouseOut={(e) => {
              e.target.style.color = "#6a4c93";
            }}
          >
            {isLogin ? "¿No tienes cuenta? Crear una" : "¿Ya tienes cuenta? Iniciar sesión"}
          </button>
        </div>
      </div>
    </div>
  );
}
