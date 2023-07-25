package project.ufrn.pw.api_rest.controller;

import org.springframework.web.bind.annotation.*;
import project.ufrn.pw.api_rest.domain.Endereco;
import project.ufrn.pw.api_rest.repository.EnderecoRepository;
import project.ufrn.pw.api_rest.service.EnderecoService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    EnderecoRepository repository;
    EnderecoService service;
    ModelMapper mapper;

    public EnderecoController(EnderecoService service, ModelMapper mapper, EnderecoRepository repository) {
        this.service = service;
        this.mapper = mapper;
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Endereco.DtoResponse create(@RequestBody Endereco.DtoRequest p) {

        Endereco endereco = this.service.create(Endereco.DtoRequest.convertToEntity(p, mapper));
        Endereco.DtoResponse response = Endereco.DtoResponse.convertToDto(endereco, mapper);
        response.generateLinks(endereco.getId());

        return response;
    }

    @GetMapping
    public List<Endereco.DtoResponse> list() {
        return this.service.list().stream().map(
                elementoAtual -> {
                    Endereco.DtoResponse response = Endereco.DtoResponse.convertToDto(elementoAtual, mapper);
                    response.generateLinks(elementoAtual.getId());
                    return response;
                }).toList();
    }
    @GetMapping("{id}")
    public Endereco.DtoResponse getById(@PathVariable Long id){

        Endereco endereco = this.service.getById(id);
        Endereco.DtoResponse response = Endereco.DtoResponse.convertToDto(endereco, mapper);
        response.generateLinks(endereco.getId());

        return response;
    }

    @PutMapping("{id}")
    public Endereco update(@PathVariable("id") Long id, @RequestBody Endereco endereco) {
        return repository.findById(id)
                .map(e -> {
                    if (endereco.getRua() != null) {
                    e.setRua(endereco.getRua());
                    }
                    return repository.save(e);
                }).orElseThrow();
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
    }
}