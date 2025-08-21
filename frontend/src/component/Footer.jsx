import React from "react";
import { FaFacebookF, FaInstagram, FaLinkedinIn, FaEnvelope } from "react-icons/fa";

const Footer = () => {
  return (
    <footer className="w-full mt-10 bg-black bg-opacity-50 text-center text-sm text-white py-6">
      <p className="text-white font-semibold text-lg tracking-wide">UNITY TECH</p>
      <p className="text-sm mb-3 text-white">Computers Service</p>

      {/* Redes sociales */}
      <div className="flex justify-center gap-6 mb-4">
        <a href="https://facebook.com" target="_blank" rel="noopener noreferrer" className="hover:text-blue-500 transition-all duration-300">
          <FaFacebookF size={20} />
        </a>
        <a href="https://instagram.com" target="_blank" rel="noopener noreferrer" className="hover:text-pink-500 transition-all duration-300">
          <FaInstagram size={20} />
        </a>
        <a href="https://linkedin.com" target="_blank" rel="noopener noreferrer" className="hover:text-blue-400 transition-all duration-300">
          <FaLinkedinIn size={20} />
        </a>
        <a href="mailto:contacto@unitytech.com" className="hover:text-green-400 transition-all duration-300">
          <FaEnvelope size={20} />
        </a>
      </div>

      {/* Botón de contacto */}
      <div className="mb-3">
        <a
          href="mailto:contacto@unitytech.com"
          className="inline-block px-5 py-2 r bg-gradient-to-r from-blue-700 to-black rounded-lg  text-white font-medium text-sm shadow-md hover:shadow-blue-500/40 hover:scale-105 transition-all duration-300"
        >
          Contactanos
        </a>
      </div>

      {/* Créditos */}
      <p className="text-xs text-white">
        © {new Date().getFullYear()} UNITY TECH. Todos los derechos reservados.
      </p>
    </footer>
  );
};

export default Footer;