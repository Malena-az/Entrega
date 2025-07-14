package com.calyma.backend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;
import com.calyma.backend.repository.RoleRepository;
import com.calyma.backend.model.Role;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		// Intentar cargar variables de entorno pero no fallar si no existe
		try {
			Dotenv dotenv = Dotenv.load();
			String dbPassword = dotenv.get("CLAVE_MYSQL");
			if(dbPassword != null && !dbPassword.trim().isEmpty()){
				System.setProperty("CLAVE_MYSQL", dbPassword);
				System.out.println("Configuración de .env cargada");
			} else {
				System.out.println("Usando configuración sin contraseña de MySQL");
			}
		} catch (Exception e) {
			System.out.println("Archivo .env no encontrado usando configuración por defecto");
		}

		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Inicializar roles si no existen
		if (roleRepository.findByName("USER").isEmpty()) {
			roleRepository.save(new Role(null, "USER"));
			System.out.println("Rol USER creado");
		}
		if (roleRepository.findByName("ADMIN").isEmpty()) {
			roleRepository.save(new Role(null, "ADMIN"));
			System.out.println("Rol ADMIN creado");
		}
		System.out.println("Aplicación iniciada correctamente");
	}
}		

