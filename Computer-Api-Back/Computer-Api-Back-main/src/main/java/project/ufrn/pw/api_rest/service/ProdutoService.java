package project.ufrn.pw.api_rest.service;

import org.springframework.stereotype.Service;
import project.ufrn.pw.api_rest.domain.Produto;
import project.ufrn.pw.api_rest.repository.ProdutoRepository;

@Service
public class ProdutoService extends GenericService<Produto, ProdutoRepository>{
    public ProdutoService(ProdutoRepository repository){
        super(repository);
    }
}