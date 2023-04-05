package com.devsuperior.bds04.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds04.dto.RoleDTO;
import com.devsuperior.bds04.dto.UserDTO;
import com.devsuperior.bds04.entities.Role;
import com.devsuperior.bds04.entities.User;
import com.devsuperior.bds04.repositories.RoleRepository;
import com.devsuperior.bds04.repositories.UserRepository;
import com.devsuperior.bds04.services.exceptions.DatabaseException;
import com.devsuperior.bds04.services.exceptions.ResourceNotFoundException;


@Service
public class UserService implements UserDetailsService {

	private static Logger logger = LoggerFactory.getLogger(UserService.class);



	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository rolerepository;

	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> list = repository.findAll(pageable);
		return list.map(x -> new ModelMapper().map(x, UserDTO.class));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		ModelMapper mapper = new ModelMapper();
		if(obj.isEmpty()){
			throw new ResourceNotFoundException("Entity not found");
		}
		UserDTO dto = mapper.map(obj.get(), UserDTO.class);
		//User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return dto;
		//return new UserDTO(entity, entity.getCategories());
	}

	@Transactional
	public UserDTO insert(UserDTO dto) {
		dto.setId(null);
		ModelMapper mapper = new ModelMapper();
		User entity = mapper.map(dto, User.class);
		for( RoleDTO roleDto : dto.getRoles()){
			Role role = rolerepository.getReferenceById(roleDto.getId());
			entity.getRoles().add(role);
			roleDto.setAuthority(role.getAuthority());
		}
		entity = repository.save(entity);
		dto.setId(entity.getId());
		return dto;
	}

	@Transactional
	public UserDTO update(Long id, UserDTO dto) {
		try{
			dto.setId(id);
			ModelMapper mapper = new ModelMapper();
			User entity = mapper.map(dto, User.class);
			entity.getRoles().clear();
			for( RoleDTO roleDto : dto.getRoles()){
				Role role = rolerepository.getReferenceById(roleDto.getId());
				entity.getRoles().add(role);
				roleDto.setAuthority(role.getAuthority());
			}
			entity = repository.save(entity);
			return dto;
		}
		catch (EntityNotFoundException e){
			throw new ResourceNotFoundException("Id not Found " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByEmail(username);
		if(user == null) {
			logger.error("User not found" + username);
			throw new UsernameNotFoundException("Email not found");
		}
		logger.info("User found: " + username);
		return user;
	}
	
		
}
