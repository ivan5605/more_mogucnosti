import React from 'react'

const FooterComponent = () => {
  return (
    <div className='container-fluid bg-white'>
      <footer className='d-flex flex-wrap justify-content-between align-items-center py-1 my-2 border-top footer'>
        <div className=' d-flex align-items-center'>
          <span className='mb-2 mb-md-0 text-body-secondary'>&copy; {new Date().getFullYear()} More Mogućnosti d.o.o.</span>
        </div>
        <ul className="nav col-md-4 justify-content-end list-unstyled d-flex">
          <li className="ms-3">
            <a className="text-body-secondary" href="#" aria-label="Facebook">
              <i className="bi bi-facebook"></i>
            </a>
          </li>
          <li className="ms-3">
            <a className="text-body-secondary" href="#" aria-label="Instagram">
              <i className="bi bi-instagram"></i>
            </a>
          </li>
        </ul>
      </footer>
    </div>
  )
}

export default FooterComponent