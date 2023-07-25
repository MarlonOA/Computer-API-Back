package project.ufrn.pw.api_rest.domain;

import java.util.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.modelmapper.ModelMapper;
import project.ufrn.pw.api_rest.controller.PedidoController;
import project.ufrn.pw.api_rest.domain.Pedido;
import org.springframework.hateoas.RepresentationModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@SQLDelete(sql = "UPDATE produto SET deleted_at = CURRENT_TIMESTAMP WHERE id=?")
@Where(clause = "deleted_at is null")
public class Pedido extends AbstractEntity{
    String formaPagamento;
    Float valor;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    Usuario usuario;
    
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "pedidos_produtos",
        joinColumns = {
            @JoinColumn(name = "pedido_id", referencedColumnName = "id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "produto_id")
        }
    )
    List<Produto> products;

    public void partialUpdate(AbstractEntity e) {}

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class DtoResponse extends RepresentationModel<DtoResponse>{
        String formaPagamento;
        Float valor;
        Long usuario_id;
        ArrayList<Long> produtos;

        public static DtoResponse convertToDto(Pedido p, ModelMapper mapper){
            DtoResponse dto = mapper.map(p, DtoResponse.class);
            dto.setUsuario_id(p.getUsuario().getId());
            for(int i=0; i < p.getProducts().size(); i++){
                ArrayList<Long> lista = new ArrayList<>();
                dto.setProdutos(lista);
                Long id = p.getProducts().get(i).getId();
                dto.getProdutos().add(id);
            }
            return dto;
        }

        public void generateLinks(Long id){
             add(linkTo(PedidoController.class).slash(id).withSelfRel());
             add(linkTo(PedidoController.class).slash(id).withRel("delete"));
             add(linkTo(PedidoController.class).withRel("pedido"));
        }
    }

    @Data
    public static class DtoRequest{
        @NotBlank(message = "Sem forma de pagamento")
        String formaPagamento;
        @NotBlank(message = "Insira um usuario")
        Long usuario_id;
        @NotBlank(message = "insira um produto")
        Long produto_id;

        public static Pedido convertToEntity(DtoRequest dto, ModelMapper mapper){ 
            return mapper.map(dto, Pedido.class);
        }
    }
}