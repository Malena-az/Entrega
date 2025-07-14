export default function Header({ onOpenCart, cartCount }) {
    return (
      <header className="d-flex justify-content-between align-items-center p-3 bg-light">
        <img src="img/logo_calyma.png" width="100" height="100" alt="logo_calyma" />
        <nav>
          <ul className="menu d-flex list-unstyled gap-3">
            <li className="itemMenu">
              <a href="#productos" className="text-dark">Productos</a>
            </li>
            <li className="itemMenu">
              <a href="#reseñas" className="text-dark">Reseñas</a>
            </li>
            <li className="itemMenu">
              <button className="text-dark btn btn-link p-0" onClick={onOpenCart}>
                Carrito (<span>{cartCount}</span>)
              </button>
            </li>
            <li className="itemMenu">
              <a href="#redes-sociales" className="text-dark">Redes Sociales</a>
            </li>
          </ul>
        </nav>
      </header>
    );
  }

  





