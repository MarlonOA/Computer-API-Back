package project.ufrn.pw.api_rest.controller;

import org.springframework.web.bind.annotation.*;

import project.ufrn.pw.api_rest.domain.Pedido;
import project.ufrn.pw.api_rest.domain.Produto;
import project.ufrn.pw.api_rest.domain.Pedido.DtoResponse;
import project.ufrn.pw.api_rest.domain.Usuario;
import project.ufrn.pw.api_rest.repository.PedidoRepository;
import project.ufrn.pw.api_rest.repository.ProdutoRepository;
import project.ufrn.pw.api_rest.repository.UsuarioRepository;
import project.ufrn.pw.api_rest.service.PedidoService;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/pedido")
public class PedidoController {
    ProdutoRepository prodRepository;
    UsuarioRepository userRepository;
    PedidoService service;
    PedidoRepository repository;
    ModelMapper mapper;

    public PedidoController(PedidoService service, ModelMapper mapper, PedidoRepository repository, UsuarioRepository userRepository, ProdutoRepository prodRepository) {
        this.service = service;
        this.mapper = mapper;
        this.repository = repository;
        this.userRepository = userRepository;
        this.prodRepository = prodRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido.DtoResponse create(@RequestBody Pedido.DtoRequest p) {
        Pedido ped = Pedido.DtoRequest.convertToEntity(p, mapper);
        Produto prod = prodRepository.findById(p.getProduto_id()).get();
        Usuario user = userRepository.findById(p.getUsuario_id()).get();

        ArrayList<Produto> lista = new ArrayList<>();
        ped.setProducts(lista);
        ped.setUsuario(user);
        ped.getProducts().add(prod);
        service.create(ped);
     
        Pedido.DtoResponse res = Pedido.DtoResponse.convertToDto(ped, mapper);
        res.generateLinks(ped.getId());

        return res;
    }

    @GetMapping
    public List<DtoResponse> list() {
        return this.service.list().stream().map(
            elementoAtual -> {
                Pedido.DtoResponse res = Pedido.DtoResponse.convertToDto(elementoAtual, mapper);
                res.generateLinks(elementoAtual.getId());
                return res;
                
            }).toList();
    }

    @GetMapping("{id}")
    public Pedido.DtoResponse getById(@PathVariable Long id){
        Pedido pedido = this.service.getById(id);
        Pedido.DtoResponse response = Pedido.DtoResponse.convertToDto(pedido, mapper);
        response.generateLinks(pedido.getId());

        return response;
    }

    @PutMapping("{id}")
    public Pedido.DtoResponse update(@PathVariable("id") Long id, @RequestBody Pedido.DtoRequest pedido) {
        return repository.findById(id)
                .map(p -> {
                    if (pedido.getFormaPagamento() != null) {
                        p.setFormaPagamento(pedido.getFormaPagamento());
                    }
                    if(pedido.getUsuario_id() != null){
                        Usuario user = userRepository.findById(pedido.getUsuario_id()).get();
                        p.setUsuario(user);
                    }
                    if(pedido.getProduto_id() != null){
                        Produto prod = prodRepository.findById(pedido.getProduto_id()).get();
                        p.getProducts().add(prod);
                    }
                    repository.save(p);

                    return Pedido.DtoResponse.convertToDto(p, mapper);
                }).orElseThrow();
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
    }
}