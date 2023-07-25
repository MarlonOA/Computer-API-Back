package project.ufrn.pw.api_rest.controller;

import org.springframework.web.bind.annotation.*;

import project.ufrn.pw.api_rest.domain.Endereco;
import project.ufrn.pw.api_rest.domain.Usuario;
import project.ufrn.pw.api_rest.domain.Usuario.DtoResponse;
import project.ufrn.pw.api_rest.repository.EnderecoRepository;
import project.ufrn.pw.api_rest.repository.UsuarioRepository;
import project.ufrn.pw.api_rest.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/usuario")
@CrossOrigin
public class UsuarioController {
    EnderecoRepository endRespository;
    UsuarioService service;
    UsuarioRepository repository;
    ModelMapper mapper;

    public UsuarioController(UsuarioService service, ModelMapper mapper, UsuarioRepository repository, EnderecoRepository endRespository) {
        this.service = service;
        this.mapper = mapper;
        this.repository = repository;
        this.endRespository = endRespository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario.DtoResponse create(@RequestBody Usuario.DtoRequest u) {
        Endereco end = endRespository.findById(u.getEndereco_id()).get();
        Usuario user = Usuario.DtoRequest.convertToEntity(u, mapper);
        user.setMeuEndereco(end);
        service.createUsuario(user);
        Usuario.DtoResponse uFromUser = Usuario.DtoResponse.convertToDto(user, mapper);
        uFromUser.generateLinks(user.getId());

        return uFromUser;
    }

    @GetMapping
    public List<DtoResponse> list(){
        return this.service.list().stream().map(
                elementoAtual -> {
                    Usuario.DtoResponse response = Usuario.DtoResponse.convertToDto(elementoAtual, mapper);
                    response.generateLinks(elementoAtual.getId());
                    return response;
                }).toList();
    }

    @GetMapping("{id}")
    public Usuario.DtoResponse getById(@PathVariable Long id){
        Usuario usuario = this.service.getById(id);
        Usuario.DtoResponse response = Usuario.DtoResponse.convertToDto(usuario, mapper);
        response.generateLinks(usuario.getId());

        return response;
    }

    @PutMapping("{id}")
    public Usuario.DtoResponse update(@PathVariable("id") Long id, @RequestBody Usuario.DtoRequest usuario) {
        return repository.findById(id)
                .map(u -> {
                    if (usuario.getUsername() != null) {
                        u.setUsername(usuario.getUsername());
                    }
                    if (usuario.getLogin() != null) {
                        u.setLogin(usuario.getLogin());
                    }
                    if (usuario.getPassword() != null) {
                        u.setPassword(usuario.getPassword());
                    }
                    if (usuario.getEndereco_id() != null) {
                        Endereco novoEnd = endRespository.findById(usuario.getEndereco_id()).get();
                        u.setMeuEndereco(novoEnd);
                    }
                    repository.save(u);

                    return Usuario.DtoResponse.convertToDto(u, mapper);
                }).orElseThrow();
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
    }
}