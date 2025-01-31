package com.devsuperior.bds04.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds04.dto.CityDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.repositories.CityRepository;

@Service
public class CityService {

    @Autowired
    private CityRepository repository;

    @Transactional(readOnly = true)
    public List<CityDTO> findAllPaged(){
        List<City> list = repository.findAll(Sort.by("name"));
        return list.stream().map(lists -> new ModelMapper().map(lists, CityDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public CityDTO insert(CityDTO dto){
        dto.setId(null);
		ModelMapper mapper = new ModelMapper();
		City entity = mapper.map(dto, City.class);
		entity = repository.save(entity);
		dto.setId(entity.getId());
		return dto;
    }
    
}
