package project.ufrn.pw.api_rest.service;

import org.springframework.stereotype.Service;
import project.ufrn.pw.api_rest.domain.Pedido;
import project.ufrn.pw.api_rest.repository.PedidoRepository;

@Service
public class PedidoService extends GenericService<Pedido, PedidoRepository>{
    public PedidoService(PedidoRepository repository){
        super(repository);
    }

    public Pedido update(Pedido c){
        return repository.saveAndFlush(c);
    }
}