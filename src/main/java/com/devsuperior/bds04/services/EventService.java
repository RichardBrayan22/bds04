package com.devsuperior.bds04.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.repositories.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    @Transactional(readOnly = true)
    public Page<EventDTO> findAllPaged(Pageable pageable){
        Page<Event> list = repository.findAll(pageable);
        return list.map(lists -> new ModelMapper().map(lists, EventDTO.class));
    }

    @Transactional
    public EventDTO insert(EventDTO dto){
        dto.setId(null);
		ModelMapper mapper = new ModelMapper();
		Event entity = mapper.map(dto, Event.class);
		entity = repository.save(entity);
		dto.setId(entity.getId());
		return dto;
    }
    
}
