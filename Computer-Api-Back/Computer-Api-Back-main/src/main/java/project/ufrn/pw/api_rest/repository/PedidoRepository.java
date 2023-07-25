package project.ufrn.pw.api_rest.repository;

import project.ufrn.pw.api_rest.domain.Pedido;


public interface PedidoRepository extends IGenericRepository<Pedido>{
    Pedido saveAndFlush(Pedido c);
}