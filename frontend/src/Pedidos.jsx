import React, { useEffect, useState } from "react";

export default function Pedidos({ token }) {
  const [pedidos, setPedidos] = useState([]);

  useEffect(() => {
    fetch("/pedidos", {
      headers: {
        "Authorization": "Bearer " + token
      }
    })
      .then(res => res.json())
      .then(data => {
        if (Array.isArray(data)) setPedidos(data);
        else setPedidos([]);
      });
  }, [token]);

  return (
    <div className="container my-4">
      <h2>Mis pedidos</h2>
      {Array.isArray(pedidos) && pedidos.length === 0 ? (
        <p>No tienes pedidos aún.</p>
      ) : Array.isArray(pedidos) ? (
        pedidos.map(pedido => (
          <div
            key={pedido.id}
            style={{
              marginBottom: 32,
              border: "2px solid #d4af37",
              borderRadius: 16,
              padding: 24,
              background: "#fcfbe9",
              maxWidth: 900,
              marginLeft: "auto",
              marginRight: "auto",
              boxShadow: "0 2px 12px rgba(180,151,189,0.07)"
            }}
          >
            <strong style={{ color: "#b497bd" }}>ID Pedido:</strong> {pedido.id}<br />
            <strong style={{ color: "#b497bd" }}>Líneas del Pedido:</strong>
            <ul>
              {pedido.lineas && pedido.lineas.map((linea, idx) => (
                <li key={idx} style={{ display: "flex", alignItems: "center", marginBottom: 8 }}>
                  <img
                    src={linea.producto.imagen ? `/img/${linea.producto.imagen}` : "/img/logo_calyma.png"}
                    alt={linea.producto.nombre}
                    style={{ width: 40, height: 40, objectFit: "contain", marginRight: 12, borderRadius: 8, border: "1.5px solid #d4af37", background: "#fff" }}
                  />
                  <span>
                    {linea.producto.nombre} x {linea.cantidad} @ ${linea.producto.precio} (Subtotal: ${linea.subtotal})
                  </span>
                </li>
              ))}
            </ul>
            <strong style={{ color: "#b497bd" }}>Costo Total del Pedido:</strong> ${pedido.costoTotal}
          </div>
        ))
      ) : (
        <p>Error al cargar los pedidos.</p>
      )}
    </div>
  );
}
