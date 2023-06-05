package br.com.api.teste.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.api.teste.security.domain.Role;
import br.com.api.teste.security.enums.RoleEnum;

@Repository("role")
public interface RoleRepository extends JpaRepository<Role, Integer> {
	//findByName(RoleEnum name)-> busca pelo nome, porém só aceita se existir no enum RoleEnum
	Optional<Role> findByName(RoleEnum name);
}