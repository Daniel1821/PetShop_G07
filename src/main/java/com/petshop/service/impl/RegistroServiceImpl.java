package com.petshop.service.impl;
import com.petshop.domain.*;
import com.petshop.repository.*;
import com.petshop.service.RegistroService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service public class RegistroServiceImpl implements RegistroService {
 private final UsuarioRepository u; private final RolRepository r; private final UsuarioRolRepository ur; private final PasswordEncoder e;
 public RegistroServiceImpl(UsuarioRepository u,RolRepository r,UsuarioRolRepository ur,PasswordEncoder e){this.u=u;this.r=r;this.ur=ur;this.e=e;}
 @Override @Transactional public void registrar(String username,String nombre,String apellidos,String correo,String password){if(username.isBlank()||nombre.isBlank()||apellidos.isBlank()||!correo.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")||password.length()<3)throw new IllegalArgumentException("Datos inválidos.");if(u.findByUsername(username).isPresent()||u.findByCorreo(correo).isPresent())throw new IllegalArgumentException("Usuario o correo ya registrado.");Usuario x=new Usuario();x.setUsername(username);x.setNombre(nombre);x.setApellidos(apellidos);x.setCorreo(correo);x.setPassword(e.encode(password));x.setActivo(true);u.save(x);Rol rol=r.findByRol("CLIENTE").orElseThrow();ur.save(new UsuarioRol(new UsuarioRolId(x.getIdUsuario(),rol.getIdRol()),x,rol,null,null));}
}
