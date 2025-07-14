  export default function CarritoSidebar({ carrito, total, onClose, onVaciar, onComprar }) {
    return (
      <>
        <div
          id="cart-sidebar"
          className="cart-sidebar"
          style={{
            position: "fixed",
            top: 0,
            right: 0,
            width: "350px",
            height: "100vh",
            background: "#fff",
            boxShadow: "0 0 20px rgba(0,0,0,0.2)",
            zIndex: 2000,
            padding: "20px",
            overflowY: "auto",
            transition: "transform 0.3s",
          }}
        >
          <div className="cart-header">
            <h3>Carrito <span className="cart-counter">{carrito.length}</span></h3>
            <button className="btn btn-secondary" onClick={onClose} style={{ float: "right" }}>Cerrar</button>
          </div>
          <div className="cart-content">
            <div className="cart-items">
              {carrito.length === 0 ? (
                <p>El carrito está vacío</p>
              ) : (
                carrito.map(item => (
                  <div key={item.id}>{item.nombre} x {item.cantidad}</div>
                ))
              )}
            </div>
            <div className="cart-footer">
              <div className="cart-total">
                <strong>Total: $<span>{total}</span></strong>
              </div>
              <div className="cart-actions">
                <button className="btn btn-danger btn-sm" onClick={onVaciar}>Vaciar</button>
                <button className="btn btn-success" onClick={onComprar}>Comprar</button>
              </div>
            </div>
          </div>
        </div>

        <div
          className="cart-overlay"
          onClick={onClose}
          style={{
            position: "fixed",
            top: 0,
            left: 0,
            width: "100vw",
            height: "100vh",
            background: "rgba(0,0,0,0.2)",
            zIndex: 1000,
          }}
        />
      </>
    );
  }