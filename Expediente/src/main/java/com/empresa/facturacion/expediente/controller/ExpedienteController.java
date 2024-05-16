package com.empresa.facturacion.expediente.controller;

import com.empresa.facturacion.expediente.entities.Expediente;
import com.empresa.facturacion.expediente.repository.ExpedienteRepository;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expediente")
public class ExpedienteController {
    
    @Autowired
    ExpedienteRepository expedienteRepository;
    
    @GetMapping()
    public ResponseEntity<?> getAll()
    {
        return ResponseEntity.ok(expedienteRepository.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable long id)
    {
        
        Optional<Expediente> expediente = expedienteRepository.findById(id);
        
        if(expediente.isPresent())
        {
            return ResponseEntity.ok(expediente.get());
        }
        else
        {
            return ResponseEntity.notFound().build();
        }   
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id,@Valid @RequestBody Expediente input, BindingResult result) 
    {
        if(result.hasFieldErrors())
        {
            return validation(result);
        }
        
        Optional<Expediente> expediente = expedienteRepository.findById(id);
        if (expediente.isPresent()) 
        {
            Expediente newExpediente = expediente.get();
            newExpediente.setDni(input.getDni());
            newExpediente.setNotas(input.getNotas());
            newExpediente.setTipoPrestacion(input.getTipoPrestacion());
            newExpediente = expedienteRepository.save(newExpediente);
            return ResponseEntity.ok(newExpediente);
        } 
        else 
        {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PostMapping                                                                                                                                                                                                                                                                                                    
    public ResponseEntity<?> create(@Valid @RequestBody Expediente expediente, BindingResult result) 
    {   
        if(result.hasFieldErrors())
        {
            return validation(result);
        }
        
        expediente = expedienteRepository.save(expediente);
        return ResponseEntity.status(HttpStatus.CREATED).body(expediente);       
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        Optional<Expediente> expediente = expedienteRepository.findById(id);
        if (expediente.isPresent()) 
        {
            expedienteRepository.deleteById(id);
            return ResponseEntity.ok().build();        
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscarPorDni")
    public ResponseEntity<?> findByDni(@RequestParam String dni) 
    {
        Expediente expediente = expedienteRepository.findByDni(dni);
        if (expediente != null) 
        {
            return ResponseEntity.ok(expediente);
        } 
        else 
        {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/buscarPorTipoPrestacion")
    public ResponseEntity<?> findByTipoPrestacion(@RequestParam int tipoPrestacion) 
    {
        List<Expediente> expediente = expedienteRepository.findByTipoPrestacion(tipoPrestacion);
        if (!expediente.isEmpty()) 
        {
            return ResponseEntity.ok(expediente);
        } 
        else 
        {
            return ResponseEntity.notFound().build();
        }
    }
    
    
    private ResponseEntity<?> validation(BindingResult result)
    {
        Map<String,String> errors= new HashMap();
        result.getFieldErrors().forEach(err->{
            errors.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
